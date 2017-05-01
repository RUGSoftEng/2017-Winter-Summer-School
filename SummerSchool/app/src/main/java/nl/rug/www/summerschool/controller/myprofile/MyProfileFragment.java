package nl.rug.www.summerschool.controller.myprofile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

import nl.rug.www.summerschool.R;

/**
 * This class is to show the data fetched from facebook or google+.
 * By using the data from this, the user can access to the forum in the app.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class MyProfileFragment extends Fragment{

    private static final String ARG_CONTENT_ID = "log_in_data";
    private ArrayList<String> mLogInData;

    public static MyProfileFragment newInstance(ArrayList<String> logInData) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CONTENT_ID, logInData);

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogInData = getArguments().getStringArrayList(ARG_CONTENT_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        Button logoutButton = (Button) view.findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                SignInFragment signInFragment = new SignInFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, signInFragment)
                        .commit();
            }
        });

        ProfilePictureView profilePictureView = (ProfilePictureView)view.findViewById(R.id.user_profile_photo);
        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        profilePictureView.setProfileId(mLogInData.get(0));
        profilePictureView.setVisibility(View.VISIBLE);
        TextView myProfileName = (TextView)view.findViewById(R.id.user_profile_name);
        myProfileName.setText(mLogInData.get(1));
        TextView myName = (TextView)view.findViewById(R.id.user_name);
        myName.setText(mLogInData.get(2));
        TextView myEmail = (TextView)view.findViewById(R.id.user_email);
        myEmail.setText(mLogInData.get(3));
        TextView myBirthday = (TextView)view.findViewById(R.id.user_birthday);
        myBirthday.setText(mLogInData.get(4));
//        TextView myGender = (TextView)view.findViewById(R.id.user_gender);
//        myGender.setText(mLogInData.get(5));

        return view;
    }
}
