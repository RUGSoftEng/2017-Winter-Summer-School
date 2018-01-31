package nl.rug.www.rugsummerschools.controller;

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

import com.android.volley.NetworkResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.networking.Networking;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This class is an acitivty started when the application is opened.
 * Only correct code can start main pager activity.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    /** temporary correct code to enter main activity */
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;

    private static final String IS_STORED = "is_stored";
    private static final String CODE = "code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_login);
        mProgressBar.setVisibility(View.GONE); // TODO : Customize progress bar implementation.
        mLoginButton = (Button)findViewById(R.id.login_button);
//        mLoginButton.setEnabled(false);
        mPasswordEditText = (EditText)findViewById(R.id.codeText);
//        mPasswordEditText.setEnabled(false);

        mSharedPreferences = getSharedPreferences("ActivityPreference", Context.MODE_PRIVATE);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String code = mPasswordEditText.getText().toString();
                new Networking(LoginActivity.this)
                        .getJSONObjectRequest(createPaths(), createQueries(code), new Networking.VolleyCallback<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject result) {
                                if (!mSharedPreferences.getBoolean(IS_STORED, false)) {
                                    Log.d(TAG, "code is not stored!");
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putBoolean(IS_STORED, true);
                                    editor.putString(CODE, code);
                                    editor.apply();
                                }
                                Toast.makeText(LoginActivity.this, "Log in Success", Toast.LENGTH_LONG).show();
                                runMainPagerActivity();
                            }

                            @Override
                            public void onError(NetworkResponse result) {
                                String errorString;
                                if (result != null) {
                                    switch (result.statusCode) {
                                        case 400 :
                                            errorString = "Code is not correct! Please try again";
                                            break;
                                            // TODO : implement more error handling code
                                        default:
                                            errorString = "Authentication error! Error code :" + result.statusCode;
                                    }
                                } else {
                                    errorString = "Unexpected error happened";
                                }
                                Toast.makeText(LoginActivity.this, errorString, Toast.LENGTH_LONG).show();
                                if (mSharedPreferences.getBoolean(IS_STORED, true)) {
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.clear();
                                    editor.apply();
                                }
                            }
                        });
            }
        });

        if (mSharedPreferences.getBoolean(IS_STORED, false)) {
            String code = mSharedPreferences.getString(CODE, null);
            Log.d(TAG, "Stored code : " + code);
            mPasswordEditText.setText(code);
            mLoginButton.performClick();
        }

    }

    private List<String> createPaths() {
        List<String> paths = new ArrayList<>();
        paths.add("logincode");
        return paths;
    }

    private Map<String, String> createQueries(String code) {
        Map<String, String> queries = new HashMap<>();
        queries.put("code", code);
        return queries;
    }

    private void runMainPagerActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
