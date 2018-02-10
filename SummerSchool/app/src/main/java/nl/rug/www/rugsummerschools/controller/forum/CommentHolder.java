package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.PUT;

/**
 * ViewHolder class for comment recyclerview on forum section.
 * This class is to bind a comment to each associated view holder.
 * It provides functionality to modify and delete the comment based on the authority.
 *
 * @since 10/02/2018
 * @version 2.0.0
 * @author Jeongkyun Oh
 */
public abstract class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, android.widget.PopupMenu.OnMenuItemClickListener {

    private Context mContext;
    private ForumComment mForumComment;
    private ImageView mAuthorPhotoView;
    private TextView mAuthorView;
    private TextView mCommentView;
    private TextView mCommentTimeView;
    private ImageView mMoreButton;

    private EditText mBodyEditTextView;
    private Button mBodyEditButton;
    private Button mBodyEditCancelButton;

    abstract public void fetchComment();
    abstract public void hideCommentView();

    public CommentHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        super(inflater.inflate(R.layout.item_comment, parent, false));

        mContext = context;
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
        mCommentTimeView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        if (mForumComment.getImgUrl() != null || !"".equals(mForumComment.getImgUrl()))
            Glide.with(mContext).load(mForumComment.getImgUrl()).into(mAuthorPhotoView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid().equals(mForumComment.getPosterId()))
            mMoreButton.setVisibility(View.VISIBLE);
        mMoreButton.setOnClickListener(this);
        mBodyEditCancelButton.setOnClickListener(this);
        mBodyEditButton.setOnClickListener(this);
    }

    private void setTextViews() {
        mCommentView.setVisibility(View.VISIBLE);
        mCommentView.setVisibility(View.VISIBLE);
        mBodyEditTextView.setVisibility(View.GONE);
        mBodyEditButton.setVisibility(View.GONE);
        mBodyEditCancelButton.setVisibility(View.GONE);
    }

    private void setModifyingView() {
        mCommentView.setVisibility(View.GONE);
        mBodyEditTextView.setVisibility(View.VISIBLE);
        mBodyEditButton.setVisibility(View.VISIBLE);
        mBodyEditCancelButton.setVisibility(View.VISIBLE);
        mBodyEditTextView.setText(mCommentView.getText().toString());
    }

    private List<String> createCommentPaths() {
        return NetworkingService.getCommentPath();
    }

    private Map<String, String> createDeleteQuery() {
        return NetworkingService.getDeleteQuery(mForumComment.getId());
    }

    private Map<String, String> createPutQuery() {
        return NetworkingService.getPutCommentQuery(mForumComment.getId(), mBodyEditTextView.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_more_comment :
                android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(mContext, view);
                popupMenu.setOnMenuItemClickListener(this);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_edit_delete, popupMenu.getMenu());
                popupMenu.show();
                break;
            case R.id.comment_body_edit_button :
                new NetworkingService<>().postPutRequest(mContext, PUT, createCommentPaths(), createPutQuery(), null, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onResponse(String result) {
                        if ("OK".equals(result) || "200".equals(result)) {
                            mCommentView.setText(mBodyEditTextView.getText().toString());
                        }
                        setTextViews();
                    }

                    @Override
                    public void onError(NetworkResponse result) {
                        Toast.makeText(mContext, "Unexpected Error", Toast.LENGTH_SHORT).show();
                        setTextViews();
                    }
                });
                break;
            case R.id.comment_body_cancel_button :
                setTextViews();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.edit_menu :
                hideCommentView();
                setModifyingView();
                break;
            case R.id.delete_menu :
                new NetworkingService<>().getDeleteRequest(mContext, DELETE, createCommentPaths(), createDeleteQuery(), null, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onResponse(String result) {
                        if ("OK".equals(result) || "200".equals(result)) {
                            Toast.makeText(mContext, "Deletion succeeded", Toast.LENGTH_SHORT).show();
                            fetchComment();
                        } else {
                            Toast.makeText(mContext, "Deletion failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(NetworkResponse result) {
                        Toast.makeText(mContext, "Unexpected error happened!", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
        return true;
    }
}
