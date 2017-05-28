package nl.rug.www.summerschool.controller.forum;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.model.Announcement;
import nl.rug.www.summerschool.model.ForumComment;
import nl.rug.www.summerschool.model.ForumThread;
import nl.rug.www.summerschool.networking.NetworkingService;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

public class ForumFragment extends Fragment {

    private RecyclerView mForumRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<ForumThread> mItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        new FetchThreadsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        TextView section = (TextView)view.findViewById(R.id.section_name);
        section.setText(R.string.forum);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchThreadsTask().execute();
            }
        });
        if (mItems == null)
            mSwipeRefreshLayout.setRefreshing(true);

        mForumRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mForumRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdatper();
        view.findViewById(R.id.forum_add_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddThreadActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setupAdatper() {
        if (isAdded()) {
            mForumRecyclerView.setAdapter(new ForumAdapter(mItems));
        }
    }


    private class ForumHolder extends RecyclerView.ViewHolder {

        private ForumThread mForumThread;
        private ImageView mPosterImageView;
        private TextView mPosterTextView;
        private TextView mPostedDateTextView;
        private TextView mPostedTimeTextView;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mNewTextView;
        private RecyclerView mCommentsRecyclerView;

        public ForumHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_forum_thread, parent, false));

            mPosterTextView = (TextView)itemView.findViewById(R.id.forum_poster_text_view);
            mPostedDateTextView = (TextView)itemView.findViewById(R.id.date);
            mPostedTimeTextView = (TextView)itemView.findViewById(R.id.time);
            mNewTextView = (TextView)itemView.findViewById(R.id.new_image_view);
            mTitleTextView = (TextView)itemView.findViewById(R.id.forum_thread_title_text_view);
            mDescriptionTextView = (TextView)itemView.findViewById(R.id.forum_thread_description_text_view);
            mCommentsRecyclerView = (RecyclerView)itemView.findViewById(R.id.forum_comments_recycler_view);
            mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        private void bind(ForumThread forumThread) {
            mForumThread = forumThread;
            mPosterTextView.setText(mForumThread.getPoster());
            Date date = new DateTime(mForumThread.getDate()).toDate();
            SimpleDateFormat parseDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            mPostedDateTextView.setText(parseDate.format(date));
            mPostedTimeTextView.setText(parseTime.format(date));
            mTitleTextView.setText(mForumThread.getTitle());
            mDescriptionTextView.setText(mForumThread.getDescription());
            List<ForumComment> comments = mForumThread.getForumCommentList();
            if (comments != null)
                mCommentsRecyclerView.setAdapter(new CommentAdapter(comments));

            Date today = new Date();
            if (today.getTime() - date.getTime() < MILLIS_PER_DAY) {
                mNewTextView.setVisibility(View.VISIBLE);
            } else {
                mNewTextView.setVisibility(View.GONE);
            }
        }

        private class CommentHolder extends RecyclerView.ViewHolder {

            private ForumComment mForumComment;
            private ImageView mCommentImageView;
            private TextView mCommentPosterTextView;
            private TextView mCommentContentsTextView;

            public CommentHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_forum_comment, parent, false));

                mCommentPosterTextView = (TextView)itemView.findViewById(R.id.comment_poster_text_view);
                mCommentContentsTextView = (TextView)itemView.findViewById(R.id.comment_contents_text_view);
            }

            private void bind(ForumComment forumComment) {
                mForumComment = forumComment;
                mCommentPosterTextView.setText(mForumComment.getPoster());
                mCommentContentsTextView.setText(mForumComment.getText());
            }
        }

        private class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

            private List<ForumComment> mForumComments;

            public CommentAdapter(List<ForumComment> forumComments) {
                mForumComments = forumComments;
            }

            @Override
            public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new CommentHolder(layoutInflater, parent);
            }

            @Override
            public void onBindViewHolder(CommentHolder holder, int position) {
                ForumComment forumComment = mForumComments.get(position);
                holder.bind(forumComment);
            }

            @Override
            public int getItemCount() {
                return mForumComments.size();
            }
        }
    }

    private class ForumAdapter extends RecyclerView.Adapter<ForumHolder> {

        private List<ForumThread> mForumThreads;

        public ForumAdapter(List<ForumThread> forumThreads) {
            mForumThreads = forumThreads;
        }

        @Override
        public ForumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new ForumHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ForumHolder holder, int position) {
            ForumThread forumThread = mForumThreads.get(position);
            holder.bind(forumThread);
        }

        @Override
        public int getItemCount() {
            return mForumThreads.size();
        }
    }

    private class FetchThreadsTask extends AsyncTask<Void, Void, List<ForumThread>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<ForumThread> doInBackground(Void... params) {
            return new NetworkingService().fetchForumThreads();
        }

        @Override
        protected void onPostExecute(List<ForumThread> announcements) {
            mItems = announcements;
            setupAdatper();
            ContentsLab.get().updateForumThreads(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
