package nl.rug.www.rugsummerschool.controller.forum;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import nl.rug.www.rugsummerschool.R;

/**
 * Created by jk on 6/5/17.
 */

public class ThreadViewHolder extends ParentViewHolder {

    private TextView mCommentTextView;

    public ThreadViewHolder(View itemView) {
        super(itemView);
        mCommentTextView = (TextView)itemView.findViewById(R.id.comment_text_view);
    }

    public TextView getCommentTextView() {
        return mCommentTextView;
    }
}
