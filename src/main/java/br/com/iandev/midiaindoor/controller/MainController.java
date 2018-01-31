package br.com.iandev.midiaindoor.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController extends Controller {

    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        openPlayer(null);
    }

    @FXML
    private void action(ActionEvent event) {

    }

    @FXML
    public void loadRegistry(ActionEvent event) {
//        loadContent(mainPane, getResource(viewPath("registry/registry.fxml")));
        mainPane.setCenter((Node) loadContent(getResource(viewPath("registry/registry.fxml"))));
    }

    @FXML
    public void loadContent(ActionEvent event) {
//        loadContent(mainPane, getResource(viewPath("content/content.fxml")));
        mainPane.setCenter((Node) loadContent(getResource(viewPath("content/content.fxml"))));
    }

    @FXML
    public void loadDevice(ActionEvent event) {
//        loadContent(mainPane, getResource(viewPath("device/device.fxml")));
        mainPane.setCenter((Node) loadContent(getResource(viewPath("device/device.fxml"))));
    }

    @FXML
    public void openPlayer(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getResource(viewPath("player/player.fxml")));
            Parent root = loader.load();
            PlayerController controller = loader.getController();
            Scene scene = new Scene(root);

//        Stage
            Stage stage = new Stage();
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Player");

            stage.setScene(scene);
            stage.setOnHidden(e -> controller.shutdown());
//            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {

        }
    }

}
