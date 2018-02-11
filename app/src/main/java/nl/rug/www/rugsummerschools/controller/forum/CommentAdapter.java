package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.model.ForumComment;

/**
 * ViewAdapter class for comment recycler view on forum section
 * This class is to bind view holders with list of comments
 *
 * @since 10/02/2018
 * @version 2.0.0
 * @author Jeongkyun Oh
 */

public abstract class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private Context mContext;
    private List<ForumComment> mForumComments;

    public abstract CommentHolder createHolder(LayoutInflater inflater, ViewGroup parent, Context context);

    public CommentAdapter(List<ForumComment> forumComments, Context context) {
        mForumComments = forumComments;
        mContext = context;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return createHolder(inflater, parent, mContext);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.bind(mForumComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mForumComments.size();
    }
}