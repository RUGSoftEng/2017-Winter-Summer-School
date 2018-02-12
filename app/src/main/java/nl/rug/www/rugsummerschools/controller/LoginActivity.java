package nl.rug.www.rugsummerschools.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Pattern;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.LoginInfo;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is an acitivty started when the application is opened.
 * Only correct code can start main pager activity.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    /** temporary correct code to enter main activity */
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private SharedPreferences mSharedPreferences;
    private String mCode;

    private static final String IS_STORED = "is_stored";
    private static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = findViewById(R.id.login_button);
        mPasswordEditText = findViewById(R.id.code_edit_text);

        mSharedPreferences = getSharedPreferences("ActivityPreference", Context.MODE_PRIVATE);

        if (mSharedPreferences.getBoolean(IS_STORED, false)) {
            mCode = mSharedPreferences.getString(CODE, null);
            new FetchLogInCodes().execute();
        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCode = mPasswordEditText.getText().toString();
                if ("".equals(mCode)) {
                    Toast.makeText(LoginActivity.this, R.string.passcode_empty_error, Toast.LENGTH_LONG).show();
                } else if (!Pattern.matches("[a-z0-9]{8}", mCode)) {
                    Toast.makeText(LoginActivity.this, R.string.passcode_restriction, Toast.LENGTH_LONG).show();
                } else {
                    new FetchLogInCodes().execute();
                }
            }
        });
    }

    private void runMainPagerActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class FetchLogInCodes extends AsyncTask<Void, Void, List<LoginInfo>> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
            mLoginButton.setEnabled(false);
            mPasswordEditText.setEnabled(false);
        }

        @Override
        protected List<LoginInfo> doInBackground(Void... params) {
            return new NetworkingService<LoginInfo>().fetchData(NetworkingService.LOGIN_CODE, mCode);
        }

        @Override
        protected void onPostExecute(List<LoginInfo> loginInfos) {
            Log.d(TAG, loginInfos.toString());
            mLoginButton.setEnabled(true);
            mPasswordEditText.setEnabled(true);
            if (loginInfos.size() == 0) {
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor ed = mSharedPreferences.edit();
                ed.clear();
                ed.apply();
            } else {
                ContentsLab.get().setSchoolId(loginInfos.get(0).getSchoolId());
                if (!mSharedPreferences.getBoolean(IS_STORED, false)) {
                    SharedPreferences.Editor ed = mSharedPreferences.edit();
                    ed.putBoolean(IS_STORED, true);
                    ed.putString(CODE, mCode);
                    ed.apply();
                }
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                runMainPagerActivity();
            }
            hideProgressDialog();
        }
    }
}
