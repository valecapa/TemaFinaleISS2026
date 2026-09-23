package utils.cargoservice;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.kactor.ActorBasic;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.mqtt.MqttSupport;
import unibo.basicomm23.utils.CommUtils;

/**
 * Ponte MQTT fra l'attore qak "sonar" e il PicoW.
 * Stesso pattern di IOPortWsAdapter (un withobj che incapsula la comunicazione
 * esterna e inietta messaggi nell'attore con sendMsgToMyself), qui basato su
 * MqttSupport (unibo.basicomm23) invece che su Javalin/WebSocket.
 *
 * La logica di soglia/debounce NON sta qui: resta interamente nel file .qak
 * (attore sonar). Questa classe fa solo I/O MQTT, per lo stesso motivo per
 * cui Hold.java tiene la logica di dominio e non la comunicazione.
 */
public class SonarMqttAdapter {

    private static final String SENDER = "sonarmqtt";

    private final ActorBasic sonar;
    private final MqttSupport mqtt;
    private final String ledTopic;

    public SonarMqttAdapter(ActorBasic sonar, String brokerAddr, String distanceTopic, String ledTopic) {
        this.sonar = sonar;
        this.ledTopic = ledTopic;
        this.mqtt = new MqttSupport();
        this.mqtt.connectToBroker("sonarAdapter", brokerAddr);
        this.mqtt.subscribe(distanceTopic, new MqttCallback() {
            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                deliverReading(payload);
            }

            @Override
            public void connectionLost(Throwable cause) {
                CommUtils.outred("SonarMqttAdapter | connessione MQTT persa: " + cause.getMessage());
            }

            @Override
            public void deliveryComplete(org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
                // non usato: qui non pubblichiamo mai con conferma di consegna
            }
        });
    }

    private void deliverReading(String rawDistance) {
        // il PicoW pubblica il solo valore numerico (es. "23.4"), non un messaggio
        // qak gia' formattato: e' questo adapter a costruire l'Event ed iniettarlo
        // nell'attore sonar, esattamente come IOPortWsAdapter.sendToActor fa per i
        // Dispatch da browser, ma con buildEvent al posto di buildDispatch.
        IApplMessage ev = CommUtils.buildEvent(SENDER, "sonarreading", "sonarreading(" + rawDistance + ")");
        sonar.sendMsgToMyself(ev);
    }

    public void sendLed(boolean flag) {
        mqtt.publish(ledTopic, flag ? "on" : "off");
    }
}
