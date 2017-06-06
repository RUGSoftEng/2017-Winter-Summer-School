package nl.rug.www.rugsummerschool.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.networking.FCMService;
import nl.rug.www.rugsummerschool.networking.NetworkingService;

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
    private List<String> mLogInCodes;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_login);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setEnabled(false);
        final EditText codeText = (EditText)findViewById(R.id.codeText);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeText.getText().toString();
                if (ContentsLab.get().checkLogInCode(code)) {
                    Intent intent = new Intent(LoginActivity.this, MainPagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_correct_code, Toast.LENGTH_SHORT).show();
                }
            }
        });

        new FetchLogInCodes().execute();

    }

    private class FetchLogInCodes extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            return new NetworkingService().fetchLoginCodes();
        }

        @Override
        protected void onPostExecute(List<String> logInCodes) {
            ContentsLab.get().updateLogInCodes(logInCodes);
            mProgressBar.setVisibility(View.GONE);
            mLoginButton.setEnabled(true);
        }
    }
}
