package nl.rug.www.summerschool.controller.myprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.controller.myprofile.SignInManager;

/**
 * This class is to sign in via Google or Facebook accounts.
 * Currently, Google account does not work.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class SignInFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "LoginFragment";

    private ArrayList<String> mlogInData;
    ContentsLab mContents;
    //FireBase Variables
    private FirebaseAuth mAuth;
    //googlesignin variables
    private Button mgoogleLoginButton;
    private SignInManager SIM;
    //facebooksignin variables
    private LoginButton invisibleFbButton;
    private Button mfacebookLoginButton;
    private CallbackManager mCallbackManager;

    protected AppCompatActivity mActivity;

    private static final int GOOGLE_SIGN_IN = 9099;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false); // inflate new layout for fragment

        //initialise
        invisibleFbButton = (LoginButton) view.findViewById(R.id.invisibleFB);
        mfacebookLoginButton = (Button) view.findViewById(R.id.facebook_login_button);
        mfacebookLoginButton.setOnClickListener(this);
        mgoogleLoginButton = (Button) view.findViewById(R.id.google_login_button);
        mgoogleLoginButton.setOnClickListener(this);
        mlogInData = new ArrayList<String>();
        //instantiate SignInManager for GSO and googleAPI
        initFirebaseService();
        initGoogleLogInService();
        initFacebookLogInService();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //onstart check if google account has already been signed in before
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Log.w(TAG, "On Start of this fragment was called ");
            setMlogInData(currentUser);
        }
    }
    public void onDestroy() {
        super.onDestroy();
        if(SIM.getmGoogleApiClient().isConnected()) {
            SIM.getmGoogleApiClient().stopAutoManage(getActivity());
            SIM.getmGoogleApiClient().disconnect();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                signInGoogle();
                break;
            case R.id.facebook_login_button:
                invisibleFbButton.performClick();
                break;
        }
    }


    //retrieve data from account
    public void setMlogInData(FirebaseUser user) {
        Log.i(TAG, "--------------------------------");
        Log.i(TAG, "Display Name: " + user.getDisplayName());
        Log.i(TAG, "Email: " + user.getEmail());
        Log.i(TAG, "PhotoURL: " + user.getPhotoUrl());
        Log.i(TAG, "UniqueID: " + user.getUid());

        mlogInData.add(user.getPhotoUrl().toString());
        mlogInData.add(user.getDisplayName());
        mlogInData.add(user.getEmail());

        mContents = ContentsLab.get();
        mContents.setmLogInData(mlogInData);

        FragmentManager fm = mActivity.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, MyProfileFragment.newInstance(mlogInData)).commit();
    }
    //INIT FIREBASE SERVICE
    private void initFirebaseService(){
        mAuth = FirebaseAuth.getInstance();
        //set statelistener to check if an account already exists
    }
    //INIT GOOGLE SERVICES
    private void initGoogleLogInService() {
        SIM = SignInManager.get(mActivity);
        SIM.setmGoogleApiClient(new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, SIM.getGso()).build());
    }

    //create intent to start Google API authentication, Result will be returned in onActivityResult.
    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(SIM.getmGoogleApiClient());
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
        //for facebook
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setMlogInData(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //END OF GOOGLE SERVICES

    //INIT FACEBOOK SERVICES
    private void initFacebookLogInService() {
        mCallbackManager = CallbackManager.Factory.create();
        invisibleFbButton.setFragment(this);
        invisibleFbButton.setReadPermissions("email", "public_profile");
        invisibleFbButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setMlogInData(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
    //end of facebook methods
}
