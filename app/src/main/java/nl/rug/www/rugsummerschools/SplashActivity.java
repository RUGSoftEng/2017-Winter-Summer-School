package nl.rug.www.rugsummerschools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import nl.rug.www.rugsummerschools.controller.LoginActivity;

/**
 * Splash activity with University of Groningen logo.
 *
 * @author Jeongkyun Oh
 * @since 07/12/17
 * @version 2.0.0
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
