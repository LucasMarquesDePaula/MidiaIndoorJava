package br.com.iandev.midiaindoor;

import br.com.iandev.midiaindoor.controller.PlayerController;
import br.com.iandev.midiaindoor.core.App;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static String[] args;

    public static void main(String[] args) {
        App.initialize();
        Main.launch(args);
    }

    public static void launch(String[] args) {
        Main.args = args;
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            if (true || args[0].equals("main")) {
                loadMain(stage);
            }
        } catch (Exception ex) {
            loadPlayer(stage);
        }
        stage.setOnCloseRequest((javafx.stage.WindowEvent event) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void loadMain(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/main/main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void loadPlayer(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/player/player.fxml"));

        Parent root = loader.load();
        PlayerController controller = loader.getController();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setOnHidden(e -> controller.shutdown());
//        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
    }

}
