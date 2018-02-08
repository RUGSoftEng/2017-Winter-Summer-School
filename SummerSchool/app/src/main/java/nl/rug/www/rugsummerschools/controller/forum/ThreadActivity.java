package nl.rug.www.rugsummerschools.controller.forum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This is an Activity class to post or edit a forum thread.
 *
 * @since 05/06/2017
 * @author Jeongkyun Oh
 */

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener, NetworkingService.VolleyCallback {

    private static final String EXTRA_EDIT_THREAD_DATA = "nl.rug.www.extra_edit_thread_data";
    private static final String EXTRA_EDITED_TREAD_DATA = "nl.rug.www.extra_edited_thread_data";

    public static final int INT_ADD = 0;
    public static final int INT_EDIT = 1;

    public static final String ARG_ADD_OR_EDIT = "add_or_edit";
    public static final String ARG_EDITABLE_DATA = "editable_data";

    private static final String TAG = "ThreadActivity";
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private int mFlag = INT_ADD;
    private String[] mData;

    public static Intent newIntent(Context packageContext, String[] data) {
        Intent intent = new Intent(packageContext, ThreadActivity.class);
        intent.putExtra(EXTRA_EDIT_THREAD_DATA, data);
        return intent;
    }

    public static String[] getForumThread(Intent data) {
        String[] editedData = new String[2];
        try {
            editedData = data.getStringArrayExtra(EXTRA_EDITED_TREAD_DATA);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return editedData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);

        mTitleEditText = (EditText)findViewById(R.id.title_edit_text);
        mContentsEditText = (EditText)findViewById(R.id.contents_edit_text);
        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(this);

        mData = getIntent().getStringArrayExtra(EXTRA_EDIT_THREAD_DATA);

        if (mData != null) {
            mFlag = INT_EDIT;
            mTitleEditText.setText(mData[1]);
            mContentsEditText.setText(mData[2]);
            postButton.setText(R.string.edit);
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
                            map.put("imgURL", user.getPhotoUrl().toString());
                            new NetworkingService<>().postPutRequest(this, Request.Method.POST, paths, null, map, this);
                            break;
                        case INT_EDIT :
                            map.put("id", mData[0]);
                            new NetworkingService<>().postPutRequest(this, Request.Method.PUT, paths, map, null, this);
                            break;
                    }

                }
                break;
        }
    }

    @Override
    public void onResponse(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if ("OK".equals(result) || "200".equals(result)) {
            Intent intent = new Intent();
            String[] data = new String[] {
                    mTitleEditText.getText().toString(),
                    mContentsEditText.getText().toString()
            };
            intent.putExtra(EXTRA_EDITED_TREAD_DATA, data);
            setResult(Activity.RESULT_OK, intent);
        } else
            setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onError(NetworkResponse result) {
        Toast.makeText(this, "Error:" + result, Toast.LENGTH_SHORT).show();
    }
}
