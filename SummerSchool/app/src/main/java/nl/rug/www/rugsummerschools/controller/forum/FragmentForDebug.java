package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import nl.rug.www.rugsummerschools.R;

/**
 * Created by jk on 17. 12. 8.
 */

public class FragmentForDebug extends Fragment {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_logout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out_menu :
                mOnSignOutListener.signOutGoogle();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
