package br.com.iandev.midiaindoor.task;

import java.util.TimerTask;
import org.apache.log4j.Logger;

public abstract class Task<T> extends TimerTask {

    private volatile boolean running = true;

    public abstract Logger getLogger();

    @Override
    public final void run() {
        getLogger().info("run()");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    before();
                    T response = execute();
                    if (running) {
                        onSuccess(response);
                    }
                } catch (Exception ex) {
                    if (running) {
                        onError(ex);
                    }
                }
            }
        };

        Thread.currentThread().setName(String.format("%s -> %s", Thread.currentThread().getName(), this.getClass().getName()));
        thread.setName(this.getClass().getName());
        thread.start();
    }

    public final void onSuccess(T response) {
        getLogger().info(String.format("success(%s)", response.toString()));
        try {
            if (running) {
                success(response);
            }
        } catch (Exception ex) {
            if (running) {
                onError(ex);
            }
        } finally {
            if (running) {
                complete();
            }
        }
    }

    public final void onError(Exception response) {
        getLogger().error("", response);
        try {
            if (running) {
                error(response);
            }
        } catch (Exception ignored) {
        }
        if (running) {
            complete();
        }
    }

    public final void terminate() throws IllegalStateException {
        getLogger().info("terminate()");
        if (!running) {
            throw new IllegalStateException("Task have been stopped already");
        }
        running = false;
    }

    public T execute() throws Exception {
        return null;
    }

    public void before() {
    }

    public void success(T response) {
    }

    public void error(Exception response) {
    }

    public void complete() {
    }

}
