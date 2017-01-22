package dit.anaptyksh;



import android.util.Log;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



public	class	Subscribe	implements	MqttCallback {
    private static final String TAG = "MyActivity";

        public static void sub(String dID) {
            String topic = dID;
            int qos = 2;
            String broker = "tcp://192.168.1.8:1883";
            String clientId = dID;
            MemoryPersistence persistence = new MemoryPersistence();
            try {
//Connect	client	to	MQTT	Broker
                MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true);
//Set	callback
                Subscribe sub = new Subscribe();
                sampleClient.setCallback(sub);
                Log.i(TAG, "Connecting	to	broker:	" + broker);
                sampleClient.connect(connOpts);
                Log.i(TAG,"Connected");
//Subscribe	to	a	topic
                Log.i(TAG,"Subscribing	to	topic	\"" + topic + "\"	qos " + qos);
                sampleClient.subscribe(topic, qos);
            } catch (MqttException me) {
                Log.e(TAG,"reason	" + me.getReasonCode());
                Log.e(TAG,"msg " + me.getMessage());
                Log.e(TAG,"loc " + me.getLocalizedMessage());
                Log.e(TAG,"cause	" + me.getCause());
                Log.e(TAG,"excep " + me);
                me.printStackTrace();
            }
        }

        public	void	connectionLost(Throwable cause)	{
//	This	method	is	called	when	the	connection	to	the	server	is	lost.
            Log.e(TAG,"Connection	lost!"	+	cause);
            System.exit(1);
        }

        public	void	deliveryComplete(IMqttDeliveryToken token)	{
//Called	when	delivery	for	a	message	has	been	completed,	and	all	acknowledgments	have	been	received
        }

        public	void	messageArrived(String	topic,	MqttMessage message)	throws	MqttException {
//This	method	is	called	when	a	message	arrives	from	the	server.
            final String msg = new String(message.getPayload());
            MainActivity.mHandler.post(new Runnable(){
                public void run(){
                    Toast.makeText(Online_Activity.context, msg, Toast.LENGTH_SHORT).show();
                   Online_Activity.mypool2.play(Online_Activity.AlertId, 1, 1, 1, 0, 1);
                }
            });
        }
    }