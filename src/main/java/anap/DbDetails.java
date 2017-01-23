// This class acts as a model class,holding getters,setters and properties
package anap;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author admin
 */
public class DbDetails {

    private final StringProperty DeviceId;
    private final StringProperty time;
    private final StringProperty proximity;
    private final StringProperty light;
    private final StringProperty latitude;
    private final StringProperty longitude;
    private final StringProperty DeviceId2;
    private final StringProperty time2;
    private final StringProperty proximity2;
    private final StringProperty light2;
    private final StringProperty latitude2;
    private final StringProperty longitude2;

    //Default constructor
    public DbDetails(String did, String time, String prox, String lig, String lat, String lon, String did2, String time2, String prox2, String lig2, String lat2, String lon2) {
        this.DeviceId = new SimpleStringProperty(did);
        this.proximity = new SimpleStringProperty(prox);
        this.light = new SimpleStringProperty(lig);
        this.latitude = new SimpleStringProperty(lat);
        this.longitude = new SimpleStringProperty(lon);
        this.time = new SimpleStringProperty(time);
        this.DeviceId2 = new SimpleStringProperty(did2);
        this.proximity2 = new SimpleStringProperty(prox2);
        this.light2 = new SimpleStringProperty(lig2);
        this.latitude2 = new SimpleStringProperty(lat2);
        this.longitude2 = new SimpleStringProperty(lon2);
        this.time2 = new SimpleStringProperty(time2);
    }

    //Getters
    public String getDeviceId() {
        return DeviceId.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getProximity() {
        return proximity.get();
    }

    public String getLight() {
        return light.get();
    }

    public String getLatitude() {
        return latitude.get();
    }

    public String getLongitude() {
        return longitude.get();
    }

    public String getDeviceId2() {
        return DeviceId2.get();
    }

    public String getTime2() {
        return time2.get();
    }

    public String getProximity2() {
        return proximity2.get();
    }

    public String getLight2() {
        return light2.get();
    }

    public String getLatitude2() {
        return latitude2.get();
    }

    public String getLongitude2() {
        return longitude2.get();
    }

    public boolean Confirmed() {
        if (DeviceId2.get() != null) {
            return true;
        } else {
            return false;
        }
    }

    //Setters
    public void setDeviceId(String value) {
        DeviceId.set(value);
    }

    public void setTime(String value) {
        time.set(value);
    }

    public void setProximity(String value) {
        proximity.set(value);
    }

    public void setLight(String value) {
        light.set(value);
    }

    public void setLatitude(String value) {
        latitude.set(value);
    }

    public void setLongitude(String value) {
        longitude.set(value);
    }

    public void setDeviceId2(String value) {
        DeviceId2.set(value);
    }

    public void setTime2(String value) {
        time2.set(value);
    }

    public void setProximity2(String value) {
        proximity2.set(value);
    }

    public void setLight2(String value) {
        light2.set(value);
    }

    public void setLatitude2(String value) {
        latitude2.set(value);
    }

    public void setLongitude2(String value) {
        longitude2.set(value);
    }

    //Property values
    public StringProperty DeviceIdProperty() {
        return DeviceId;
    }

    public StringProperty TimeProperty() {
        return time;
    }

    public StringProperty ProximityProperty() {
        return proximity;
    }

    public StringProperty LightProperty() {
        return light;
    }

    public StringProperty LatitudeProperty() {
        return latitude;
    }

    public StringProperty LongitudeProperty() {
        return longitude;
    }

    public StringProperty DeviceIdProperty2() {
        return DeviceId2;
    }

    public StringProperty TimeProperty2() {
        return time2;
    }

    public StringProperty ProximityProperty2() {
        return proximity2;
    }

    public StringProperty LightProperty2() {
        return light2;
    }

    public StringProperty LatitudeProperty2() {
        return latitude2;
    }

    public StringProperty LongitudeProperty2() {
        return longitude2;
    }
}
