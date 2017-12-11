package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URL;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.controller.ContentsLab;
import nl.rug.www.rugsummerschools.model.Lecturer;

/**
 * Lecturer fragment is to show the details of lecturer information
 * when any item is clicked on LecturerListFragment.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

@Deprecated
public class LecturerFragment extends Fragment {

    private static final String ARG_LECTURER_ID = "lecturer_id";

    private Lecturer mLecturer;

    public static LecturerFragment newInstance(String lecturerId) {
        Bundle args = new Bundle();
        args.putString(ARG_LECTURER_ID, lecturerId);

        LecturerFragment fragment = new LecturerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String lecturerId = getArguments().getString(ARG_LECTURER_ID);
        mLecturer = ContentsLab.get().getLecturer(lecturerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer, container, false);

        TextView mTitle = (TextView)view.findViewById(R.id.lecturer_name_text_view);
        mTitle.setText(mLecturer.getTitle());
        TextView mDescription = (TextView)view.findViewById(R.id.lecturer_decription_text_view);
        mDescription.setText(mLecturer.getDescription());
        TextView mWebSiteTextView = (TextView)view.findViewById(R.id.lecturer_website_text_view);
        mWebSiteTextView.setText(mLecturer.getWebsite());
        mWebSiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ImageView mLecturerImageView = (ImageView)view.findViewById(R.id.lecturer_image_view);
        String url = mLecturer.getImgurl();
        if (!url.equals(""))
            Glide.with(getActivity()).load(mLecturer.getImgurl()).into(mLecturerImageView);

        return view;
    }
}
