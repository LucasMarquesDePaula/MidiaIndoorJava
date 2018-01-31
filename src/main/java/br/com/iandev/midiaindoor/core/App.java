package br.com.iandev.midiaindoor.core;

import br.com.iandev.midiaindoor.services.LicenseService;
import br.com.iandev.midiaindoor.services.ManagementService;
import br.com.iandev.midiaindoor.services.SyncService;

public class App {

    public static void initialize() {
        App app = App.getInstance();
        new LicenseService().start();
        new ManagementService().start();
        new SyncService().start();
    }

    private TimeCounter timeCounter;

    private App() {
        timeCounter = new TimeCounter();
    }

    public static App getInstance() {
        return AppHolder.INSTANCE;
    }

    private static class AppHolder {

        private static final App INSTANCE = new App();
    }

    public TimeCounter getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(TimeCounter timeCounter) {
        this.timeCounter = timeCounter;
    }
}
