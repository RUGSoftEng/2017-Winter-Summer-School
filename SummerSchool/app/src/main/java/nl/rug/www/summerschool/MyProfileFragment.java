package nl.rug.www.summerschool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jk on 4/2/17.
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
                Toast.makeText(getActivity(), "Log out button is pressed", Toast.LENGTH_SHORT).show();
                LoginManager.getInstance().logOut();
            }
        });

        TextView myProfileName = (TextView)view.findViewById(R.id.user_profile_name);
        myProfileName.setText(mLogInData.get(0));
        TextView myName = (TextView)view.findViewById(R.id.user_name);
        myName.setText(mLogInData.get(0));
        TextView myEmail = (TextView)view.findViewById(R.id.user_email);
        myEmail.setText(mLogInData.get(1));
        TextView myBirthday = (TextView)view.findViewById(R.id.user_birthday);
        myBirthday.setText(mLogInData.get(2));
        TextView myGender = (TextView)view.findViewById(R.id.user_gender);
        myGender.setText(mLogInData.get(3));
        ProfilePictureView profilePictureView = (ProfilePictureView)view.findViewById(R.id.user_profile_photo);
        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        profilePictureView.setProfileId(mLogInData.get(4));
        profilePictureView.setVisibility(View.VISIBLE);

        return view;
    }
}
