package nl.rug.www.rugsummerschool.networking;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by RavenSP on 1/6/2017.
 */

public class FCMIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCMIDService";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        new NetworkingService().postRequestFCMID(this, refreshedToken);
    }
}
