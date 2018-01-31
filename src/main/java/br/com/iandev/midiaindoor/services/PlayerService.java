package br.com.iandev.midiaindoor.services;

import br.com.iandev.midiaindoor.core.interfaces.Playable;
import br.com.iandev.midiaindoor.util.IntervalUtil;
import br.com.iandev.midiaindoor.task.Player;
import br.com.iandev.midiaindoor.task.Task;

public class PlayerService extends TimerService {
    public static final String MESSAGE_LOAD_URL = PlayerService.class.getCanonicalName() + ".MESSAGE_LOAD_URL";
    private static final long DELAY_DEFAULT = IntervalUtil.parse("00:00:10");
    private long delay = DELAY_DEFAULT;
 
    @Override
    protected long getDelay() {
        return delay;
    }

    @Override
    protected Task getNewTask() {
        return new Player() {
            @Override
            public void success(Playable response) {
                delay = response.getDurationInterval();
            }

            @Override
            public void error(Exception response) {
                delay = DELAY_DEFAULT;
            }

            @Override
            public void complete() {
                schedule();
            }
        };
    }

}