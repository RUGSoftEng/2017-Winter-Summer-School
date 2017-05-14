package nl.rug.www.summerschool.controller.forum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.model.ForumComment;
import nl.rug.www.summerschool.model.ForumThread;

public class ForumFragment extends Fragment {

    private RecyclerView mForumRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        TextView section = (TextView)view.findViewById(R.id.section_name);
        section.setText(R.string.forum);

        mForumRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mForumRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mForumRecyclerView.setAdapter(new ForumAdapter(ContentsLab.get().getForumThreads()));

        return view;
    }

    private class ForumHolder extends RecyclerView.ViewHolder {

        private ForumThread mForumThread;
        private ImageView mPosterImageView;
        private TextView mPosterTextView;
        private TextView mPostedTimeTextView;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private RecyclerView mCommentsRecyclerView;

        public ForumHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_forum_thread, parent, false));

            mPosterTextView = (TextView)itemView.findViewById(R.id.forum_poster_text_view);
            mPostedTimeTextView = (TextView)itemView.findViewById(R.id.forum_posted_time_text_view);
            mTitleTextView = (TextView)itemView.findViewById(R.id.forum_thread_title_text_view);
            mDescriptionTextView = (TextView)itemView.findViewById(R.id.forum_thread_description_text_view);
            mCommentsRecyclerView = (RecyclerView)itemView.findViewById(R.id.forum_comments_recycler_view);
            mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        private void bind(ForumThread forumThread) {
            mForumThread = forumThread;
            mPosterTextView.setText(mForumThread.getPoster());
            mPostedTimeTextView.setText(mForumThread.getDate());
            mTitleTextView.setText(mForumThread.getTitle());
            mDescriptionTextView.setText(mForumThread.getDescription());
            List<ForumComment> comments = mForumThread.getForumCommentList();
            if (comments != null)
                mCommentsRecyclerView.setAdapter(new CommentAdapter(comments));
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
}
