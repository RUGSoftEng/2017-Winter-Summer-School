package nl.rug.www.rugsummerschool.controller.myprofile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.rug.www.rugsummerschool.R;

/**
 * This class is a container fragment of SignInFragment and MyProfileFragment.
 * By using nested fragments, it is possible to transact fragments on the viewpager.
 * If an user logged in, it inflates MyProfileFragment.
 * Otherwise, SignInFragment is inflated.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class RootFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment, container, false);

        SignInFragment signInFragment = new SignInFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, signInFragment)
                .commit();

        return view;
    }
}
