package nl.rug.www.summerschool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jk on 3/31/17.
 */

public class LecturerFragment extends Fragment {

    private static final String ARG_LECTURER_ID = "lecturer_id";

    private Lecturer mLecturer;
    private TextView mTitle;
    private TextView mDepartment;
    private TextView mDescription;

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
        mLecturer = ContentsLab.get(getActivity()).getLecturer(lecturerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer, container, false);

        mTitle = (TextView)view.findViewById(R.id.lecturer_name_text_view);
        mTitle.setText(mLecturer.getTitle());
        mDepartment = (TextView)view.findViewById(R.id.lecturer_department_text_view);
        mDepartment.setText(mLecturer.getDepartment());
        mDescription = (TextView)view.findViewById(R.id.lecturer_decription_text_view);
        mDescription.setText(mLecturer.getDescription());

        return view;
    }
}
