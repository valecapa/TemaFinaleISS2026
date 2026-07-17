package utils.cargoservice;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.MsgUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
 * IOPortWsAdapter - bridge tra l'attore qak "ioport" e la GUI web (ioport_gui.html).
 *
 * Stessa idea di conway.io.IoJavalin / guiout.JavalinGuiHandler (conway26GuiHtml,
 * griddisplay): Javalin serve la pagina statica + una websocket. Qui in piu' la
 * ws e' bidirezionale: i comandi del browser (pushButton/setOccupied) diventano
 * veri Dispatch iniettati nell'attore con ActorBasic.sendMsgToMyself (nessuna
 * chiamata diretta a Hold/IOPortStatus dal lato web), e gli update dell'attore
 * (updateDisplay/blinkLed) vengono spinti a tutti i client connessi come JSON.
 *
 * Protocollo:
 *   client -> server: {"type":"pushButton"} | {"type":"setOccupied","value":true}
 *   server -> client: {"type":"display","state":"ENGAGED","hold":"1/4","msg":"..."}
 *                     {"type":"led","on":true}
 */
public class IOPortWsAdapter {

    private static final String SENDER = "ioportws";
    private static final String IOPORT_NAME = "ioport"; // nome dell'attore nel .qak, non generico di proposito

    private final ActorBasic ioport;
    private final Set<WsContext> clients = new CopyOnWriteArraySet<>();

    public IOPortWsAdapter(ActorBasic ioport, int port) {
        this.ioport = ioport;

        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/page";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(port);

        app.get("/", ctx -> ctx.redirect("/ioport_gui.html"));

        app.ws("/ws/ioport", ws -> {
            ws.onConnect(ctx -> {
                clients.add(ctx);
                CommUtils.outgreen("IOPortWsAdapter | client connected: " + ctx.session.getRemoteAddress());
            });
            ws.onClose(ctx -> {
                clients.remove(ctx);
                CommUtils.outyellow("IOPortWsAdapter | client disconnected");
            });
            ws.onMessage(ctx -> handleClientMessage(ctx.message()));
        });

        CommUtils.outgreen("IOPortWsAdapter | started on port " + port + " (gui: http://localhost:" + port + "/ioport_gui.html)");
    }

    // ponytail: un solo client atteso in Sprint1 (un IOPort fisico = una GUI);
    // il broadcast a piu' client e' gia' gratis con il Set ma non serializza
    // eventuali pushButton concorrenti, che restano comunque seriali una volta
    // dentro l'inbox dell'attore ioport.
    private void handleClientMessage(String raw) {
        try {
            String[] dispatch = toDispatch(raw);
            sendToActor(dispatch[0], dispatch[1]);
        } catch (Exception e) {
            CommUtils.outred("IOPortWsAdapter | bad message from client: " + raw + " (" + e.getMessage() + ")");
        }
    }

    private void sendToActor(String msgId, String content) {
        IApplMessage dispatch = MsgUtil.buildDispatch(SENDER, msgId, content, IOPORT_NAME);
        ioport.sendMsgToMyself(dispatch);
    }

    /** Chiamato dall'attore ioport quando cambia stato/hold/messaggio da mostrare. */
    public void updateDisplay(String state, String hold, String msg) {
        broadcast(displayJson(state, hold, msg));
    }

    /** Chiamato dall'attore ioport alla ricezione del Dispatch blinkLed. */
    public void setLed(String onOff) {
        broadcast(ledJson(onOff));
    }

    /** Riflette sulla GUI lo stato forzato del sensore (dispatch di test setOccupied). */
    public void notifySensor(String flag) {
        broadcast(sensorJson(flag));
    }

    private void broadcast(String json) {
        clients.forEach(ctx -> ctx.send(json));
    }

    /*
     * ------------------------------------------------------------------
     * Protocollo JSON: pure functions, testabili senza Javalin ne' attore
     * (vedi utils/cargoservice/IOPortWsAdapterTest.java)
     * ------------------------------------------------------------------
     */

    /** Converte un messaggio client (JSON) in {msgId, content} per un Dispatch qak. */
    static String[] toDispatch(String raw) throws Exception {
        JSONObject json = (JSONObject) new JSONParser().parse(raw);
        String type = (String) json.get("type");
        if ("pushButton".equals(type)) {
            return new String[] { "pushButton", "pushButton(0)" };
        } else if ("setOccupied".equals(type)) {
            boolean value = Boolean.TRUE.equals(json.get("value"));
            return new String[] { "setOccupied", "setOccupied(" + value + ")" };
        }
        throw new IllegalArgumentException("unknown message type: " + type);
    }

    static String displayJson(String state, String hold, String msg) {
        JSONObject json = new JSONObject();
        json.put("type", "display");
        json.put("state", state);
        json.put("hold", hold);
        json.put("msg", msg);
        return json.toJSONString();
    }

    static String ledJson(String onOff) {
        JSONObject json = new JSONObject();
        json.put("type", "led");
        json.put("on", "on".equals(onOff));
        return json.toJSONString();
    }

    static String sensorJson(String flag) {
        JSONObject json = new JSONObject();
        json.put("type", "sensor");
        json.put("occupied", "true".equals(flag));
        return json.toJSONString();
    }
}
