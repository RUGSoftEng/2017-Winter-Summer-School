package nl.rug.www.rugsummerschools.controller.forum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This is an Activity class to post or edit a forum thread.
 *
 * @since 05/06/2017
 * @author Jeongkyun Oh
 */

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener, NetworkingService.VolleyCallback {

    public static final int INT_ADD = 0;
    public static final int INT_EDIT = 1;

    public static final String ARG_ADD_OR_EDIT = "add_or_edit";
    public static final String ARG_EDITABLE_DATA = "editable_data";

    private static final String TAG = "ThreadActivity";
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private int mFlag;
    private String[] mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);

        mTitleEditText = (EditText)findViewById(R.id.title_edit_text);
        mContentsEditText = (EditText)findViewById(R.id.contents_edit_text);
        Button postButton = (Button) findViewById(R.id.post_button);
        final Bundle bundle = getIntent().getExtras();
        mFlag = bundle.getInt(ARG_ADD_OR_EDIT);
        if (mFlag == INT_EDIT) {
            mData = bundle.getStringArray(ARG_EDITABLE_DATA);
            mTitleEditText.setText(mData[1]);
            mContentsEditText.setText(mData[2]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_button :
                String title = mTitleEditText.getText().toString();
                String description = mContentsEditText.getText().toString();

                List<String> paths = new ArrayList<>();
                paths.add("forum");
                paths.add("thread");

                if (title.equals("") || description.equals("")) {
                    Toast.makeText(ThreadActivity.this, "Title or description cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("title", title);
                    map.put("description", description);
                    switch (mFlag) {
                        case INT_ADD :
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            map.put("author", user.getDisplayName());
                            map.put("posterID", user.getUid());
                            map.put("imgurl", user.getPhotoUrl().toString());
                            new NetworkingService<>().postPutRequest(this, Request.Method.POST, paths, null, map, this);
                            break;
                        case INT_EDIT :
                            map.put("threadID", mData[0]);
                            new NetworkingService<>().postPutRequest(this, Request.Method.PUT, paths, null, map, this);
                            break;
                    }

                }
                break;
        }
    }

    @Override
    public void onResponse(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if ("OK".equals(result) || "200".equals(result))
            setResult(Activity.RESULT_OK);
        else
            setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onError(NetworkResponse result) {
        Toast.makeText(this, "Error:" + result, Toast.LENGTH_SHORT).show();
    }
}
