package nl.rug.www.rugsummerschool.controller.myprofile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.controller.ContentsLab;
import nl.rug.www.rugsummerschool.networking.NetworkingService;

/**
 * This class is to show the data fetched from facebook or google+.
 * By using the data from this, the user can access to the forum in the app.
 *
 * @author Jeongkyun Oh
 * @since 13/04/2017
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_CONTENT_ID = "log_in_data";
    private static final String TAG = "MyProfileFragment";
    //UI Object Declaration
    private TextView displayNameTV;
    private TextView nameTV;
    private TextView emailTV;
    private TextView fosTV;
    private TextView dobTV;
    private ImageView profilePictureIV;

    private ArrayList<String> mLogInData;
    private Button logout;

    private static SignInManager SIM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mLogInData = ContentsLab.get().getmLogInData();
        //test data, should retreive from database
        String displayName = mLogInData.get(1);
        String email = mLogInData.get(2);

        logout = (Button) view.findViewById(R.id.log_out_button);
        logout.setOnClickListener(this);

        profilePictureIV = (ImageView) view.findViewById(R.id.user_profile_photo);
        Glide.with(getActivity()).load(mLogInData.get(0)).into(profilePictureIV);

        displayNameTV = (TextView) view.findViewById(R.id.user_profile_name);
        displayNameTV.setText(displayName);

        nameTV = (TextView) view.findViewById(R.id.user_name);
        nameTV.setText(displayName);

        emailTV = (TextView) view.findViewById(R.id.user_email);
        emailTV.setText(email);

        SIM = SignInManager.get(getActivity());
        if (!SIM.getmGoogleApiClient().isConnected()) {
            SIM.getmGoogleApiClient().connect();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        signOut();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, new SignInFragment()).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void signOut() {
        //if facebook login is permitted
        for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("facebook.com")) {
                LoginManager.getInstance().logOut();
            }
            if (user.getProviderId().equals("google.com")) {
                Log.w(TAG, "" + "User Sign out!");

                Auth.GoogleSignInApi.signOut(SIM.getmGoogleApiClient()).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                            }
                        });
                revokeAccess();
            }
        }
        if (SIM.getmGoogleApiClient().isConnected()) {
            SIM.getmGoogleApiClient().stopAutoManage(getActivity());
            SIM.getmGoogleApiClient().disconnect();
            SIM.setmGoogleApiClient(null);
        }
        FirebaseAuth.getInstance().signOut();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(SIM.getmGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }

}
