package nl.rug.www.rugsummerschools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.rug.www.rugsummerschools.controller.LoginActivity;

/**
 * Created by jk on 17. 12. 7.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
