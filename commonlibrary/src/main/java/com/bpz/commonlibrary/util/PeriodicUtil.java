package com.bpz.commonlibrary.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 执行定时任务
 */

public class PeriodicUtil {

    private final Task task;
    private final int period;
    private TimerTask progressUpdateTask;
    private boolean isSchedule = false;

    /**
     * 执行周期性周期任务
     *
     * @param task   执行内容
     * @param period 周期
     */
    public PeriodicUtil(Task task, int period) {
        this.task = task;
        this.period = period;
    }

    public void stop() {
        if (!isSchedule()) {
            return;
        }

        if (progressUpdateTask != null) {
            progressUpdateTask.cancel();
            isSchedule = false;
        }
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public void start() {
        if (isSchedule()) {
            return;
        }

        Timer timer = new Timer();
        progressUpdateTask = new TimerTask() {
            @Override
            public void run() {
                task.execute();
            }
        };
        timer.schedule(progressUpdateTask, period, period);
        isSchedule = true;
    }

    public interface Task {
        /**
         * 执行周期任务，注意该方法不在主线程执行，不能在其中访问 UI 控件
         */
        void execute();
    }

}
