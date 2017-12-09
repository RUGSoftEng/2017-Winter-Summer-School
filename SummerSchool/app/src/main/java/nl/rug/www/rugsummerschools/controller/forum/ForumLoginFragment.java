package nl.rug.www.rugsummerschools.controller.forum;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;

import nl.rug.www.rugsummerschools.R;

/**
 * Created by RavenSP on 30/5/2017.
 */

public class ForumLoginFragment extends Fragment implements View.OnClickListener {

    public OnSignInListener mOnSignInListener;

    public interface OnSignInListener {
        void signInWithGoogle();
        void signInWithFacebook();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnSignInListener = (OnSignInListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement OnSignInListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_login, container, false);
        SignInButton googleSignInButton = view.findViewById(R.id.google_login_button);
        googleSignInButton.setOnClickListener(this);
        Button facebookSignInButton = view.findViewById(R.id.facebook_login_button);
        facebookSignInButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_login_button:
                mOnSignInListener.signInWithGoogle();
                break;
            case R.id.facebook_login_button :
                mOnSignInListener.signInWithFacebook();
                break;
        }
    }
}
