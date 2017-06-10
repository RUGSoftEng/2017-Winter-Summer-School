package nl.rug.www.rugsummerschool.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import nl.rug.www.rugsummerschool.R;
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
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;

    private static final String TAG = "LoginActivity";

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
                    Log.d(TAG, "In Button lisetner true/false and passcode before : " + mSharedPreferences.getBoolean("password_correct", false) + mSharedPreferences.getString("password", null));
                    if (!mSharedPreferences.getBoolean("password_correct", false)) {
                        SharedPreferences.Editor ed = mSharedPreferences.edit();
                        ed.putBoolean("password_correct", true);
                        ed.putString("password", code);
                        ed.apply();
                        Log.d(TAG, "In Button lisetner true/false and passcode after : " + mSharedPreferences.getBoolean("password_correct", false) + mSharedPreferences.getString("password", null));
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
            Log.d(TAG, "In PostExcute true/false and passcode Before : " + mSharedPreferences.getBoolean("password_correct", false) + mSharedPreferences.getString("password", null));
            if (mSharedPreferences.getBoolean("password_correct", true)) {
                String code = mSharedPreferences.getString("password", null);
                if (ContentsLab.get().checkLogInCode(code)) {
                    runMainPagerActivity();
                } else {
                    SharedPreferences.Editor ed = mSharedPreferences.edit();
                    ed.putBoolean("password_correct", false);
                    ed.apply();
                }
                Log.d(TAG, "In PostExcute true/false and passcode After : " + mSharedPreferences.getBoolean("password_correct", false) + mSharedPreferences.getString("password", null));
            }
            mProgressBar.setVisibility(View.GONE);
            mLoginButton.setEnabled(true);
            mPasswordEditText.setEnabled(true);
        }
    }
}
