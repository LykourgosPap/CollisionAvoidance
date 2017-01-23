package anap;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publish {

    public static void pub(String topic, String msg) {
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "1";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            //Connect	to	MQTT	Broker
            MqttClient hostClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting	to	broker:	" + broker);
            hostClient.connect(connOpts);
            System.out.println("Connected");
            //Publish	message	to	MQTT	Broker
            System.out.println("Publishing	message:	" + msg);
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(qos);
            hostClient.publish(topic, message);
            System.out.println("Message	published");
            hostClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason	" + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause	" + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
