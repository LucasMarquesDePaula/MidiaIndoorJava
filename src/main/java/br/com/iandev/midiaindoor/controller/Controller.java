package br.com.iandev.midiaindoor.controller;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;

public abstract class Controller implements Initializable {

    protected static final String viewPath(String view) {
        return String.format("view/%s", view);
    }

    protected static void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(Controller.class.getClassLoader().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class).error("", ex);
        }
    }

    public void loadContent(Pane pane, URL resource) {
        try {
            Pane newPane = FXMLLoader.load(resource);
            newPane.prefWidthProperty().bind(pane.prefWidthProperty());
            newPane.prefHeightProperty().bind(pane.prefHeightProperty());
            pane.getChildren().clear();
            pane.getChildren().add(newPane);
        } catch (Exception ex) {
            Logger.getLogger(this.getClass()).error("", ex);
        }
    }

    public Object loadContent(URL resource) {
        try {
            return FXMLLoader.load(resource);
        } catch (IOException ex) {
            return null;
        }
    }

    protected static URL getResource(String path) {
        return Controller.class.getClassLoader().getResource(path);
    }

}
