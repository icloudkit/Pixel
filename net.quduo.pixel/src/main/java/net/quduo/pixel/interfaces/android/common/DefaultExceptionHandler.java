package net.quduo.pixel.interfaces.android.common;

import android.content.Context;

/**
 * Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this.getApplicationContext()));
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {


    private Context mContext = null;

    public DefaultExceptionHandler(Context context) {
        this.mContext = context;
    }


    @Override

    public void uncaughtException(Thread thread, Throwable ex) {

        // 收集异常信息 并且发送到服务器
        sendCrashReport(ex);

        // 等待半秒
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO:
        }
        // 处理异常
        handleException();
    }


    private void sendCrashReport(Throwable ex) {

        StringBuffer exceptionBuffer = new StringBuffer();
        exceptionBuffer.append(ex.getMessage());

        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionBuffer.append(elements[i].toString());
        }
        // TODO:发送收集到的Crash信息到服务器
    }


    private void handleException() {

        // TODO:对异常进行处理
        // 提示用户程序崩溃了
        // 记录重要的信息，尝试恢复
        // 记录重要的信息后，直接杀死程序
    }
}
