package br.com.iandev.midiaindoor.controller;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.model.Channel;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.model.Person;
import br.com.iandev.midiaindoor.task.Synchronizer;
import br.com.iandev.midiaindoor.util.ViewUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class DeviceController extends Controller {

    @FXML
    StackPane root;

    @FXML
    JFXTextField id;

    @FXML
    JFXTextField description;

    @FXML
    JFXTextField status;

    @FXML
    JFXTextField person;

    @FXML
    JFXTextField channel;

    @FXML
    JFXTextField timeZone;

    @FXML
    JFXTextField updateInterval;

    @FXML
    JFXTextField updateToleranceInterval;

    @FXML
    JFXTextField lastUpdateDate;

    @FXML
    JFXTextField lastUpdateAttemptDate;

    @FXML
    JFXTextField nextUpdateDate;

    @FXML
    JFXCheckBox mustUpdate;

    @FXML
    JFXCheckBox outOfDate;

    @FXML
    JFXTextField lastAuthenticationDate;

    @FXML
    JFXTextField tokenExpirationInterval;

    @FXML
    JFXTextField token;

    @FXML
    JFXTextField ip;

    @FXML
    JFXButton updateDevice;

    private final DeviceFacede deviceFacede = new DeviceFacede();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
//        JFXSnackbar bar = new JFXSnackbar(root);
//        bar.setBackground(new Background(new BackgroundFill(
//                Color.BLACK,
//                CornerRadii.EMPTY,
//                Insets.EMPTY
//        )));
//        bar.enqueue(new SnackbarEvent("Notification Msg"));
    }

    protected void refresh() {
        Device device;
        try {
            device = deviceFacede.get();
        } catch (Exception ex) {
            device = new Device();
        }

        id.setText(ViewUtil.getString(device.getId()));
        id.setEditable(false);

        description.setText(ViewUtil.getString(device.getDescription()));
        description.setEditable(false);

        status.setText(ViewUtil.getString(device.getStatus()));
        status.setEditable(false);

        try {
            channel.setText(ViewUtil.getString(device.getChannel()));
        } catch (Exception ex) {
            channel.setText(ViewUtil.getString(new Channel()));
        }
        channel.setEditable(false);

        try {
            person.setText(ViewUtil.getString(device.getPerson()));
        } catch (Exception ex) {
            person.setText(ViewUtil.getString(new Person()));
        }
        person.setEditable(false);

        timeZone.setText(ViewUtil.getString(device.getTimeZone()));
        timeZone.setEditable(false);

        updateInterval.setText(ViewUtil.getInterval(device.getUpdateInterval()));
        updateInterval.setEditable(false);

        updateToleranceInterval.setText(ViewUtil.getInterval(device.getUpdateToleranceInterval()));
        updateToleranceInterval.setEditable(false);

        lastUpdateDate.setText(ViewUtil.getString(device.getLastUpdateDate(), device.getTimeZone()));
        lastUpdateDate.setEditable(false);

        lastUpdateAttemptDate.setText(ViewUtil.getString(device.getLastUpdateAttemptDate(), device.getTimeZone()));
        lastUpdateAttemptDate.setEditable(false);

        try {
            nextUpdateDate.setText(ViewUtil.getString(device.getNextUpdateDate(), device.getTimeZone()));
        } catch (Exception ex) {
            nextUpdateDate.setText("");
        }
        nextUpdateDate.setEditable(false);

        App app = App.getInstance();
        try {
            mustUpdate.setSelected(device.mustUpdate(app.getTimeCounter().getDate()));
        } catch (Exception ex) {
            mustUpdate.setSelected(false);
        }
        mustUpdate.setDisable(true);

        try {
            outOfDate.setSelected(device.outOfDate(app.getTimeCounter().getDate()));
        } catch (Exception ex) {
            outOfDate.setSelected(true);
        }
        outOfDate.setDisable(true);

        lastAuthenticationDate.setText(ViewUtil.getString(device.getLastAuthenticationDate(), device.getTimeZone()));
        lastAuthenticationDate.setEditable(false);

        tokenExpirationInterval.setText(ViewUtil.getInterval(device.getTokenExpirationInterval()));
        tokenExpirationInterval.setEditable(false);

        token.setText(ViewUtil.getString(device.getToken()));
        token.setEditable(false);

        ip.setText(ViewUtil.getString(device.getIp()));
        ip.setEditable(false);

    }

    @FXML
    public void updateDevice(ActionEvent event) {
        updateDevice.setText("Atualizando...");
        updateDevice.setDisable(true);
        new Thread(new Synchronizer() {
            @Override
            public void success(Device device) {
                Platform.runLater(() -> {
                    updateDevice.setText("Atualizado com sucesso!");
                });
            }

            @Override
            public void error(Exception response) {
                Platform.runLater(() -> {
                    updateDevice.setText("Erro ao atualizar!");
                });
            }

            @Override
            public void complete() {
                Platform.runLater(() -> {
                    updateDevice.setDisable(false);
                    refresh();
                });
            }
        }).start();
    }

}
