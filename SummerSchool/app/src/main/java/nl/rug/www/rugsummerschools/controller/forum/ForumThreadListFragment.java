package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
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
 * Created by jk on 17. 12. 8.
 */

public class ForumThreadListFragment extends ContentsListFragment<ForumThread, ContentHolder<ForumThread>> {

    private OnSignOutListener mOnSignOutListener;

    public interface OnSignOutListener {
        void signOutGoogle();
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
        return new NetworkingService().fetchForumThreads();
    }

    @Override
    protected void update(List<ForumThread> contents) {
        ContentsLab.get().updateForumThreads(contents);
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    @Override
    protected void setupAdatper() {
        if (isAdded()) {
            mBinding.recyclerView.setAdapter(new ContentAdapter<ForumThread, ForumThreadHolder>(mItems, getActivity()) {
                @Override
                protected ForumThreadHolder createHolder(LayoutInflater layoutInflater, ViewGroup parent) {
                    return new ForumThreadHolder(layoutInflater, parent, getActivity()) {
                        @Override
                        public void onClick(View v) {
                            Intent intent = ForumThreadDetailActivity.newIntent(getActivity(), mContent.getId());
                            startActivity(intent);
                        }
                    };
                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forum, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_menu :
                mOnSignOutListener.signOutGoogle();
                return super.onOptionsItemSelected(item);
            case R.id.write_forum_thread :
                Intent intent = new Intent(getActivity(), ThreadActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
