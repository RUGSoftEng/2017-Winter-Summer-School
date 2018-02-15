package nl.rug.www.rugsummerschools.controller.generalinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import nl.rug.www.rugsummerschools.R;
import nl.rug.www.rugsummerschools.model.ContentsLab;
import nl.rug.www.rugsummerschools.model.GeneralInfo;

/**
 * This is scrolling activity for general information.
 * It contains information of a general information.
 *
 * @author Jeongkyun Oh
 * @version 2.0.0
 * @since 10/02/2018
 */

public class GeneralInfoScrollingActivity extends AppCompatActivity {

    protected static final String EXTRA_CONTENT_ID =
            "nl.rug.www.rugsummerschool.content_id";

    protected GeneralInfo mGeneralInfo;

    public static Intent newIntent(Context context, String contentId) {
        Intent intent = new Intent(context, GeneralInfoScrollingActivity.class);
        intent.putExtra(EXTRA_CONTENT_ID, contentId);
        return intent;
    }

    private void inflateDrawable(String category, ImageView view) {
        if ("Food".equals(category)) {
            Glide.with(this).load(R.drawable.bg_food).into(view);
        } else if ("Location".equals(category)) {
            Glide.with(this).load(R.drawable.bg_map).into(view);
        } else if ("Internet".equals(category)) {
            Glide.with(this).load(R.drawable.bg_internet).into(view);
        } else if ("Accommodation".equals(category)) {
            Glide.with(this).load(R.drawable.bg_accomodation).into(view);
        } else {
            Glide.with(this).load(R.drawable.bg_info).into(view);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        String contentId = (String) getIntent().getSerializableExtra(EXTRA_CONTENT_ID);
        mGeneralInfo = ContentsLab.get().getGeneralInfo(contentId);
        TextView detailTextView = findViewById(R.id.contents);
        ImageView backgroudView = findViewById(R.id.background_app_bar);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mGeneralInfo.getTitle());
        String category = mGeneralInfo.getCategory();
        inflateDrawable(category, backgroudView);
        backgroudView.setColorFilter(Color.DKGRAY, PorterDuff.Mode.LIGHTEN);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            detailTextView.setText(Html.fromHtml(mGeneralInfo.getDescription(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            detailTextView.setText(Html.fromHtml(mGeneralInfo.getDescription()));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }
}
