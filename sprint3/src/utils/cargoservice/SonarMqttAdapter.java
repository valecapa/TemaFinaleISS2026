package utils.cargoservice;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.kactor.ActorBasic;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.mqtt.MqttSupport;
import unibo.basicomm23.utils.CommUtils;

public class SonarMqttAdapter {

    private static final String SENDER = "sonarmqtt";

    private final ActorBasic sonar;
    private final MqttSupport mqtt;
    private final String ledTopic;

    public SonarMqttAdapter(
            ActorBasic sonar,
            String brokerAddr,
            String distanceTopic,
            String ledTopic) {

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
                CommUtils.outred(
                    "SonarMqttAdapter | connessione MQTT persa: "
                    + cause.getMessage()
                );
            }

            @Override
            public void deliveryComplete(
                    org.eclipse.paho.client.mqttv3.IMqttDeliveryToken token) {
            }
        });
    }

    private void deliverReading(String rawDistance) {
        IApplMessage ev = CommUtils.buildEvent(
            SENDER,
            "sonarreading",
            "sonarreading(" + rawDistance + ")"
        );

        sonar.sendMsgToMyself(ev);
    }

    public void sendLed(boolean flag) {
        mqtt.publish(ledTopic, flag ? "on" : "off");
    }
}
