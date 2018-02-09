package nl.rug.www.rugsummerschools.controller.forum;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.databinding.ActivityForumThreadDetailBinding;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;
import static nl.rug.www.rugsummerschools.controller.forum.ThreadActivity.ARG_ADD_OR_EDIT;
import static nl.rug.www.rugsummerschools.controller.forum.ThreadActivity.ARG_EDITABLE_DATA;
import static nl.rug.www.rugsummerschools.controller.forum.ThreadActivity.INT_ADD;
import static nl.rug.www.rugsummerschools.controller.forum.ThreadActivity.INT_EDIT;

public class ForumThreadDetailActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, NestedScrollView.OnScrollChangeListener {

    private static final String TAG = "ThreadDetailActivity";
    private static final String EXTRA_FORUM_THREAD_ID =
            "nl.rug.www.rugsummerschool.forum_thread_id";
    private static final int REQUEST_CODE_EDIT_THREAD = 0;

    private ForumThread mForumThread;
    private List<ForumComment> mForumComments;
    private ActivityForumThreadDetailBinding mActivityForumThreadDetailBinding;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBodyView;
    private ImageView mPosterPhotoView;
    private TextView mRelativeTimeView;
    private EditText mCommentEditText;
    private LinearLayout mCommentPostView;
    private RecyclerView mCommentRecyclerView;

    public static Intent newIntent(Context context, String threadId) {
        Intent intent = new Intent(context, ForumThreadDetailActivity.class);
        intent.putExtra(EXTRA_FORUM_THREAD_ID, threadId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_thread_detail);
        String threadId = getIntent().getStringExtra(EXTRA_FORUM_THREAD_ID);

        mCommentPostView = findViewById(R.id.comment_form);
        NestedScrollView nestedScrollView = findViewById(R.id.nsv_forum_detail);
        nestedScrollView.requestFocus();
        nestedScrollView.setOnScrollChangeListener(this);
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
        mRelativeTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        if (mForumThread.getImgUrl() != null || !"".equals(mForumThread.getImgUrl()))
            Glide.with(this).load(mForumThread.getImgUrl()).into(mPosterPhotoView);
        findViewById(R.id.button_post_comment).setOnClickListener(this);
        mCommentRecyclerView = findViewById(R.id.recycler_comments);
        mCommentRecyclerView.setFocusable(false);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new FetchForumComments().execute();
    }

    void setupAdapter() {
        mCommentRecyclerView.setAdapter(new ThreadDetailAdapter(mForumComments));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_post_comment :
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, String> map = new HashMap<>();
                map.put("parentThread", mForumThread.getId());
                map.put("author", user.getDisplayName());
                map.put("posterID", user.getUid());
                map.put("text", mCommentEditText.getText().toString());
                map.put("imgURL", user.getPhotoUrl().toString());

                List<String> paths = new ArrayList<>();
                paths.add("forum");
                paths.add("comment");

                new NetworkingService<>().postPutRequest(ForumThreadDetailActivity.this, Request.Method.POST, paths, null, map, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onResponse(String result) {
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.edit_menu :
                String[] data = new String[] {
                        mForumThread.getId(),
                        mForumThread.getTitle(),
                        mForumThread.getDescription()
                };
                intent = ThreadActivity.newIntent(this, data);
                startActivityForResult(intent, REQUEST_CODE_EDIT_THREAD);
                break;
            case R.id.delete_menu :
                List<String> paths = new ArrayList<>();
                paths.add("forum");
                paths.add("thread");
                Map<String, String> map = new HashMap<>();
                map.put("id", mForumThread.getId());
                new NetworkingService<>().getDeleteRequest(this, DELETE, paths, map, null, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onResponse(String result) {
                        if ("OK".equals(result) || "200".equals(result)) {
                            Toast.makeText(ForumThreadDetailActivity.this, "Deletion succeeded!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(ForumThreadDetailActivity.this, "Deletion failed!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(NetworkResponse result) {
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
        if (scrollY - oldScrollY > 0) {
            mCommentPostView.setVisibility(View.GONE);
        } else {
            mCommentPostView.setVisibility(View.VISIBLE);
        }
    }

    private class ThreadDetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

        private ForumComment mForumComment;
        private ImageView mAuthorPhotoView;
        private TextView mAuthorView;
        private TextView mCommentView;
        private TextView mCommentTimeView;
        private ImageView mMoreButton;

        private EditText mBodyEditTextView;
        private Button mBodyEditButton;
        private Button mBodyEditCancelButton;

        public ThreadDetailHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_comment, parent, false));

            mAuthorPhotoView = itemView.findViewById(R.id.comment_photo);
            mAuthorView = itemView.findViewById(R.id.comment_author);
            mCommentView = itemView.findViewById(R.id.comment_body);
            mCommentTimeView = itemView.findViewById(R.id.comment_time_view);
            mMoreButton = itemView.findViewById(R.id.btn_more_comment);

            mBodyEditTextView = itemView.findViewById(R.id.comment_body_edit_text);
            mBodyEditButton = itemView.findViewById(R.id.comment_body_edit_button);
            mBodyEditCancelButton = itemView.findViewById(R.id.comment_body_cancel_button);
        }

        public void bind(ForumComment forumComment) {
            mForumComment = forumComment;
            mAuthorView.setText(mForumComment.getPoster());
            mCommentView.setText(mForumComment.getDescription());
            Date date = new DateTime(mForumComment.getDate()).toDate();
            mCommentTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.YEAR_IN_MILLIS));
            if (mForumComment.getImgUrl() != null || !"".equals(mForumComment.getImgUrl()))
                Glide.with(ForumThreadDetailActivity.this).load(mForumComment.getImgUrl()).into(mAuthorPhotoView);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user.getUid().equals(mForumComment.getPosterId()))
                mMoreButton.setVisibility(View.VISIBLE);
            mMoreButton.setOnClickListener(this);
            mBodyEditCancelButton.setOnClickListener(this);
            mBodyEditButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_more_comment :
                    PopupMenu popupMenu = new PopupMenu(ForumThreadDetailActivity.this, view);
                    popupMenu.setOnMenuItemClickListener(this);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_edit_delete, popupMenu.getMenu());
                    popupMenu.show();
                    break;
                case R.id.comment_body_edit_button :
                    List<String> paths = new ArrayList<>();
                    paths.add("forum");
                    paths.add("comment");
                    Map<String, String> map = new HashMap<>();
                    map.put("id", mForumComment.getId());
                    map.put("text", mBodyEditTextView.getText().toString());
                    new NetworkingService<>().postPutRequest(ForumThreadDetailActivity.this, PUT, paths, map, null, new NetworkingService.VolleyCallback() {
                        @Override
                        public void onResponse(String result) {
                            if ("OK".equals(result) || "200".equals(result)) {
                                mCommentView.setText(mBodyEditTextView.getText().toString());
                            }
                            mCommentView.setVisibility(View.VISIBLE);
                            mCommentView.setVisibility(View.VISIBLE);
                            mBodyEditTextView.setVisibility(View.GONE);
                            mBodyEditButton.setVisibility(View.GONE);
                            mBodyEditCancelButton.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(NetworkResponse result) {
                            Toast.makeText(ForumThreadDetailActivity.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                            mCommentView.setVisibility(View.VISIBLE);
                            mCommentView.setVisibility(View.VISIBLE);
                            mBodyEditTextView.setVisibility(View.GONE);
                            mBodyEditButton.setVisibility(View.GONE);
                            mBodyEditCancelButton.setVisibility(View.GONE);
                        }
                    });
                    break;
                case R.id.comment_body_cancel_button :
                    mCommentView.setVisibility(View.VISIBLE);
                    mCommentView.setVisibility(View.VISIBLE);
                    mBodyEditTextView.setVisibility(View.GONE);
                    mBodyEditButton.setVisibility(View.GONE);
                    mBodyEditCancelButton.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit_menu :
                    mCommentPostView.setVisibility(View.GONE);
                    mCommentView.setVisibility(View.GONE);
                    mBodyEditTextView.setVisibility(View.VISIBLE);
                    mBodyEditButton.setVisibility(View.VISIBLE);
                    mBodyEditCancelButton.setVisibility(View.VISIBLE);
                    mBodyEditTextView.setText(mCommentView.getText().toString());
                    break;
                case R.id.delete_menu :
                    List<String> paths = new ArrayList<>();
                    paths.add("forum");
                    paths.add("comment");
                    Map<String, String> map = new HashMap<>();
                    map.put("id", mForumComment.getId());
                    new NetworkingService<>().getDeleteRequest(ForumThreadDetailActivity.this, DELETE, paths, map, null, new NetworkingService.VolleyCallback() {
                        @Override
                        public void onResponse(String result) {
                            if ("OK".equals(result) || "200".equals(result)) {
                                Toast.makeText(ForumThreadDetailActivity.this, "Deletion succeeded", Toast.LENGTH_SHORT).show();
                                new FetchForumComments().execute();
                            } else {
                                Toast.makeText(ForumThreadDetailActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(NetworkResponse result) {
                            Toast.makeText(ForumThreadDetailActivity.this, "Unexpected error happened!", Toast.LENGTH_LONG).show();
                        }
                    });
                    break;
            }
            return true;
        }
    }

    private class ThreadDetailAdapter extends RecyclerView.Adapter<ThreadDetailHolder> {

        private List<ForumComment> mForumComments;

        public ThreadDetailAdapter(List<ForumComment> forumComments) {
            mForumComments = forumComments;
        }

        @Override
        public ThreadDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ForumThreadDetailActivity.this);
            return new ThreadDetailHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(ThreadDetailHolder holder, int position) {
            holder.bind(mForumComments.get(position));
        }

        @Override
        public int getItemCount() {
            return mForumComments.size();
        }
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
