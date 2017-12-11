package nl.rug.www.rugsummerschools.controller.forum;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.ForumThread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by jk on 17. 12. 9.
 */

public abstract class ForumThreadHolder extends ContentHolder<ForumThread> implements View.OnClickListener {

    private Context mContext;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBodyView;
    private ImageView mPosterPhotoView;

    public ForumThreadHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_post, parent, false));

        mContext = context;
        mTitleView = itemView.findViewById(R.id.post_title);
        mAuthorView = itemView.findViewById(R.id.post_author);
        mBodyView = itemView.findViewById(R.id.post_body);
        mPosterPhotoView = itemView.findViewById(R.id.post_author_photo);
        itemView.setOnClickListener(this);
    }

    public void bind(ForumThread forumThread) {
        mContent = forumThread;
        mTitleView.setText(mContent.getTitle());
        mAuthorView.setText(mContent.getPoster());
        mBodyView.setText(mContent.getDescription());
        Glide.with(mContext).load(forumThread.getImgUrl()).into(mPosterPhotoView);
    }
}
