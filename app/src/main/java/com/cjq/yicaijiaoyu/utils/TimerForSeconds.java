package com.cjq.yicaijiaoyu.utils;

/**
 * Created by CJQ on 2015/5/7.
 */
public class TimerForSeconds extends Thread {
    private int milliseconds;
    private int timeLeft;
    private TimerListener listener;

    public TimerForSeconds(int milliseconds, int timeLeft, TimerListener listener) {
        this.milliseconds = milliseconds;
        /**
         * 每秒调用
         */
        this.timeLeft = timeLeft;
        /**
         * 计时完毕再调用
         */
        this.listener = listener;
    }

    public interface TimerListener {
        void onEverySeconds(int timeLeft);

        void onTimeUp();

        /**
         * 用于返回一个标识，中止计时器的循环
         * @return false中止循环计时
         */
        boolean getTimerFlag();
        boolean getTimerPauseFlag();
    }

    @Override
    public void run() {
        while((timeLeft>=1|| timeLeft<0) && listener.getTimerFlag()){
            try {
                sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }if(!listener.getTimerPauseFlag()){
                if(timeLeft>0)
                    listener.onEverySeconds(--timeLeft);
                else if(timeLeft<0)
                    listener.onEverySeconds(0);
            }
        }
        listener.onTimeUp();
    }
}
