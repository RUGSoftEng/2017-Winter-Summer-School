package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;

import java.util.Date;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.model.ForumThread;

/**
 * This is view holder class for forum thread recycler view on forum thread list fragment.
 * It binds the model of each forum to the associated views.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 09/12/2017
 */

public abstract class ForumThreadHolder extends ContentHolder<ForumThread> implements View.OnClickListener {

    private Context mContext;
    private TextView mTitleView;
    private TextView mAuthorView;
    private TextView mBodyView;
    private TextView mNumCommentView;
    private TextView mRelativeTimeView;
    private ImageView mPosterPhotoView;

    public ForumThreadHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_post, parent, false));

        mContext = context;
        mTitleView = itemView.findViewById(R.id.post_title);
        mAuthorView = itemView.findViewById(R.id.post_author);
        mBodyView = itemView.findViewById(R.id.post_body);
        mPosterPhotoView = itemView.findViewById(R.id.post_author_photo);
        mNumCommentView = itemView.findViewById(R.id.number_of_comments);
        mRelativeTimeView = itemView.findViewById(R.id.post_time_view);
        itemView.setOnClickListener(this);
    }

    public void bind(ForumThread forumThread) {
        mContent = forumThread;
        mTitleView.setText(mContent.getTitle());
        mAuthorView.setText(mContent.getPoster());
        mBodyView.setText(mContent.getDescription());
        String numComments = mContent.getForumComments().size() + " comments";
        mNumCommentView.setText(numComments);
        Date date = new DateTime(mContent.getDate()).toDate();
        mRelativeTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        Glide.with(mContext).load(forumThread.getImgUrl()).into(mPosterPhotoView);
    }
}
