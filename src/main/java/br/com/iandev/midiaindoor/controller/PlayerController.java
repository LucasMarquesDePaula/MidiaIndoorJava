package br.com.iandev.midiaindoor.controller;

import br.com.iandev.midiaindoor.core.App;
import br.com.iandev.midiaindoor.core.interfaces.Playable;
import br.com.iandev.midiaindoor.facede.DeviceFacede;
import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.services.TimerService;
import br.com.iandev.midiaindoor.task.Player;
import br.com.iandev.midiaindoor.task.Task;
import br.com.iandev.midiaindoor.util.IntervalUtil;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class PlayerController extends Controller {

    @FXML
    private WebView webView;

    private String defaultUrl = "";
    private PlayerService playerService;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }

                String script = "onPageFinished();";

                try {
                    App app = App.getInstance();
                    Device device = new DeviceFacede().get();
                    script = String.format("onPageFinished({ timeZone: %d, startTime: %d });", device.getTimeZone().getRawOffset(), app.getTimeCounter().getTime());
                } catch (Exception ignored) {

                }

                engine.executeScript(script);
            }
        });

        this.defaultUrl = this.getClass().getClassLoader().getResource("www/clock/index.html").toExternalForm();

        playerService = new PlayerService();

        playerService.start();

    }

    public void shutdown() {
        try {
            playerService.getTask().terminate();
        } catch (NullPointerException | IllegalStateException ignored) {
//            Silence is golden
        }
    }

    class PlayerService extends TimerService {

        public final String MESSAGE_LOAD_URL = PlayerService.class.getCanonicalName() + ".MESSAGE_LOAD_URL";
        private final long DELAY_DEFAULT = IntervalUtil.parse("00:00:10");
        private long delay = DELAY_DEFAULT;
        private Task task;

        @Override
        protected long getDelay() {
            return delay;
        }

        protected Task getTask() {
            return task;
        }

        @Override
        protected Task getNewTask() {
            task = new Player() {
                @Override
                public void success(Playable response) {
                    Platform.runLater(() -> {
                        try {
                            webView.getEngine().load(response.getURLForPlayer());
//                        webView.getEngine().load("file:///D:/Java/MidiaIndoor/target/classes/assets/contents/000000000000000070/bdoserver2.7/index.html");
//                        webView.getEngine().load("http://www.google.com.br");
                        } catch (Exception ex) {
                            webView.getEngine().load(defaultUrl);
                        }
                    });

                    delay = response.getDurationInterval();
//                    try {
//                        Thread.sleep(response.getDurationInterval());
//                    } catch (InterruptedException ignored) {
////                        Silence is golden
//                    }
                }

                @Override
                public void error(Exception response) {
                    delay = DELAY_DEFAULT;
                    Platform.runLater(() -> {
                        webView.getEngine().load(defaultUrl);
                    });
                }

                @Override
                public void complete() {
                    schedule();
                }
            };
            return task;
        }

    }
}
