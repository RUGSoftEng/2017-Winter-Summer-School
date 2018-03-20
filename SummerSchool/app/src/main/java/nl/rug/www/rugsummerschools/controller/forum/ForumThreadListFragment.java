package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentAdapter;
import nl.rug.www.rugsummerschools.controller.ContentHolder;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.controller.ContentsListFragment;
import nl.rug.www.rugsummerschools.model.ForumThread;
import nl.rug.www.rugsummerschools.networking.NetworkingService;

/**
 * This is list fragment of forum threads that inflates main activity.
 * This fragment shows list of forum threads and the number of comments.
 * It contains functionality for thread addition and log out.
 *
 * @author Jeongkyun Oh
 * @since 08/12/2017
 * @version 2.0.0
 */


public class ForumThreadListFragment extends ContentsListFragment<ForumThread, ContentHolder<ForumThread>> {

    private static final String TAG = "ThreadListFragment";

    private OnSignOutListener mOnSignOutListener;

    public interface OnSignOutListener {
        void signOut();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnSignOutListener = (OnSignOutListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement OnSignOutListener");
        }
    }

    @Override
    protected void bindViews() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @StringRes
    @Override
    protected int getSectionStringId() {
        return R.string.forum;
    }

    @Override
    protected List<ForumThread> fetchContents() {
        return new NetworkingService<ForumThread>().fetchData(NetworkingService.FORUM_THREAD, null);
    }

    @Override
    protected void update(List<ForumThread> contents) {
        ContentsLab.get().updateForumThreads(contents);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(newAdapter(mItems, getActivity()));
        }
    }

    private ContentAdapter<ForumThread, ForumThreadHolder> newAdapter(List<ForumThread> items, final Context context) {
        return new ContentAdapter<ForumThread, ForumThreadHolder>(items, context) {
            @Override
            protected ForumThreadHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                return newHolder(layoutInflater, parent, context);
            }
        };
    }

    private ForumThreadHolder newHolder(LayoutInflater inflater, ViewGroup parent, Context context) {
        return new ForumThreadHolder(inflater, parent, context) {
            @Override
            public void onClick(View view) {
                Intent intent = ForumThreadDetailActivity.newIntent(getActivity(), mContent.getId());
                startActivity(intent);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forum, menu);
        Log.i(TAG, "Options menu is inflated.");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_menu :
                mOnSignOutListener.signOut();
                return true;
            case R.id.write_forum_thread :
                Intent intent = new Intent(getActivity(), ThreadActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
