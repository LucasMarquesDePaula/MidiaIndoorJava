package br.com.iandev.midiaindoor.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AutoRefreshController extends Controller {

    protected abstract void refresh();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, getPeriod());
    }

    protected long getPeriod() {
        return 10000L;
    }
}
