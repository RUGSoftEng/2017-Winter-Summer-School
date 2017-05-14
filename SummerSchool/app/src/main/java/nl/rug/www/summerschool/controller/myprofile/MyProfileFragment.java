package nl.rug.www.summerschool.controller.myprofile;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.controller.myprofile.SignInManager;

/**
 * This class is to show the data fetched from facebook or google+.
 * By using the data from this, the user can access to the forum in the app.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_CONTENT_ID = "log_in_data";
    private static final String TAG = "MyProfileFragment";
    //UI Object Declaration
    private TextView displayNameTV;
    private TextView nameTV;
    private TextView emailTV;
    private ImageView profilePictureIV;

    private ArrayList<String> mLogInData;
    private Button logout;

    private static SignInManager SIM;

    //creation of bundle, put arguments in it and set it when creating fragment for return.
    public static MyProfileFragment newInstance(ArrayList<String> details) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CONTENT_ID, details);

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve data from bundle
        mLogInData = getArguments().getStringArrayList(ARG_CONTENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

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
        return view;
    }

    @Override
    public void onClick(View v) {
        signOut();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, new SignInFragment()).commit();
    }

    private void signOut() {
        Log.w(TAG,"" + "User Sign out!");
        //if facebook login is permitted
        for (UserInfo user: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if(user.getProviderId().equals("facebook.com")){
                LoginManager.getInstance().logOut();
            }
        }

        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(SIM.getmGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
        revokeAccess();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(SIM.getmGoogleApiClient().isConnected()) {
            SIM.getmGoogleApiClient().stopAutoManage(getActivity());
            SIM.getmGoogleApiClient().disconnect();
        }
    }
}
