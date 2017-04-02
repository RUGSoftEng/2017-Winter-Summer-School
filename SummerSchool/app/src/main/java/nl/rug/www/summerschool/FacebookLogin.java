package nl.rug.www.summerschool;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.Arrays;

/**
 * Created by jk on 4/1/17.
 */

public class FacebookLogin {

    private static FacebookLogin sFacebookLogin;

    private LoginButton invisibleButton;
    private CallbackManager callbackManager;
    private String id;
    private String email;
    private String name;
    private String birthday;
    private String gender;

    public static FacebookLogin get() {
        if (sFacebookLogin == null) {
            sFacebookLogin = new FacebookLogin();
        }
        return sFacebookLogin;
    }

    private FacebookLogin() {

    }

    private void facebookLogin() {
//        invisibleButton = (LoginButton) findViewById(R.id.facebook_invisible_button);
        callbackManager = CallbackManager.Factory.create();
        invisibleButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        // Callback registration
        invisibleButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Main Activity", loginResult.getAccessToken().toString());
            }

            @Override
            public void onCancel() {
                Log.i("Main Activity", "Login cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("Main Activity", exception.getMessage());
            }
        });
    }

    private void retrieveData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Main", response.toString());
                        setProfileToVariables(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setProfileToVariables(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            email = jsonObject.getString("email");
            name = jsonObject.getString("name");
            birthday = jsonObject.getString("birthday");
            gender = jsonObject.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


}
