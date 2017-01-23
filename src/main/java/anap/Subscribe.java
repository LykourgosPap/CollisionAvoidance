package anap;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Subscribe implements MqttCallback {

    public static void sub() {
        String topic = "Register";
        int qos = 2;
        String broker = "tcp://localhost:1883";
        String clientId = "JavaSampleSubscriber";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            //Connect	client	to	MQTT	Broker
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            //Set	callback
            Subscribe sub = new Subscribe();
            sampleClient.setCallback(sub);
            System.out.println("Connecting	to	broker:	" + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            //Subscribe	to	a	topic
            System.out.println("Subscribing	to	topic	\"" + topic + "\"	qos " + qos);
            sampleClient.subscribe(topic, qos);
        } catch (MqttException me) {
            System.out.println("reason	" + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause	" + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    /**
     * @see	MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        //		This	method	is	called	when	the	connection	to	the	server	is	lost.
        System.out.println("Connection	lost!" + cause);
        System.exit(1);
    }

    /**
     * @see	MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
        //Called	when	delivery	for	a	message	has	been	completed,	and	all	acknowledgments	have	been	received
    }

    /**
     * @see	MqttCallback#messageArrived(String,	MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException, SQLException {
        //This	method	is	called	when	a	message	arrives	from	the	server.
        //Get message and break it into strings that will be used for acces in the database
        String time = getDateTime();
        String id, prox, light, lat, lon, msg;
        msg = new String(message.getPayload());
        int iid = msg.indexOf(",");
        int iprox = msg.indexOf(",,");
        int ilight = msg.indexOf(",,,");
        int ilat = msg.indexOf(",,,,");
        id = msg.substring(0, iid);
        prox = msg.substring(iid + 1, iprox);
        light = msg.substring(iprox + 2, ilight);
        lat = msg.substring(ilight + 3, ilat);
        lon = msg.substring(ilat + 4);
        //Check thresholds and proceed accordingly 
        if (CheckTh(prox, light)) {
            Connection con = MysqlConnection.dbConnector();
            PreparedStatement st = con.prepareStatement("UPDATE Android SET And_Id2='" + id + "',Time2='" + time + "',Proximity2='" + prox + "',Light2='" + light + "',Latitude2='" + lat + "',Longitude2='" + lon + "' WHERE (SELECT TIMESTAMPDIFF(SECOND,TIME,'" + time + "')) < 1 && And_Id!='" + id + "';");
            try {
                int row = st.executeUpdate();
                st = con.prepareStatement("Insert into Android (And_Id,Time,Proximity,Light,Latitude,Longitude) VALUES('" + id + "',\"" + time + "\",'" + prox + "','" + light + "','" + lat + "','" + lon + "');");
                st.executeUpdate();
                if (row >= 1) {
                    st = con.prepareStatement("SELECT * FROM Android WHERE (SELECT TIMESTAMPDIFF(SECOND,TIME,'" + time + "')) < 1 && And_Id!='" + id + "' LIMIT 1;");
                    ResultSet rs = st.executeQuery();
                    Thread publish = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Publish.pub(id, "Danger! Confirmed crash!!!");
                                Publish.pub(rs.getString(1), "Danger! Confirmed crash!!!");
                            } catch (SQLException ex) {
                                Logger.getLogger(Subscribe.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    };
                    publish.start();
                    while (rs.next()) {
                        st = con.prepareStatement("UPDATE Android SET And_Id2='" + rs.getString(1) + "',Time2='" + rs.getString(2) + "',Proximity2='" + rs.getString(3) + "',Light2='" + rs.getString(4) + "',Latitude2='" + rs.getString(5) + "',Longitude2='" + rs.getString(6) + "' WHERE (And_Id='" + id + "' && TIME='" + time + "');");
                        st.executeUpdate();
                    }
                }
                if (row == 0) {
                    Thread publish = new Thread() {
                        @Override
                        public void run() {
                            Publish.pub(id, "Be carefull, Possible Crash");
                        }
                    };
                    publish.start();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private boolean CheckTh(String ProximityT, String LightT) {
        if (Float.parseFloat(ProximityT) == 0 || Float.parseFloat(LightT) < FXMLController.ThresHoldL) {
            return true;
        } else {
            return false;
        }
    }

}
