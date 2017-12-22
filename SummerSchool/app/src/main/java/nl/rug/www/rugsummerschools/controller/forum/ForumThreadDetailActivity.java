package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

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

public class ForumThreadDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ThreadDetailActivity";
    private static final String EXTRA_FORUM_THREAD_ID =
            "nl.rug.www.rugsummerschool.forum_thread_id";

    private ForumThread mForumThread;
    private ActivityForumThreadDetailBinding mActivityForumThreadDetailBinding;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBodyView;
    private ImageView mPosterPhotoView;
    private TextView mRelativeTimeView;
    private EditText mCommentEditText;

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
        mForumThread = ContentsLab.get().getForumThread(threadId);
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
        Glide.with(this).load(mForumThread.getImgUrl()).into(mPosterPhotoView);
        findViewById(R.id.button_post_comment).setOnClickListener(this);
        RecyclerView recyclerComments = findViewById(R.id.recycler_comments);
        recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerComments.setAdapter(new ThreadDetailAdapter());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_post_comment :
                try {

                } catch (NullPointerException e) {
                    Log.e(TAG, e.getMessage() + "Send comment:");
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Map<String, String> map = new HashMap<>();
                map.put("threadID", mForumThread.getId());
                map.put("commentID", UUID.randomUUID().toString());
                map.put("author", user.getDisplayName());
                map.put("posterID", user.getUid());
                map.put("text", mCommentEditText.getText().toString());
                map.put("imgurl", user.getPhotoUrl().toString());
                new NetworkingService().postRequestForumThread(this, "comment", map, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onFail(String result) {
                        Toast.makeText(ForumThreadDetailActivity.this, "It fails to post your forum thread.\nPlease try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(ForumThreadDetailActivity.this, "Error:" + result, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private class ThreadDetailHolder extends RecyclerView.ViewHolder {

        private ForumComment mForumComment;
        private ImageView mAuthorPhotoView;
        private TextView mAuthorView;
        private TextView mCommentView;
        private TextView mCommentTimeView;

        public ThreadDetailHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_comment, parent, false));

            mAuthorPhotoView = itemView.findViewById(R.id.comment_photo);
            mAuthorView = itemView.findViewById(R.id.comment_author);
            mCommentView = itemView.findViewById(R.id.comment_body);
            mCommentTimeView = itemView.findViewById(R.id.comment_time_view);
        }

        public void bind(ForumComment forumComment) {
            mForumComment = forumComment;
            mAuthorView.setText(mForumComment.getPoster());
            mCommentView.setText(mForumComment.getText());
            Date date = new DateTime(mForumComment.getDate()).toDate();
            mCommentTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.YEAR_IN_MILLIS));
            Glide.with(ForumThreadDetailActivity.this).load(mForumComment.getImgUrl()).into(mAuthorPhotoView);
        }
    }

    private class ThreadDetailAdapter extends RecyclerView.Adapter<ThreadDetailHolder> {

        private List<ForumComment> mForumComments = mForumThread.getForumCommentList();

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
}
