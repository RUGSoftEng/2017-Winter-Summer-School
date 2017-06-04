package nl.rug.www.rugsummerschool.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import nl.rug.www.rugsummerschool.R;

/**
 * This class is an acitivty started when the application is opened.
 * Only correct code can start main pager activity.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LoginActivity extends AppCompatActivity {

    /** temporary correct code to enter main activity */
    final static private String CORRECT_CODE = "aaaa";
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button)findViewById(R.id.login_button);
        final EditText codeText = (EditText)findViewById(R.id.codeText);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeText.getText().toString();
                if (code.equals(CORRECT_CODE)) {
                    Intent intent = new Intent(LoginActivity.this, MainPagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_correct_code, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
