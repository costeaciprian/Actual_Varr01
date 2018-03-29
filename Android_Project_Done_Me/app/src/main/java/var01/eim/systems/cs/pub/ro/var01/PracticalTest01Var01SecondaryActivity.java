package var01.eim.systems.cs.pub.ro.var01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticalTest01Var01SecondaryActivity extends AppCompatActivity {

    private Button register, cancel;
    private TextView result_text, result_number;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.register_button:
                    setResult(RESULT_OK, null);
                    finish();
                    break;
                case R.id.cancel_button:
                    setResult(RESULT_CANCELED, null);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var01_secondary);

        register = findViewById(R.id.register_button);
        cancel = findViewById(R.id.cancel_button);
        result_number = findViewById(R.id.number_result);
        result_text = findViewById(R.id.result_text);

        register.setOnClickListener(buttonClickListener);
        cancel.setOnClickListener(buttonClickListener);

        Intent intent = getIntent();
        int num_Clicks = intent.getIntExtra("num_clicks", -1);
        String text = intent.getStringExtra("text_coord");

        result_text.setText(text);
        result_number.setText(String.valueOf(num_Clicks));
    }
}
