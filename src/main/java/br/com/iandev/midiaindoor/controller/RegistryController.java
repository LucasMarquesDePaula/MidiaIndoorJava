package br.com.iandev.midiaindoor.controller;

import br.com.iandev.midiaindoor.settings.LicenseSettings;
import br.com.iandev.midiaindoor.task.Licensor;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class RegistryController extends Controller {

    @FXML
    JFXTextField deviceCode;

    @FXML
    JFXTextField deviceID;

    @FXML
    JFXTextField deviceDescription;

    @FXML
    JFXTextField ownerID;

    @FXML
    JFXTextField password;

    @FXML
    JFXTextField url;

    @FXML
    JFXButton apply;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final LicenseSettings licenseSettings = new LicenseSettings();

        loadSettings(licenseSettings);

        apply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                apply.setText("Registrando");
                apply.setDisable(true);

                new Thread(new Licensor() {
                    @Override
                    public void before() {
                        saveSettings(licenseSettings);
                        loadSettings(licenseSettings);
                    }

                    @Override
                    public void success(LicenseSettings response) {
                        saveSettings(licenseSettings);
                        Platform.runLater(() -> {
                            apply.setText("Registrado com sucesso!");
                        });
                    }

                    @Override
                    public void error(Exception response) {
                        Platform.runLater(() -> {
                            apply.setText("Erro ao registrar!");
                        });
                    }

                    @Override
                    public void complete() {
                        Platform.runLater(() -> {
                            apply.setDisable(false);
                        });
                    }
                }).start();
            }
        });
    }

    private void loadSettings(LicenseSettings licenceSettings) {
        deviceCode.setDisable(true);
        deviceCode.setText(licenceSettings.getCode());

        deviceID.setDisable(true);
        deviceID.setText(licenceSettings.getDeviceId());

        deviceDescription.setText(licenceSettings.getDeviceDescription());
        ownerID.setText(licenceSettings.getOwnerId());
        password.setText(licenceSettings.getPassword());
        url.setText(licenceSettings.getURL());
    }

    private void saveSettings(LicenseSettings licenceSettings) {
        licenceSettings.setDeviceDescription(deviceDescription.getText());
        licenceSettings.setOwnerID(ownerID.getText());
        licenceSettings.setPassword(password.getText());
        licenceSettings.setURL(url.getText());
    }
}
