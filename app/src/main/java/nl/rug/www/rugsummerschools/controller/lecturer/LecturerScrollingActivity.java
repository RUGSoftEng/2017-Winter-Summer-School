package nl.rug.www.rugsummerschools.controller.lecturer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.net.URL;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.Lecturer;

/**
 * This is scrolling activity for lecturer.
 * It contains information of a lecturer.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 10/02/2018
 */

public class LecturerScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LecturerSclActivity";

    protected static final String EXTRA_CONTENT_ID =
            "nl.rug.www.rugsummerschool.content_id";

    protected Lecturer mLecturer;

    public static Intent newIntent(Context context, String contentId) {
        Intent intent = new Intent(context, LecturerScrollingActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, contentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        String contentId = (String) getIntent().getSerializableExtra(EXTRA_CONTENT_ID);
        mLecturer = ContentsLab.get().getLecturer(contentId);
        TextView detailTextView = findViewById(R.id.contents);
        ImageView backgroudView = findViewById(R.id.background_app_bar);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mLecturer.getTitle());
        Glide.with(this).load(mLecturer.getImgurl()).into(backgroudView);
        backgroudView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.LIGHTEN);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            detailTextView.setText(Html.fromHtml(mLecturer.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            detailTextView.setText(Html.fromHtml(mLecturer.getDescription()));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String url = mLecturer.getWebsite();
        if (!url.matches("^(http|https)://.*"))
            url = "https://" + url;
        try {
            Uri website = Uri.parse(url);
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, website);
            startActivity(websiteIntent);
        } catch (Exception e) {
            Toast.makeText(LecturerScrollingActivity.this, "Failed to visit website", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
