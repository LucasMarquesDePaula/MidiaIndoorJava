package br.com.iandev.midiaindoor.services;

import br.com.iandev.midiaindoor.util.IntervalUtil;
import br.com.iandev.midiaindoor.task.Licensor;
import br.com.iandev.midiaindoor.task.Task;
import br.com.iandev.midiaindoor.settings.LicenseSettings;

public class LicenseService extends TimerService {
    private static final long SHORT_TIMEOUT = IntervalUtil.parse("00:01:00");
    private static final long LONG_TIMEOUT = IntervalUtil.parse("24:00:00");

    private long delay = SHORT_TIMEOUT;

    @Override
    protected long getDelay() {
        return delay;
    }

    @Override
    protected Task getNewTask() {
        return new Licensor() {
            @Override
            public void success(LicenseSettings response) {
                delay = LONG_TIMEOUT;
            }

            @Override
            public void error(Exception response) {
                delay = SHORT_TIMEOUT;
            }

            @Override
            public void complete() {
//                schedule();
            }
        };
    }
}
