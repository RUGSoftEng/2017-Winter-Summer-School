//package nl.rug.www.rugsummerschools.controller.forum;
//
//import android.content.Context;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
//
//import nl.rug.www.rugsummerschools.R;
//import nl.rug.www.rugsummerschools.model.ForumThread;
//
///**
// * This class is ForumComment adapter class in order to adapt items to ViewHolder.
// *
// * @since 05/06/2017
// * @author Jeongkyun Oh
// */
//
//@Deprecated
//public class CommentViewHolder extends ChildViewHolder {
//
//    private ImageView mProfilePictureView;
//    private TextView mNameView;
//    private TextView mDateView;
//    private TextView mContentsView;
//    private TextView mNewTextView;
//
//    public CommentViewHolder(View itemView, ForumThread forumThread, Context context) {
//        super(itemView);
//        mProfilePictureView = (ImageView)itemView.findViewById(R.id.comment_image_view);
//        mNameView = (TextView)itemView.findViewById(R.id.comment_poster_text_view);
//        mDateView = (TextView)itemView.findViewById(R.id.comment_date_text_view);
//        mContentsView = (TextView)itemView.findViewById(R.id.comment_contents_text_view);
//        mNewTextView = (TextView)itemView.findViewById(R.id.new_image_view);
//    }
//
//    public ImageView getProfilePictureView() {
//        return mProfilePictureView;
//    }
//
//    public TextView getNameView() {
//        return mNameView;
//    }
//
//    public TextView getDateView() {
//        return mDateView;
//    }
//
//    public TextView getContentsView() {
//        return mContentsView;
//    }
//
//    public TextView getNewTextView() {
//        return mNewTextView;
//    }
//}
