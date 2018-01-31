package br.com.iandev.midiaindoor.services;

import br.com.iandev.midiaindoor.task.Task;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public abstract class TimerService extends Service {

    private volatile static Map<String, Task> taskMap = new HashMap<>();
    private final Timer timer = new Timer();

    protected abstract Task getNewTask();

    protected abstract long getDelay();

    public synchronized final void start() {
        try {
            stop();
        } catch (IllegalStateException ignored) {
//            Silence is golden
        }
//        Thread.currentThread().setName(String.format("%s -> %s", Thread.currentThread().getName(), this.getClass().getSimpleName()));
        Task task = getNewTask();
        timer.schedule(task, 0);
    }

    protected synchronized final void stop() throws IllegalStateException {
        Task task = taskMap.get(mapKey());
        if (task != null) {
            task.terminate();
            taskMap.remove(mapKey());
        }
    }

    protected synchronized final void schedule() {
        try {
            stop();
            Task task = getNewTask();
            task.getLogger().info("schedule()");
            timer.schedule(task, getDelay());
            taskMap.put(mapKey(), task);
        } catch (IllegalStateException ignored) {
//            Silence is golden
        }

    }

    private String mapKey() {
        return this.getClass().getName();
    }
}
