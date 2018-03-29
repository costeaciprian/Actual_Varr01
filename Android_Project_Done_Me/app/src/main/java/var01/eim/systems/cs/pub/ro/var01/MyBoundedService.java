package var01.eim.systems.cs.pub.ro.var01;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;

/**
 * Created by ciprian on 3/29/2018.
 */

public class MyBoundedService extends Service {

    private final IBinder myBoundedServiceBinder = new MyBoundedServiceBinder();
    private Random rand = new Random();

    public class MyBoundedServiceBinder extends Binder {

        public MyBoundedService getService() {
            return MyBoundedService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBoundedServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getMessage() {
        return Constants.Messages[rand.nextInt(Constants.Messages.length)];
    }
}
