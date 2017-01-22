package dit.anaptyksh;

import	org.eclipse.paho.client.mqttv3.MqttClient;
import	org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import	org.eclipse.paho.client.mqttv3.MqttException;
import	org.eclipse.paho.client.mqttv3.MqttMessage;
import	org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publish {
    public static void pub(String dID, float prox, float light, double lat, double lon) {
        String topic = "Register";
        String content = String.valueOf(dID) + "," + String.valueOf(prox) + ",," + String.valueOf(light) + ",,," +String.valueOf(lat) + ",,,," + String.valueOf(lon);
        int qos = 2;
        String broker = "tcp://"+Online_Activity.MQTT_IP+":1883";
        String clientId = "Pub"+dID;
        MemoryPersistence persistence = new MemoryPersistence();
        try {
//Connect	to	MQTT	Broker
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts);
           // System.out.println("Connected");
//Publish	message	to	MQTT	Broker
            //System.out.println("Publishing	message:	" + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message	published");
            sampleClient.disconnect();
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
