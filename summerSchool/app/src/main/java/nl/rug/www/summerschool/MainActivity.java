package nl.rug.www.summerschool;

import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import layout.AnnouncementFragment;
import layout.ForumFragment;
import layout.GeneralInformationFragment;
import layout.ProfileFragment;
import layout.SchoolInformationFragment;
import layout.TimeTableFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton announcementButton = (ImageButton)findViewById(R.id.announcementButton);
        final ImageButton generalinformationButton = (ImageButton)findViewById(R.id.generalinformationButton);
        final ImageButton schoolinformationButton = (ImageButton)findViewById(R.id.schoolinformationButton);
        final ImageButton timetableButton = (ImageButton)findViewById(R.id.timetableButton);
        final ImageButton forumButton = (ImageButton)findViewById(R.id.forumButton);
        final ImageButton profileButton = (ImageButton)findViewById(R.id.profileButton);
        final ScrollView mainFragment = (ScrollView)findViewById(R.id.default_mainFragment);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/rugfont.ttf");
        TextView mainWelcomeText = (TextView) findViewById(R.id.main_welcome);
        mainWelcomeText.setTypeface(typeface);
        TextView mainContentsText = (TextView) findViewById(R.id.main_contents);
        mainContentsText.setTypeface(typeface);

        ImageView poster = (ImageView) findViewById(R.id.poster);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.x * 1023 / 745;

        poster.getLayoutParams().width = width;
        poster.getLayoutParams().height = height;

        announcementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new AnnouncementFragment());
                fragmentTransaction.commit();
            }
        });

        generalinformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new GeneralInformationFragment());
                fragmentTransaction.commit();
            }
        });

        schoolinformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new SchoolInformationFragment());
                fragmentTransaction.commit();
            }
        });

        timetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new TimeTableFragment());
                fragmentTransaction.commit();
            }
        });

        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new ForumFragment());
                fragmentTransaction.commit();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainFragment.setVisibility(View.GONE);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.next_mainFragment, new ProfileFragment());
                fragmentTransaction.commit();
            }
        });

    }
}
