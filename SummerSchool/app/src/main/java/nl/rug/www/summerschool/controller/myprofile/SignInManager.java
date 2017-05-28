package nl.rug.www.summerschool.controller.myprofile;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import nl.rug.www.summerschool.R;

/**
 * This class is a singleton class to ensure only one googleSignInManager exist throughout life of the app.
 * Created by Chin Tian Boon on 7/5/2017.
 */

public class SignInManager {
    private static SignInManager sGoogleSignInManager;
    private Context context;
    private static GoogleSignInOptions gso;
    private static GoogleApiClient mGoogleApiClient;

    public static SignInManager get(Context context){
        if (sGoogleSignInManager == null){
            sGoogleSignInManager = new SignInManager(context);
        }
        return sGoogleSignInManager;
    }

    private SignInManager(Context context){
        this.context = context;
        initGoogleSignInManager();
    }

    private void initGoogleSignInManager(){
        if(getmGoogleApiClient() == null) {
            setGso(new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build());
        }
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public static void setGso(GoogleSignInOptions gso) {
        SignInManager.gso = gso;
    }

    public static void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        SignInManager.mGoogleApiClient = mGoogleApiClient;
    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

}
