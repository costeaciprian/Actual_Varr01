package var01.eim.systems.cs.pub.ro.var01;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;

public class Var01MainActivity extends AppCompatActivity {

    private Button east_but, navigate_but, west_but, south_but, north_but,
            reinitialize_but, bounded_service_button;
    private TextView out_text, bounded_service_text;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private int num_clicks;
    private boolean isServiceStarted = false;
    private boolean isBoundedServiceStarted = false;
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private IntentFilter intentFilter = null;
    private MyBoundedService myBoundedService = null;

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "[BROADCAST RECEIVER]: " +
                    intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();

            Log.d("[BROADCAST RECEIVER]", intent.getStringExtra("message"));
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.east_button:
                    num_clicks++;
                    out_text.setText(Constants.east + out_text.getText().toString());
                    break;
                case R.id.west_button:
                    out_text.setText(Constants.west + out_text.getText().toString());
                    num_clicks++;
                    break;
                case R.id.south_button:
                    out_text.setText(Constants.south + out_text.getText().toString());
                    num_clicks++;
                    break;
                case R.id.north_button:
                    out_text.setText(Constants.north + out_text.getText().toString());
                    num_clicks++;
                    break;
                case R.id.navigate_button:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var01SecondaryActivity.class);
                    intent.putExtra("num_clicks", num_clicks);
                    intent.putExtra("text_coord", out_text.getText().toString());
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                    break;
                case R.id.reinitialize_second_button:
                    out_text.setText("");
                    num_clicks = 0;
                    Intent intent_vid = new Intent(getApplicationContext(), PracticalTest01Var01SecondaryActivity.class);
                    intent_vid.putExtra("num_clicks", 0);
                    intent_vid.putExtra("text_coord", "");
                    startActivityForResult(intent_vid, Constants.REQUEST_CODE);
                    break;
                case R.id.message_from_bounded_service_button:
                    if(myBoundedService != null && isBoundedServiceStarted) {
                        bounded_service_text.setText("[" + new Timestamp(System.currentTimeMillis()) + "]"
                                + myBoundedService.getMessage() + "\n" + bounded_service_text.getText().toString());
                    }
                    break;
            }
            verifyStartOfService();
        }
    }

    public void verifyStartOfService() {
        if( !isServiceStarted && num_clicks > Constants.THRESHOLD) {
            isServiceStarted = true;

            Intent intent = new Intent(getApplicationContext(), PracticalTest01Var01Service.class);
            intent.putExtra("text_coords", out_text.getText().toString());
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isServiceStarted = false;
        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var01Service.class);
        stopService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBoundedService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( isBoundedServiceStarted) {
            isBoundedServiceStarted = false;
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_var01_main);

        out_text = findViewById(R.id.out_text);
        navigate_but = findViewById(R.id.navigate_button);
        east_but = findViewById(R.id.east_button);
        west_but = findViewById(R.id.west_button);
        south_but = findViewById(R.id.south_button);
        north_but = findViewById(R.id.north_button);
        reinitialize_but = findViewById(R.id.reinitialize_second_button);
        bounded_service_button = findViewById(R.id.message_from_bounded_service_button);
        bounded_service_text = findViewById(R.id.text_bounded_service);

        east_but.setOnClickListener(buttonClickListener);
        west_but.setOnClickListener(buttonClickListener);
        north_but.setOnClickListener(buttonClickListener);
        south_but.setOnClickListener(buttonClickListener);
        navigate_but.setOnClickListener(buttonClickListener);
        reinitialize_but.setOnClickListener(buttonClickListener);
        bounded_service_button.setOnClickListener(buttonClickListener);

        if (savedInstanceState == null) {
            num_clicks = 0;
            out_text.setText("");
        }
        else if(savedInstanceState.containsKey("num_clicks")) {
            num_clicks = savedInstanceState.getInt("num_clicks");
            out_text.setText(savedInstanceState.getString("text"));
        }

        intentFilter = new IntentFilter();
        for(int i = 0;  i < Constants.actions.length; i++) {
            intentFilter.addAction(Constants.actions[i]);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("[Save Instance]", "onSave Instance CALLED!");
        outState.putInt("num_clicks", num_clicks);
        outState.putString("text", out_text.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("[Restore Instance]", "onRestore CALLED!");
        if(savedInstanceState == null) {
            num_clicks = 0;
            out_text.setText("");
        }
        else if(savedInstanceState.containsKey("num_clicks")) {
            num_clicks = savedInstanceState.getInt("num_clicks");
            out_text.setText(savedInstanceState.getString("text"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE) {
            Toast.makeText(getApplicationContext(), "The secondary activity returned with" +
                    "result: " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            isBoundedServiceStarted = true;
            Log.d("ServiceConnected", "Called");
            MyBoundedService.MyBoundedServiceBinder binder = (MyBoundedService.MyBoundedServiceBinder)service;

            myBoundedService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBoundedServiceStarted = false;
            myBoundedService = null;
            Log.d("ServiceDisconnected", "Called");
        }
    };
}
