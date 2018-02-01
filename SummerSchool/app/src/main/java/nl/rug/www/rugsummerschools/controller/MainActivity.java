package nl.rug.www.rugsummerschools.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.announcement.AnnouncementListFragment;
import nl.rug.www.rugsummerschools.controller.forum.ForumLoginFragment;
import nl.rug.www.rugsummerschools.controller.forum.ForumThreadListFragment;
import nl.rug.www.rugsummerschools.controller.generalinfo.GeneralInfoListFragment;
import nl.rug.www.rugsummerschools.controller.lecturer.LecturerListFragment;
import nl.rug.www.rugsummerschools.controller.timetable.TimeTableFragment;
import nl.rug.www.rugsummerschools.controller.timetable.TimeTableFragment2;

/**
 * This class is main activity that contains basic layout of the app.
 * It consists of header, viewpager, and buttons.
 * Fragments (Announcements, Generalninfos, ...) will be inflated in view pager.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class MainActivity extends AppCompatActivity implements ForumLoginFragment.OnSignInListener, ForumThreadListFragment.OnSignOutListener {

    private static final String TAG = "MainActivity";
    private static final int PAGE_ACCOUNCEMENT = 0;
    private static final int PAGE_GENERAL_INFO = 1;
    private static final int PAGE_LECTURER = 2;
    private static final int PAGE_TIME_TABLE = 3;
    private static final int PAGE_FORUM = 4;
    private static final int FRAGMENTS_SIZE = 5;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private LoginButton mInvisibleFacebookLoginButton;
    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private ViewPager mViewPager;
    private BottomNavigationView mNavigation;

    private Fragment[] mFragments = {
            new AnnouncementListFragment(),
            new GeneralInfoListFragment(),
            new LecturerListFragment(),
            new TimeTableFragment2(),
            new ForumLoginFragment(),
            new ForumThreadListFragment()
    };

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            if (position == PAGE_FORUM) {
                if (mAuth.getCurrentUser() == null) {
                    return new ForumLoginFragment(); // TODO : is this proper to inflate menu options?
                } else {
                    return new ForumThreadListFragment(); // TODO : is this proper to inflate menu options?
                }
            }
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return FRAGMENTS_SIZE;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            // TODO : is this proper solution for transaction replace fragments in view pager activity?
            if (object instanceof ForumLoginFragment || object instanceof ForumThreadListFragment) {
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_announcement:
                    mViewPager.setCurrentItem(PAGE_ACCOUNCEMENT);
                    return true;
                case R.id.navigation_general:
                    mViewPager.setCurrentItem(PAGE_GENERAL_INFO);
                    return true;
                case R.id.navigation_lecturer:
                    mViewPager.setCurrentItem(PAGE_LECTURER);
                    return true;
                case R.id.navigation_time_table:
                    mViewPager.setCurrentItem(PAGE_TIME_TABLE);
                    return true;
                case R.id.navigation_forum:
                    mViewPager.setCurrentItem(PAGE_FORUM);
                    return true;
            }
            return false;
        }
    };

    private ViewPager.SimpleOnPageChangeListener mSimpleOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case PAGE_ACCOUNCEMENT :
                    mNavigation.setSelectedItemId(R.id.navigation_announcement);
                    break;
                case PAGE_GENERAL_INFO :
                    mNavigation.setSelectedItemId(R.id.navigation_general);
                    break;
                case PAGE_LECTURER :
                    mNavigation.setSelectedItemId(R.id.navigation_lecturer);
                    break;
                case PAGE_TIME_TABLE :
                    mNavigation.setSelectedItemId(R.id.navigation_time_table);
                    break;
                case PAGE_FORUM :
                    mNavigation.setSelectedItemId(R.id.navigation_forum);
                    break;
            }
            invalidateOptionsMenu();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftingNavigationMode();

        initFbLogin();
        initGoogleLogin();

        mAuth = FirebaseAuth.getInstance();

        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mViewPager.addOnPageChangeListener(mSimpleOnPageChangeListener);
        mViewPager.setAdapter(mFragmentStatePagerAdapter);
    }

    private void initFbLogin() {
        mCallbackManager = CallbackManager.Factory.create();
        mInvisibleFacebookLoginButton = findViewById(R.id.invisible_facebook_button);
        mInvisibleFacebookLoginButton.setReadPermissions("email", "public_profile");
        mInvisibleFacebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftingNavigationMode() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mNavigation.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for(int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        mFragmentStatePagerAdapter.notifyDataSetChanged();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });

    }

    @Override
    public void signInWithGoogle() {
        signIn();
    }

    @Override
    public void signInWithFacebook() {
        mInvisibleFacebookLoginButton.performClick();
    }

    @Override
    public void signOutGoogle() {
        revokeAccess();
        LoginManager.getInstance().logOut();
    }
}
