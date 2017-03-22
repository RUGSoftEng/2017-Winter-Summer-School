package layout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.login.widget.ProfilePictureView;

import java.net.URL;

import nl.rug.www.summerschool.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_NAME = "name";
    private static final String ARG_BIRTHDAY = "birthday";
    private static final String ARG_GENDER = "gender";

    // TODO: Rename and change types of parameters
    private String id;
    private String email;
    private String name;
    private String birthday;
    private String gender;
    private TextView myProfileName;
    private TextView myName;
    private TextView myEmail;
    private TextView myBirthday;
    private TextView myGender;
    private ProfilePictureView profilePictureView;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2, String param3, String param4, String param5) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        args.putString(ARG_EMAIL, param2);
        args.putString(ARG_NAME, param3);
        args.putString(ARG_BIRTHDAY, param4);
        args.putString(ARG_GENDER, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            email = getArguments().getString(ARG_EMAIL);
            name = getArguments().getString(ARG_NAME);
            birthday = getArguments().getString(ARG_BIRTHDAY);
            gender = getArguments().getString(ARG_GENDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.v("Profile Fragment", id + email + name + getArguments());
        myProfileName = (TextView)view.findViewById(R.id.user_profile_name);
        myProfileName.setText(name);
        myName = (TextView)view.findViewById(R.id.user_name);
        myName.setText(name);
        myEmail = (TextView)view.findViewById(R.id.user_email);
        myEmail.setText(email);
        myBirthday = (TextView)view.findViewById(R.id.user_birthday);
        myBirthday.setText(birthday);
        myGender = (TextView)view.findViewById(R.id.user_gender);
        myGender.setText(gender);
        profilePictureView = (ProfilePictureView)view.findViewById(R.id.user_profile_photo);
        profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
        profilePictureView.setProfileId(id);
        profilePictureView.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
