package nl.rug.www.summerschool.controller.myprofile;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.glide.transformations.BlurTransformation;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.controller.ContentsLab;

/**
 * This class is for the signup page if an non existing account is logged in
 * Otherwise, SignInFragment is inflated.
 *
 * @author Jeongkyun Oh
 * @since 13/04/2017
 */

public class SignUpFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private static final String TAG = "SignUpFragment";
    private static final String USER_CONTENT_ID = "Sign_Up_Data";
    //Data Variables
    private String month;
    private String FOS;
    private String DOB;
    private int mm;
    //Widget Variables
    private Spinner spinner, month_spinner;
    private Button next;
    private EditText day, year;
    //Utility Variables
    private String mUID;
    protected AppCompatActivity mActivity;
    private ArrayAdapter<CharSequence> adapter;
    private Pattern pattern;
    private Matcher matcher;
    private SignInManager SIM;
    private static final String DATE_PATTERN =
            "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";


    public static SignUpFragment newInstance(String UID) {
        Log.i(TAG, UID);
        Bundle args = new Bundle();
        args.putString(USER_CONTENT_ID, UID);

        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //if app crashes or user quits
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy Called");
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUID = getArguments().getString(USER_CONTENT_ID);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        SIM = SignInManager.get(getActivity());
        //Dropdown list for Month
        spinner = (Spinner) view.findViewById(R.id.signup_month_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Month_Array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Dropdown list for FOS
        spinner = (Spinner) view.findViewById(R.id.signup_fos_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.FOS_Array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Edittext for day/year
        day = (EditText) view.findViewById(R.id.signup_day_text);
        year = (EditText) view.findViewById(R.id.signup_year_text);

        next = (Button) view.findViewById(R.id.signup_continue);
        next.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_continue:
                //validate details
                if (day.getText().toString().matches("") || year.getText().toString().matches("")) {
                    Toast.makeText
                            (getActivity().getApplicationContext(), "Enter Valid Date", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    DOB = day.getText().toString() + "/" + mm + "/" + year.getText().toString();
                    if (!validateDOB(DOB)) {
                        Toast.makeText
                                (getActivity().getApplicationContext(), "Enter Valid Date", Toast.LENGTH_SHORT)
                                .show();

                    } else {
                        //add to database
                        DOB = day.getText().toString() + " " + month + " " + year.getText().toString();
                        ContentsLab.get().addFakeData(DOB);
                        ContentsLab.get().addFakeData(FOS);
                        Log.d(TAG, DOB);
                        ContentsLab.get().addFakeUsers(mUID);
                        changeFragment();
                    }
                    break;
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.signup_fos_spinner:
                FOS = (String) parent.getItemAtPosition(position);
                break;
            case R.id.signup_month_spinner:
                month = (String) parent.getItemAtPosition(position);
                mm = position + 1;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void changeFragment() {
        FragmentManager fm = mActivity.getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();
    }


    private boolean validateDOB(final String date) {
        pattern = Pattern.compile(DATE_PATTERN);
        matcher = pattern.matcher(date);

        int yyyy = Calendar.getInstance().get(Calendar.YEAR);
        int yyyy2 = Integer.parseInt(year.getText().toString());
        if ((yyyy - yyyy2) < 0 || (yyyy - yyyy2) < 16) return false;

        if (matcher.matches()) {

            matcher.reset();

            if (matcher.find()) {

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
