package br.com.iandev.midiaindoor.services;

import br.com.iandev.midiaindoor.util.IntervalUtil;
import br.com.iandev.midiaindoor.task.Manager;
import br.com.iandev.midiaindoor.task.Task;

public class ManagementService extends TimerService {

    private final long delay = IntervalUtil.parse("00:01:00");

    @Override
    protected long getDelay() {
        return delay;
    }

    @Override
    protected Task getNewTask() {
        return new Manager() {
            @Override
            public void complete() {
                schedule();
            }
        };
    }

}
