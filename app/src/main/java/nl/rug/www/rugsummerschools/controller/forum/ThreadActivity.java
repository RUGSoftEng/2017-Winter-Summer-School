package nl.rug.www.rugsummerschools.controller.forum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.BaseActivity;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This is an Activity class to post or edit a forum thread.
 *
 * @since 05/06/2017
 * @author Jeongkyun Oh
 * @version 2.0.0
 */

public class ThreadActivity extends BaseActivity implements NetworkingService.NetworkCallback {

    private static final String TAG = "ThreadActivity";

    private static final String EXTRA_EDIT_THREAD_DATA = "nl.rug.www.extra_edit_thread_data";
    private static final String EXTRA_EDITED_TREAD_DATA = "nl.rug.www.extra_edited_thread_data";

    public static final int INDEX_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_DETAIL = 2;

    public static final int INT_ADD = 0;
    public static final int INT_EDIT = 1;

    private int mFlag = INT_ADD;
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private String[] mData;

    public static Intent newIntent(Context packageContext, String[] data) {
        Intent intent = new Intent(packageContext, ThreadActivity.class);
        intent.putExtra(EXTRA_EDIT_THREAD_DATA, data);
        return intent;
    }

    public static String[] getForumThread(Intent data) {
        String[] editedData = new String[3];
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

        if (mFlag == INT_ADD) {
            setTitle("Add Forum Thread");
        } else {
            setTitle("Edit Forum Thread");
        }
        mTitleEditText = findViewById(R.id.title_edit_text);
        mContentsEditText = findViewById(R.id.contents_edit_text);

        mData = getIntent().getStringArrayExtra(EXTRA_EDIT_THREAD_DATA);

        if (mData != null) {
            mFlag = INT_EDIT;
            mTitleEditText.setText(mData[INDEX_TITLE]);
            mContentsEditText.setText(mData[INDEX_DETAIL]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_menu :
                String title = mTitleEditText.getText().toString();
                String description = mContentsEditText.getText().toString();

                if (title.equals("") || description.equals("")) {
                    Toast.makeText(ThreadActivity.this, "Title or description cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog();
                    switch (mFlag) {
                        case INT_ADD :
                            new NetworkingService<>().postPutRequest(this, Request.Method.POST, NetworkingService.getThreadPath(), null, getPostQuery(), this);
                            break;
                        case INT_EDIT :
                            new NetworkingService<>().postPutRequest(this, Request.Method.PUT, NetworkingService.getThreadPath(), getPutQuery(), null, this);
                            break;
                    }

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Map<String, String> getPostQuery() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String title = mTitleEditText.getText().toString();
        String description = mContentsEditText.getText().toString();
        String author = user.getDisplayName();
        String posterID = user.getUid();
        String imgURL = user.getPhotoUrl().toString();
        String school = ContentsLab.get().getSchoolInfo().getSchoolId();
        return NetworkingService.getPostThreadQuery(title, description, author, posterID, imgURL, school);
    }

    private Map<String, String> getPutQuery() {
        String id = mData[INDEX_ID];
        String title = mTitleEditText.getText().toString();
        String description = mContentsEditText.getText().toString();
        return NetworkingService.getPutThreadQuery(id, title, description);
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
        hideProgressDialog();
        finish();
    }

    @Override
    public void onError(NetworkResponse result) {
        Toast.makeText(this, "Error:" + result, Toast.LENGTH_SHORT).show();
        hideProgressDialog();
    }
}
