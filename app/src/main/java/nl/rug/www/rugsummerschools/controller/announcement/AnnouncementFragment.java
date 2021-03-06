package nl.rug.www.rugsummerschools.controller.announcement;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.util.Date;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.databinding.FragmentAnnBinding;
import nl.rug.www.rugsummerschools.model.Announcement;
import nl.rug.www.rugsummerschools.model.ContentsLab;

/**
 * Announcement fragment is to show the details of announcement
 * when any item is clicked on AnnouncementListFragment.
 * This fragment is inflated into AnnouncementPagerActivity
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 13/04/2017
 */

public class AnnouncementFragment extends Fragment {

    private static final String ARG_ANNOUNCEMENT_ID = "announcement_id";
    private FragmentAnnBinding mBinding;
    private Announcement mAnnouncement;

    public static AnnouncementFragment newInstance(String announcementId) {
        Bundle args = new Bundle();
        args.putString(ARG_ANNOUNCEMENT_ID, announcementId);
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        String announcementId = getArguments().getString(ARG_ANNOUNCEMENT_ID);
        mAnnouncement = ContentsLab.get().getAnnouncement(announcementId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_ann, container, false);
        mBinding.setAnnouncement(mAnnouncement);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mBinding.announcementDetail.setText(Html.fromHtml(mAnnouncement.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            mBinding.announcementDetail.setText(Html.fromHtml(mAnnouncement.getDescription()));
        }

        GradientDrawable circle = (GradientDrawable) mBinding.initialTextView.getBackground();
        circle.setColor(mAnnouncement.getColor());
        Date date = new DateTime(mAnnouncement.getDate()).toDate();
        mBinding.dateTextView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        return mBinding.getRoot();
    }
}
