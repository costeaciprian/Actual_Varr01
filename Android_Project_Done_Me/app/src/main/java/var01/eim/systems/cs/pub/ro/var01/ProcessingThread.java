package var01.eim.systems.cs.pub.ro.var01;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Random;

/**
 * Created by ciprian on 3/29/2018.
 */

public class ProcessingThread extends Thread {

    private Context context;
    private String text;
    private boolean isRunning = true;
    private Random random = new Random();

    ProcessingThread(Context context, String text) {
        this.context = context;
        this.text = text;
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException interr) {
            interr.printStackTrace();
        }
    }

    public void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.actions[random.nextInt(Constants.actions.length)]);
        intent.putExtra("message", text);
        context.sendBroadcast(intent);
    }

    @Override
    public void run() {
        super.run();
        while(isRunning) {
            sendMessage();
            sleep();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
