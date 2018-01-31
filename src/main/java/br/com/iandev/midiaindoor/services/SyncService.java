package br.com.iandev.midiaindoor.services;

import br.com.iandev.midiaindoor.model.Device;
import br.com.iandev.midiaindoor.util.IntervalUtil;
import br.com.iandev.midiaindoor.task.Task;
import br.com.iandev.midiaindoor.task.Synchronizer;

public class SyncService extends TimerService {
    private static final long SHORT_TIMEOUT = IntervalUtil.parse("00:00:30");

    private long delay = SHORT_TIMEOUT;

    @Override
    protected long getDelay() {
        return delay;
    }

    @Override
    protected Task getNewTask() {
        return new Synchronizer() {
            @Override
            public void success(Device response) {
                delay = response.getUpdateInterval();
            }

            @Override
            public void error(Exception response) {
                delay = SHORT_TIMEOUT;
            }

            @Override
            public void complete() {
                schedule();
            }
        };
    }
}

