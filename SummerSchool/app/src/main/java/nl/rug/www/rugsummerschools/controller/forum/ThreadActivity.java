package nl.rug.www.rugsummerschools.controller.forum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static nl.rug.www.rugsummerschools.controller.forum.ForumFragment.INT_ADD;
import static nl.rug.www.rugsummerschools.controller.forum.ForumFragment.INT_EDIT;

/**
 * This is an Activity class to post or edit a forum thread.
 *
 * @since 05/06/2017
 * @author Jeongkyun Oh
 */

public class ThreadActivity extends AppCompatActivity {

    public static final String ARG_ADD_OR_EDIT = "add_or_edit";
    public static final String ARG_EDITABLE_DATA = "editable_data";

    private static final String TAG = "ThreadActivity";
    private EditText mTitleEditText;
    private EditText mContentsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thread);

        mTitleEditText = (EditText)findViewById(R.id.title_edit_text);
        mContentsEditText = (EditText)findViewById(R.id.contents_edit_text);
        Button postButton = (Button) findViewById(R.id.post_button);
        final Bundle bundle = getIntent().getExtras();
        int addOrEdit = bundle.getInt(ARG_ADD_OR_EDIT);
        switch (addOrEdit) {
            case INT_ADD :
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = mTitleEditText.getText().toString();
                        String description = mContentsEditText.getText().toString();
                        if (title.equals("") || description.equals("")) {
                            Toast.makeText(ThreadActivity.this, "Title or description cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Map<String, String> map = new HashMap<>();
                            map.put("title", title);
                            map.put("description", description);
                            map.put("author", user.getDisplayName());
                            map.put("posterID", user.getUid());
                            map.put("imgurl", user.getPhotoUrl().toString());

                            new NetworkingService().postRequestForumThread(ThreadActivity.this, "thread", map, new NetworkingService.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFail(String result) {
                                    setResult(Activity.RESULT_CANCELED);
                                    Toast.makeText(ThreadActivity.this, "It fails to post forum thread.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onError(String result) {
                                    Toast.makeText(ThreadActivity.this, "Error:" + result, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                break;
            case INT_EDIT :
                final String[] data = bundle.getStringArray(ARG_EDITABLE_DATA);
                mTitleEditText.setText(data[1]);
                mContentsEditText.setText(data[2]);
                postButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = mTitleEditText.getText().toString();
                        String description = mContentsEditText.getText().toString();
                        if (title.equals("") || description.equals("")) {
                            Toast.makeText(ThreadActivity.this, "Title or description cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, String> map = new HashMap<>();
                            map.put("threadID", data[0]);
                            map.put("title", title);
                            map.put("description", description);

                            new NetworkingService().putRequestForumThread(ThreadActivity.this, "thread", map, new NetworkingService.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                }

                                @Override
                                public void onFail(String result) {
                                    Toast.makeText(ThreadActivity.this, "It fails to edit forum thread.", Toast.LENGTH_SHORT).show();
                                    setResult(Activity.RESULT_CANCELED);
                                    finish();
                                }

                                @Override
                                public void onError(String result) {
                                    Toast.makeText(ThreadActivity.this, "Error:" + result, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                break;
        }


    }
}
