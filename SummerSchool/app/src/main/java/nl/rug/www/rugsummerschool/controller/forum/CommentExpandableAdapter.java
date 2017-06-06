package nl.rug.www.rugsummerschool.controller.forum;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bumptech.glide.Glide;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.controller.ContentsLab;
import nl.rug.www.rugsummerschool.model.ForumComment;
import nl.rug.www.rugsummerschool.model.ForumThread;
import nl.rug.www.rugsummerschool.networking.NetworkingService;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

/**
 * Created by jk on 6/5/17.
 */

public class CommentExpandableAdapter extends ExpandableRecyclerAdapter<ThreadViewHolder, CommentViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ForumThread mForumThread;
    private ForumFragment.FetchThreadsTask mFetchThreadsTask;

    public CommentExpandableAdapter(Context context, List parentItemList, ForumThread forumThread, ForumFragment.FetchThreadsTask fetchThreadsTask) {
        super(context, parentItemList);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mForumThread = forumThread;
        mFetchThreadsTask = fetchThreadsTask;
    }

    @Override
    public ThreadViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.list_item_forum_comment_parent, viewGroup, false);
        return new ThreadViewHolder(view);
    }

    @Override
    public CommentViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.list_item_forum_comment_child, viewGroup, false);
        return new CommentViewHolder(view, mForumThread, mContext);
    }

    @Override
    public void onBindParentViewHolder(ThreadViewHolder parentViewHolder, int i, Object o) {
    }

    @Override
    public void onBindChildViewHolder(CommentViewHolder childViewHolder, final int i, Object o) {
        Log.d("CEA", "position : " + i);
        final ForumComment forumComment = (ForumComment)o;
        Glide.with(mContext).load(forumComment.getImgUrl()).into(childViewHolder.getProfilePictureView());
        childViewHolder.getNameView().setText(forumComment.getPoster());
        childViewHolder.getContentsView().setText(forumComment.getText());
        Date date = new DateTime(forumComment.getDate()).toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm a", Locale.getDefault());
        childViewHolder.getDateView().setText(sdf.format(date));
        Date today = new Date();
        if (today.getTime() - date.getTime() < MILLIS_PER_DAY) {
            childViewHolder.getNewTextView().setVisibility(View.VISIBLE);
        } else {
            childViewHolder.getNewTextView().setVisibility(View.GONE);
        }

        childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = ContentsLab.get().getmLogInData().get(3);
                if (forumComment.getPosterId().equals(id)) {
                    View view = View.inflate(mContext, R.layout.alertdialog_comment, null);
                    Button sendButton = (Button) view.findViewById(R.id.send_button);
                    ImageView commenter = (ImageView) view.findViewById(R.id.comment_poster_profile_image_view);
                    Glide.with(mContext).load(ContentsLab.get().getmLogInData().get(0)).into(commenter);
                    final EditText commentEditText = (EditText) view.findViewById(R.id.comment_edit_text);
                    commentEditText.setText(forumComment.getText());
                    final BottomSheetDialog commentDialog = new BottomSheetDialog(mContext);
                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, String> map = new HashMap<>();
                            map.put("threadID", mForumThread.getId());
                            map.put("arrayPos", i-1 + "");
                            map.put("text", commentEditText.getText().toString());
                            new NetworkingService().putRequestForumThread(mContext, "comment", map, new NetworkingService.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    mFetchThreadsTask.execute();
                                }
                            });
                            commentDialog.dismiss();
                        }
                    });
                    commentDialog.setContentView(view);
                    commentDialog.show();

                } else {
                    Toast.makeText(mContext, "Not your comment", Toast.LENGTH_SHORT).show();
                }
            }
        });

        childViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("threadID", mForumThread.getId());
                map.put("arrayPos", i-1 + "");
                new NetworkingService().deleteRequestForumThread(mContext, "comment", map, new NetworkingService.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        mFetchThreadsTask.execute();
                    }
                });
                Toast.makeText(mContext, "Remove success", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
