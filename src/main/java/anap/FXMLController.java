/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anap;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Lykou
 */
public class FXMLController implements Initializable {

    @FXML
    private Button Sid;

    @FXML
    private TableView<DbDetails> tableUser;
    @FXML
    private TableColumn<DbDetails, String> columnDeviceId;
    @FXML
    private TableColumn<DbDetails, String> columnProximity;
    @FXML
    private TableColumn<DbDetails, String> columnLight;
    @FXML
    private TableColumn<DbDetails, String> columnLatitude;
    @FXML
    private TableColumn<DbDetails, String> columnLongitude;
    private ObservableList<DbDetails> data;
    private MysqlConnection dc;
    @FXML
    private TableColumn<DbDetails, String> columnTime;
    @FXML
    private TextField textProximity;
    @FXML
    private TextField textDeviceId;
    @FXML
    private TextField textLight;
    @FXML
    private TextField textLatitude;
    @FXML
    private TextField textLongitude;
    @FXML
    private Button Rid;
    @FXML
    private TextField textTime;
    @FXML
    private TableColumn<DbDetails, String> columnDeviceId2;
    @FXML
    private TableColumn<DbDetails, String> columnTime2;
    @FXML
    private TableColumn<DbDetails, String> columnProximity2;
    @FXML
    private TableColumn<DbDetails, String> columnLight2;
    @FXML
    private TableColumn<DbDetails, String> columnLatitude2;
    @FXML
    private TableColumn<DbDetails, String> columnLongitude2;
    @FXML
    private ChoiceBox<String> ProxT;
    @FXML
    private ChoiceBox<Float> LightT;

    /**
     * Handles the Light Threshold
     */
    @FXML
    public volatile static Float ThresHoldL;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dc = new MysqlConnection();
        ProxT.setItems(FXCollections.observableArrayList("Near", "Far"));
        LightT.setItems(FXCollections.observableArrayList(50.0f, 100.0f, 200.0f));
        ProxT.setValue("Near");
        ThresHoldL = 100.0f;
        LightT.setValue(100.0f);
        LightT.getSelectionModel()
                .selectedItemProperty()
                .addListener((ObservableValue<? extends Float> observable, Float oldValue, Float newValue) -> {
                    ThresHoldL = newValue;
                });
    }

    @FXML
    private void SearchOnDb(ActionEvent event) {
        try {
            Connection conn = dc.dbConnector();
            data = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Android");
            while (rs.next()) {
                //get string from db,whichever way 
                data.add(new DbDetails(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

        //Set cell value factory to tableview.
        //NB.PropertyValue Factory must be the same with the one set in model class.
        columnDeviceId.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("DeviceId"));
        columnTime.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("time"));
        columnProximity.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("proximity"));
        columnLight.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("light"));
        columnLatitude.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("latitude"));
        columnLongitude.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("longitude"));
        columnDeviceId2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("DeviceId2"));
        columnTime2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("time2"));
        columnProximity2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("proximity2"));
        columnLight2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("light2"));
        columnLatitude2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("latitude2"));
        columnLongitude2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("longitude2"));
        tableUser.setRowFactory(e -> new TableRow<DbDetails>() {
            @Override
            protected void updateItem(DbDetails item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.Confirmed()) {
                    setStyle("-fx-background-color:lightcoral");
                } else if (!item.Confirmed()) {
                    setStyle("-fx-background-color:lightgreen");
                } else {
                    setStyle("");
                }
            }
        });

        tableUser.setItems(null);
        tableUser.setItems(data);
    }

    @FXML
    private void SearchSpecific(ActionEvent event) throws SQLException {

        Thread SearchSp = new Thread() {
            String devid, time, proximity, light, lat, lon;

            @Override
            public void run() {

                devid = textDeviceId.getText();
                time = textTime.getText();
                proximity = textProximity.getText();
                light = textLight.getText();
                lat = textLatitude.getText();
                lon = textLongitude.getText();
                String Query = "SELECT * FROM Android WHERE 1=1";
                if (devid.matches("^\\s*$") == false) {
                    Query = Query + " && (And_Id='" + devid + "' || And_Id2='" + devid + "')";
                }
                if (time.matches("^\\s*$") == false) {
                    Query = Query + " && (Time='" + time + "' || Time2='" + time + "')";
                }
                if (proximity.matches("^\\s*$") == false) {
                    Query = Query + " && (Proximity='" + proximity + "' || Proximity2='" + proximity + "')";
                }
                if (light.matches("^\\s*$") == false) {
                    Query = Query + " && (Light='" + light + "' || Light2='" + light + "')";
                }
                if (lat.matches("^\\s*$") == false) {
                    Query = Query + " && (Latitude='" + lat + "' || Latitude2='" + lat + "')";
                }
                if (lon.matches("^\\s*$") == false) {
                    Query = Query + " && (Longitude='" + lon + "' || Longitude2='" + lon + "')";
                }
                Connection con = MysqlConnection.dbConnector();
                data = FXCollections.observableArrayList();

                try {
                    PreparedStatement stmt = con.prepareStatement(Query);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        //get string from db,whichever way 
                        data.add(new DbDetails(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12)));

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //System.out.println("102");
                    e.printStackTrace();
                }

                columnDeviceId.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("DeviceId"));
                columnTime.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("time"));
                columnProximity.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("proximity"));
                columnLight.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("light"));
                columnLatitude.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("latitude"));
                columnLongitude.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("longitude"));
                columnDeviceId2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("DeviceId2"));
                columnTime2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("time2"));
                columnProximity2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("proximity2"));
                columnLight2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("light2"));
                columnLatitude2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("latitude2"));
                columnLongitude2.setCellValueFactory(new PropertyValueFactory<DbDetails, String>("longitude2"));
                tableUser.setRowFactory(e -> new TableRow<DbDetails>() {
                    @Override
                    protected void updateItem(DbDetails item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setStyle("");
                        } else if (item.Confirmed()) {
                            setStyle("-fx-background-color:lightcoral");
                        } else if (!item.Confirmed()) {
                            setStyle("-fx-background-color:lightgreen");
                        } else {
                            setStyle("");
                        }
                    }
                });
                tableUser.setItems(null);
                tableUser.setItems(data);
            }
        };
        SearchSp.start();
    }

}
