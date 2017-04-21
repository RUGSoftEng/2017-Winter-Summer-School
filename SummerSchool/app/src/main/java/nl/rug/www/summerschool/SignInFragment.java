package nl.rug.www.summerschool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is to sign in via Google or Facebook accounts.
 * Currently, Google account does not work.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class SignInFragment extends Fragment {

    private static final String TAG = "SignInFragment";

    private LoginButton mInvisibleButton;
    private CallbackManager mCallbackManager;
    private ArrayList<String> mLogInDataSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mInvisibleButton = (LoginButton)view.findViewById(R.id.facebook_invisible_log_in_button);
        mCallbackManager = CallbackManager.Factory.create();
        mInvisibleButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        mInvisibleButton.setFragment(this);
        mInvisibleButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Log in success");
                retrieveData();

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Log in cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "Log in error");
            }
        });

        Button loginButton = (Button) view.findViewById(R.id.facebook_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInvisibleButton.performClick();
            }
        });

        mLogInDataSet = new ArrayList<>();
        if(isLoggedInFacebook()) retrieveData();
        return view;
    }

    private void retrieveData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v(TAG, response.toString());
                        setProfileToVariables(object);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, MyProfileFragment.newInstance(mLogInDataSet))
                                .commit();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setProfileToVariables(JSONObject jsonObject) {
        try {
            mLogInDataSet.add(jsonObject.getString("id"));
            mLogInDataSet.add(jsonObject.getString("name"));
            mLogInDataSet.add(jsonObject.getString("email"));
            mLogInDataSet.add(jsonObject.getString("gender"));
            mLogInDataSet.add(jsonObject.getString("birthday"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
