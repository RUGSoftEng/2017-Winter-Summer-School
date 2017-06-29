package nl.rug.www.rugsummerschools.controller.myprofile;

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
import java.util.Calendar;
import java.util.GregorianCalendar;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;

/**
 * This class is to show the data fetched from facebook or google+.
 * By using the data from this, the user can access to the forum in the app.
 *
 * @author Jeongkyun Oh
 * @since 13/04/2017
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MyProfileFragment";
    //UI Object Declaration
    private TextView displayNameTV;
    private TextView schoolnameTV;
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
        SIM = SignInManager.get(getActivity());
        if (!SIM.getmGoogleApiClient().isConnected()) {
            SIM.getmGoogleApiClient().connect();
        }
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

        schoolnameTV = (TextView) view.findViewById(R.id.user_profile_short_bio);
        Calendar today = new GregorianCalendar();
        String shortBio = "Groningen University - ";
        int year = today.get(Calendar.YEAR);
        shortBio += year;
        int month = today.get(Calendar.MONTH);
        if (month >= 4 && month <= 9) {
            shortBio += " Summer School";
        } else {
            shortBio += " Winter School";
        }
        schoolnameTV.setText(shortBio);

        emailTV = (TextView) view.findViewById(R.id.user_email);
        emailTV.setText(email);

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
