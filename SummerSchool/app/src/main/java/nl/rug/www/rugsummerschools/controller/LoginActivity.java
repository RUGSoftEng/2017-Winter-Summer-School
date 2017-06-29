package nl.rug.www.rugsummerschools.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is an acitivty started when the application is opened.
 * Only correct code can start main pager activity.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LoginActivity extends AppCompatActivity {

    /** temporary correct code to enter main activity */
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;

    private static final String CORRECT = "correct";
    private static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_login);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mLoginButton.setEnabled(false);
        mPasswordEditText = (EditText)findViewById(R.id.codeText);
        mPasswordEditText.setEnabled(false);

        new FetchLogInCodes().execute();

        mSharedPreferences = getSharedPreferences("ActivityPreference", Context.MODE_PRIVATE);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mPasswordEditText.getText().toString();
                if (ContentsLab.get().checkLogInCode(code)) {
                    if (!mSharedPreferences.getBoolean(CORRECT, false)) {
                        SharedPreferences.Editor ed = mSharedPreferences.edit();
                        ed.putBoolean(CORRECT, true);
                        ed.putString(CODE, code);
                        ed.apply();
                    }
                    runMainPagerActivity();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.not_correct_code, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void runMainPagerActivity() {
        Intent intent = new Intent(LoginActivity.this, MainPagerActivity.class);
        startActivity(intent);
        finish();
    }

    private class FetchLogInCodes extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... params) {
            return new NetworkingService().fetchLoginCodes();
        }

        @Override
        protected void onPostExecute(List<String> logInCodes) {
            ContentsLab.get().updateLogInCodes(logInCodes);
            if (mSharedPreferences.getBoolean(CORRECT, true)) {
                String code = mSharedPreferences.getString(CODE, null);
                if (ContentsLab.get().checkLogInCode(code)) {
                    runMainPagerActivity();
                } else {
                    SharedPreferences.Editor ed = mSharedPreferences.edit();
                    ed.putBoolean(CORRECT, false);
                    ed.apply();
                }
            }
            mProgressBar.setVisibility(View.GONE);
            mLoginButton.setEnabled(true);
            mPasswordEditText.setEnabled(true);
        }
    }
}
