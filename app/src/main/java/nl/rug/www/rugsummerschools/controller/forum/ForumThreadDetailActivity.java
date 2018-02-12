package nl.rug.www.rugsummerschools.controller.forum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.BaseActivity;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static com.android.volley.Request.Method.DELETE;

/**
 * This is detail activity of a forum thread.
 * It shows information about the contents of forum and comments.
 *
 * @author Jeongkyun Oh
 * @since 09/02/2018
 * @version 2.0.0
 */

public class ForumThreadDetailActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, NestedScrollView.OnScrollChangeListener, View.OnTouchListener {

    private static final String TAG = "ThreadDetailActivity";
    private static final String EXTRA_FORUM_THREAD_ID =
            "nl.rug.www.rugsummerschool.forum_thread_id";
    private static final int REQUEST_CODE_EDIT_THREAD = 0;

    private ForumThread mForumThread;
    private List<ForumComment> mForumComments;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBodyView;
    private ImageView mPosterPhotoView;
    private TextView mRelativeTimeView;
    private EditText mCommentEditText;
    private LinearLayout mCommentPostView;
    private RecyclerView mCommentRecyclerView;
    private int mPostViewTop = 0;

    public static Intent newIntent(Context context, String threadId) {
        Intent intent = new Intent(context, ForumThreadDetailActivity.class);
        intent.putExtra(EXTRA_FORUM_THREAD_ID, threadId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thd_dtl);
        String threadId = getIntent().getStringExtra(EXTRA_FORUM_THREAD_ID);
        setTitle("Forum");
        mCommentPostView = findViewById(R.id.comment_form);
        NestedScrollView nestedScrollView = findViewById(R.id.nsv_forum_detail);
        nestedScrollView.requestFocus();
        nestedScrollView.setOnScrollChangeListener(this);
        nestedScrollView.setOnTouchListener(this);
        ImageView commentPostImageView = findViewById(R.id.comment_post_photo);
        ImageView moreButton = findViewById(R.id.btn_more_post);
        moreButton.setOnClickListener(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(this).load(user.getPhotoUrl()).into(commentPostImageView);
        mForumThread = ContentsLab.get().getForumThread(threadId);
        if (user.getUid().equals(mForumThread.getPosterId()))
            moreButton.setVisibility(View.VISIBLE);
        mTitleView = findViewById(R.id.post_title);
        mAuthorView = findViewById(R.id.post_author);
        mBodyView = findViewById(R.id.post_body);
        mCommentEditText = findViewById(R.id.field_comment_text);
        mPosterPhotoView = findViewById(R.id.post_author_photo);
        mRelativeTimeView = findViewById(R.id.post_time_view);
        mTitleView.setText(mForumThread.getTitle());
        mAuthorView.setText(mForumThread.getPoster());
        mBodyView.setText(mForumThread.getDescription());
        Date date = new DateTime(mForumThread.getDate()).toDate();
        mRelativeTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        if (mForumThread.getImgUrl() != null || !"".equals(mForumThread.getImgUrl()))
            Glide.with(this).load(mForumThread.getImgUrl()).into(mPosterPhotoView);
        findViewById(R.id.button_post_comment).setOnClickListener(this);
        mCommentRecyclerView = findViewById(R.id.recycler_comments);
        mCommentRecyclerView.setFocusable(false);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setNestedScrollingEnabled(false);
        new FetchForumComments().execute();
    }

    private void setupAdapter() {
        mCommentRecyclerView.setAdapter(newAdapter());
    }

    private CommentAdapter newAdapter() {
        return new CommentAdapter(mForumComments, this) {
            @Override
            public CommentHolder createHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
                return newHolder(inflater, parent, context);
            }
        };
    }

    private CommentHolder newHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        return new CommentHolder(inflater, parent, context) {
            @Override
            public void fetchComment() {
                new FetchForumComments().execute();
            }

            @Override
            public void hideCommentView() {
                mCommentPostView.setVisibility(View.GONE);
            }

            @Override
            public void showCommentView() {
                mCommentPostView.setVisibility(View.VISIBLE);
            }

            @Override
            public void showProgress() {
                showProgressDialog();
            }

            @Override
            public void hideProgress() {
                hideProgressDialog();
            }
        };
    }

    private List<String> createCommentPaths() {
        return NetworkingService.getCommentPath();
    }

    private Map<String, String> createPostQuery() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return NetworkingService.getPostCommentQuery(mCommentEditText.getText().toString(), user.getDisplayName(), user.getUid(), mForumThread.getId(), user.getPhotoUrl().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_post_comment :
                showProgressDialog();
                new NetworkingService<>().postPutRequest(ForumThreadDetailActivity.this, Request.Method.POST, createCommentPaths(), null, createPostQuery(), new NetworkingService.NetworkCallback() {
                    @Override
                    public void onResponse(String result) {
                        hideProgressDialog();
                        if ("OK".equals(result) || "200".equals(result)) {
                            Toast.makeText(ForumThreadDetailActivity.this, "Post succeeded!", Toast.LENGTH_LONG).show();
                            new FetchForumComments().execute();
                            View view = ForumThreadDetailActivity.this.getCurrentFocus();
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null && view != null) {
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            mCommentEditText.setText(null);
                        } else {
                            Toast.makeText(ForumThreadDetailActivity.this, "Post failed!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(NetworkResponse result) {
                        errorMessage(result, "Post forum comment is failed!");
                    }
                });
                break;
            case R.id.btn_more_post :
                PopupMenu popupMenu = new PopupMenu(ForumThreadDetailActivity.this, v);
                popupMenu.setOnMenuItemClickListener(ForumThreadDetailActivity.this);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_edit_delete, popupMenu.getMenu());
                popupMenu.show();
                break;
        }
    }

    private String[] createForumData() {
        return new String[] {
                mForumThread.getId(),
                mForumThread.getTitle(),
                mForumThread.getDescription()
        };
    }

    private List<String> createThreadPaths() {
        return NetworkingService.getThreadPath();
    }

    private Map<String, String> createDeleteQuery() {
        return NetworkingService.getDeleteQuery(mForumThread.getId());
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.edit_menu :
                intent = ThreadActivity.newIntent(this, createForumData());
                startActivityForResult(intent, REQUEST_CODE_EDIT_THREAD);
                break;
            case R.id.delete_menu :
                showProgressDialog();
                new NetworkingService<>().getDeleteRequest(
                        this, DELETE, createThreadPaths(), createDeleteQuery(), new NetworkingService.NetworkCallback() {
                    @Override
                    public void onResponse(String result) {
                        hideProgressDialog();
                        if ("OK".equals(result) || "200".equals(result)) {
                            Toast.makeText(ForumThreadDetailActivity.this, "Deletion succeeded!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ForumThreadDetailActivity.this, "Deletion failed!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(NetworkResponse result) {
                        hideProgressDialog();
                        errorMessage(result, "Delete forum thread is failed!");
                    }
                });
                break;
        }
        return true;
    }

    private void errorMessage(NetworkResponse result, String message) {
        if (result != null) {
            switch (result.statusCode) {
                case 400 :
                    Toast.makeText(ForumThreadDetailActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(ForumThreadDetailActivity.this, "Error : " + result.statusCode, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(ForumThreadDetailActivity.this, "Unexpected error happened!", Toast.LENGTH_LONG).show();
        }
        hideProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CODE_EDIT_THREAD) {
            if (data == null) return;

            String[] strings = ThreadActivity.getForumThread(data);
            mForumThread.setTitle(strings[0]);
            mForumThread.setDescription(strings[1]);
            mTitleView.setText(strings[0]);
            mBodyView.setText(strings[1]);
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int height = -mCommentPostView.getHeight();
        if (mPostViewTop <= 0 && mPostViewTop >= height) {
            mPostViewTop += (oldScrollY - scrollY);
            if (mPostViewTop > 0){
                mPostViewTop = 0;
            } else if (mPostViewTop < height) {
                mPostViewTop = height;
            }
            mCommentPostView.scrollTo(0, mPostViewTop);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP :
                int height = -mCommentPostView.getHeight();
                if (mPostViewTop >= height/2) {
                    mCommentPostView.scrollTo(0, 0);
                    mPostViewTop = 0;
                } else {
                    mCommentPostView.scrollTo(0, height);
                    mPostViewTop = height;
                }
                return false;
        }
        return false;
    }

    private class FetchForumComments extends AsyncTask<Void, Void, List<ForumComment>> {

        @Override
        protected List<ForumComment> doInBackground(Void... voids) {
            return new NetworkingService<ForumComment>().fetchData(NetworkingService.FORUM_COMMENT, mForumThread.getId());
        }

        @Override
        protected void onPostExecute(List<ForumComment> forumComments) {
            super.onPostExecute(forumComments);
            mForumComments = forumComments;
            setupAdapter();
            ContentsLab.get().updateForumComments(forumComments);
        }
    }
}
