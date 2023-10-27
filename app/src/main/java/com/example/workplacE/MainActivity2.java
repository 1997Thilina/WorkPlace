package com.example.workplacE;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {


    private Toolbar toolbar;
    private NavigationView nav_view;
    private DrawerLayout drawerLayout;

    private TextView head_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        toolbar = findViewById(R.id.toolbar);
        nav_view= findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity2.this,drawerLayout,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //head_text = head_text.getHeader


    }


}