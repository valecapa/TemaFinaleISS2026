package utils.cargoservice;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;
import it.unibo.kactor.ActorBasic;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

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

    private void handleClientMessage(String raw) {
        try {
            String[] dispatch = toDispatch(raw);
            sendToActor(dispatch[0], dispatch[1]);
        } catch (Exception e) {
            CommUtils.outred("IOPortWsAdapter | bad message from client: " + raw + " (" + e.getMessage() + ")");
        }
    }

    private void sendToActor(String msgId, String content) {
        IApplMessage dispatch = CommUtils.buildDispatch(SENDER, msgId, content, IOPORT_NAME);
        ioport.sendMsgToMyself(dispatch);
    }

    public void updateDisplay(String state, String hold, String msg) {
        broadcast(displayJson(state, hold, msg));
    }


    public void notifySensor(String flag) {
        broadcast(sensorJson(flag));
    }

    private void broadcast(String json) {
        clients.forEach(ctx -> ctx.send(json));
    }


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

    static String sensorJson(String flag) {
        JSONObject json = new JSONObject();
        json.put("type", "sensor");
        json.put("occupied", "true".equals(flag));
        return json.toJSONString();
    }
}
