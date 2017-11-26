package nl.rug.www.rugsummerschools.controller.forum;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bumptech.glide.Glide;
import com.dgreenhalgh.android.simpleitemdecoration.linear.DividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.ForumComment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

import static org.joda.time.DateTimeConstants.MILLIS_PER_DAY;

/**
 * This is main forum fragment class that shows all of the forum threads and comments.
 *
 * @since 05/06/2017
 * @author Jeongkyun Oh
 */

public class ForumFragment extends Fragment {

    public static final int INT_ADD = 0;
    public static final int INT_EDIT = 1;

    protected AppCompatActivity mActivity;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed in
                    FragmentManager fm = mActivity.getSupportFragmentManager();
                    if(!mActivity.isFinishing())
                        fm.beginTransaction().replace(R.id.fragment_forum_container, new ForumLoginFragment()).commitAllowingStateLoss();
                }
            }
        });
        if (user == null) {
            FragmentManager fm = mActivity.getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_forum_container, new ForumLoginFragment()).commit();
        }
        //get log in data
        String UID = ContentsLab.get().getmLogInData().get(3);
        Log.d("FORUMFRAG", "" + UID);

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
        mForumRecyclerView.addItemDecoration(new DividerItemDecoration(
                ContextCompat.getDrawable(getActivity(), R.drawable.divider_horizontal)
        ));

        setupAdapter();
        view.findViewById(R.id.forum_add_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ThreadActivity.class);
                intent.putExtra(ThreadActivity.ARG_ADD_OR_EDIT, INT_ADD);
                startActivityForResult(intent, INT_ADD);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == INT_ADD) {
            new FetchThreadsTask().execute();
        } else if (requestCode == INT_EDIT) {
            new FetchThreadsTask().execute();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            mForumRecyclerView.setAdapter(new ForumAdapter(mItems));
        }
    }

    private class ForumHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        private ForumThread mForumThread;
        private ImageView mPosterImageView;
        private TextView mPosterTextView;
        private TextView mPostedDateTextView;
        private TextView mPostedTimeTextView;
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mNewTextView;
        private RecyclerView mCommentsRecyclerView;
        private CommentExpandableAdapter mCommentExpandableAdapter;

        private ForumHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_forum_thread, parent, false));

            mPosterImageView = (ImageView)itemView.findViewById(R.id.forum_poster_profile_picture);
            mPosterTextView = (TextView)itemView.findViewById(R.id.forum_poster_text_view);
            mPostedDateTextView = (TextView)itemView.findViewById(R.id.date);
            mPostedTimeTextView = (TextView)itemView.findViewById(R.id.time);
            mNewTextView = (TextView)itemView.findViewById(R.id.new_image_view);
            mTitleTextView = (TextView)itemView.findViewById(R.id.forum_thread_title_text_view);
            mDescriptionTextView = (TextView)itemView.findViewById(R.id.forum_thread_description_text_view);
            mCommentsRecyclerView = (RecyclerView)itemView.findViewById(R.id.forum_comments_recycler_view);
            mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCommentsRecyclerView.addItemDecoration(new DividerItemDecoration(
                    ContextCompat.getDrawable(getActivity(), R.drawable.divider_horizontal)));
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        private void setAdapter() {
            if (isAdded()) {
                mCommentExpandableAdapter = new CommentExpandableAdapter(getActivity(), generateComments(), mForumThread, new FetchThreadsTask());
                mCommentExpandableAdapter.setCustomParentAnimationViewId(R.id.expandable_arrow);
                mCommentExpandableAdapter.setParentClickableViewAnimationDuration(ExpandableRecyclerAdapter.DEFAULT_ROTATE_DURATION_MS);
                mCommentExpandableAdapter.setParentAndIconExpandOnClick(true);
                mCommentsRecyclerView.setAdapter(mCommentExpandableAdapter);
            }
        }

        private void bind(ForumThread forumThread) {
            mForumThread = forumThread;
            Glide.with(getActivity()).load(mForumThread.getImgUrl()).into(mPosterImageView);
            mPosterTextView.setText(mForumThread.getPoster());
            Date date = new DateTime(mForumThread.getDate()).toDate();
            SimpleDateFormat parseDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat parseTime = new SimpleDateFormat("HH:mm a", Locale.getDefault());
            mPostedDateTextView.setText(parseDate.format(date));
            mPostedTimeTextView.setText(parseTime.format(date));
            mTitleTextView.setText(mForumThread.getTitle());
            mDescriptionTextView.setText(mForumThread.getDescription());
            List<ForumComment> comments = mForumThread.getForumCommentList();
            if (comments != null) setAdapter();

            Date today = new Date();
            if (today.getTime() - date.getTime() < MILLIS_PER_DAY) {
                mNewTextView.setVisibility(View.VISIBLE);
            } else {
                mNewTextView.setVisibility(View.GONE);
            }
        }

        private List<ParentObject> generateComments() {
            List<ParentObject> parentObjects = new ArrayList<>();
            parentObjects.add(mForumThread);
            return parentObjects;
        }

        @Override
        public void onClick(View v) {
            View view = View.inflate(getActivity(), R.layout.alertdialog_comment, null);
            Button sendButton = (Button) view.findViewById(R.id.send_button);
            ImageView commentorPic = (ImageView)view.findViewById(R.id.commentor_pic);
            Glide.with(getActivity()).load(ContentsLab.get().getmLogInData().get(0)).into(commentorPic);
            final EditText commentEditText = (EditText) view.findViewById(R.id.comment_edit_text);
            final BottomSheetDialog commentDialog = new BottomSheetDialog(getActivity());
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String imgurl = ContentsLab.get().getmLogInData().get(0);
                    String name = ContentsLab.get().getmLogInData().get(1);
                    String uid = ContentsLab.get().getmLogInData().get(3);
                    Map<String, String> map = new HashMap<>();
                    map.put("threadID", mForumThread.getId());
                    map.put("commentID", UUID.randomUUID().toString());
                    map.put("author", name);
                    map.put("posterID", uid);
                    map.put("text", commentEditText.getText().toString());
                    map.put("imgurl", imgurl);
                    new NetworkingService().postRequestForumThread(getActivity(), "comment", map, new NetworkingService.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            new FetchThreadsTask().execute();
                        }

                        @Override
                        public void onFail(String result) {
                            Toast.makeText(getActivity(), "It fails to post your forum thread.\nPlease try again.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String result) {
                            Toast.makeText(getActivity(), "Error:" + result, Toast.LENGTH_SHORT).show();
                        }
                    });
                    commentDialog.dismiss();
                }
            });
            commentDialog.setContentView(view);
            commentDialog.show();
        }

        @Override
        public boolean onLongClick(View v) {
            String id = ContentsLab.get().getmLogInData().get(3);
            if(mForumThread.getPosterId().equals(id)) {
                View view = View.inflate(getActivity(), R.layout.alertdialog_edit_delete, null);
                final BottomSheetDialog editDeleteDialog = new BottomSheetDialog(getActivity());
                editDeleteDialog.setContentView(view);
                ImageView edit = (ImageView)view.findViewById(R.id.edit_image_view);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ThreadActivity.class);
                        String[] data = {mForumThread.getId(), mForumThread.getTitle(), mForumThread.getDescription()};
                        intent.putExtra(ThreadActivity.ARG_ADD_OR_EDIT, INT_EDIT);
                        intent.putExtra(ThreadActivity.ARG_EDITABLE_DATA, data);
                        startActivityForResult(intent, INT_EDIT);
                        editDeleteDialog.dismiss();
                    }
                });
                ImageView delete = (ImageView)view.findViewById(R.id.delete_image_view);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                                .setTitle("Remove Thread")
                                .setMessage("Are you sure?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("threadID", mForumThread.getId());
                                        new NetworkingService().deleteRequestForumThread(getActivity(), "thread", map, new NetworkingService.VolleyCallback() {
                                            @Override
                                            public void onSuccess(String result) {
                                                new FetchThreadsTask().execute();
                                            }

                                            @Override
                                            public void onFail(String result) {
                                                Toast.makeText(getActivity(), "It fails to delete your forum thread.\nPlease try again.", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(String result) {
                                                Toast.makeText(getActivity(), "Error:"+result, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create();

                        alertDialog.show();

                        editDeleteDialog.dismiss();
                    }
                });
                editDeleteDialog.show();
            } else {
                Toast.makeText(getActivity(), "This is not your post", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    private class ForumAdapter extends RecyclerView.Adapter<ForumHolder> {

        private List<ForumThread> mForumThreads;

        private ForumAdapter(List<ForumThread> forumThreads) {
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

    public class FetchThreadsTask extends AsyncTask<Void, Void, List<ForumThread>> {

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
        protected void onPostExecute(List<ForumThread> forumThreads) {
            mItems = forumThreads;
            setupAdapter();
            ContentsLab.get().updateForumThreads(mItems);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
