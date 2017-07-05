package nl.rug.www.rugsummerschools.controller.forum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.rug.www.rugsummerschools.R;

/**
 * Created by RavenSP on 30/5/2017.
 */

public class ForumRootFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_forum_fragment, container, false);
        Log.d("ForumRootFragment", "Called");
        ForumLoginFragment forumLoginFragment = new ForumLoginFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_forum_container, forumLoginFragment)
                .commit();
        return view;
    }
}
