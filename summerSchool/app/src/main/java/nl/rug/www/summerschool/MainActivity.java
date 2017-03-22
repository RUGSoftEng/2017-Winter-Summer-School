package nl.rug.www.summerschool;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.SignInButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import layout.AnnouncementFragment;
import layout.ForumFragment;
import layout.GeneralInformationFragment;
import layout.ProfileFragment;
import layout.SchoolInformationFragment;
import layout.TimeTableFragment;

public class MainActivity extends AppCompatActivity {

    final static private String WELCOME_EXAMPLE = "Welcome to the University of Groningen Summer School on XXX.\n\n" +
            "The University of Groningen (UoG) has a rich academic tradition dating back to 1614. From this tradition arose the first female student and the first female lecturer in the Netherlands, the first Dutch astronaut, the first president of the European Central Bank and, in 2016, a Nobel prize winner in Chemistry.\n\n" +
            "The UoG is a leading research institution and is among the world’s top 100 universities. Although highly research-driven and innovative, UoG acknowledges its heritage and has strong links to the northern Netherlands region. UoG aims to conduct research of high societal relevance and strongly connects with societal stakeholders. It differentiates itself in the international market by having a close link between education and research and by focusing on three key themes: Energy, Healthy Ageing, and Sustainable Society.\n\n" +
            "The summer school is an initiative of the Faculty of XXX , and it has been developed in conjunction with sponsors, partners.\n\n" +
            "The summer school is presented under the auspices of XXX (GSG/master track / minor …. Please elaborate and include relevant links).\n";

    private LoginButton invisibleButton;
    private CallbackManager callbackManager;
    private String id;
    private String email;
    private String name;
    private String birthday;
    private String gender;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton announcementButton = (ImageButton) findViewById(R.id.announcementButton);
        final ImageButton generalinformationButton = (ImageButton) findViewById(R.id.generalinformationButton);
        final ImageButton schoolinformationButton = (ImageButton) findViewById(R.id.schoolinformationButton);
        final ImageButton timetableButton = (ImageButton) findViewById(R.id.timetableButton);
        final ImageButton forumButton = (ImageButton) findViewById(R.id.forumButton);
        final ImageButton profileButton = (ImageButton) findViewById(R.id.profileButton);
        final ScrollView mainFragment = (ScrollView) findViewById(R.id.default_mainFragment);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/rugfont.ttf");
        TextView mainWelcomeText = (TextView) findViewById(R.id.main_welcome);
        mainWelcomeText.setTypeface(typeface, Typeface.BOLD);
        TextView mainContentsText = (TextView) findViewById(R.id.main_contents);
        mainContentsText.setTypeface(typeface, Typeface.BOLD);
        mainContentsText.setText(WELCOME_EXAMPLE);

        ImageView poster = (ImageView) findViewById(R.id.poster);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.x * 1023 / 745;

        poster.getLayoutParams().width = width;
        poster.getLayoutParams().height = height;

        if(isLoggedIn()) {
            retrieveData();
        }

        announcementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new AnnouncementFragment()).commit();
            }
        });

        generalinformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new GeneralInformationFragment()).commit();
            }
        });

        schoolinformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new SchoolInformationFragment()).commit();
            }
        });

        timetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new TimeTableFragment()).commit();
            }
        });


        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                if (isLoggedIn()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new ForumFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new SignInFragment()).commit();
                }
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);
                if (isLoggedIn()) {
                    ProfileFragment pf = ProfileFragment.newInstance(id, email, name, birthday, gender);
                    Log.v("Main Activity", id + email + name + birthday + gender);
                    getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, pf).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new SignInFragment()).commit();
                }
            }
        });

        facebookLogin();
    }

    private void facebookLogin() {
        invisibleButton = (LoginButton) findViewById(R.id.facebook_invisible_button);
        callbackManager = CallbackManager.Factory.create();
        invisibleButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        // Callback registration
        invisibleButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Main Activity", loginResult.getAccessToken().toString());
                retrieveData();
                getSupportFragmentManager().beginTransaction().replace(R.id.next_mainFragment, new AnnouncementFragment()).commit();
                Toast.makeText(MainActivity.this.getApplicationContext(), "Login Succeeded", Toast.LENGTH_SHORT).show();
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
                        setProfileToView(object);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setProfileToView(JSONObject jsonObject) {
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

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
