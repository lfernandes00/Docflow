package com.example.docflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("id", String.valueOf(LoggedUser.id));
        Log.d("token", LoggedUser.token);
        
        changeStatusBarColor();
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setItemIconTintList(null);

        // Evento no clique de cada menu item
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item_home:
                                Log.d("MENU_DEBUG", "onNavigationItemSelected: " + "Profile");
                                openFragment(HomePageFragment.newInstance("", ""));
                                return true;
                            case R.id.menu_item_leaderboard:
                                Log.d("MENU_DEBUG", "onNavigationItemSelected: " + "Payments");
                                openFragment(LeaderboardFragment.newInstance("", ""));
                                return true;
                            case R.id.menu_item_upload:
                                Log.d("MENU_DEBUG", "onNavigationItemSelected: " + "Saved");
                                openFragment(UploadFragment.newInstance("", ""));
                                return true;
                            case R.id.menu_item_request:
                                Log.d("MENU_DEBUG", "onNavigationItemSelected: " + "Saved");
                                openFragment(RequestFragment.newInstance("", ""));
                                return true;
                            case R.id.menu_item_profile:
                                Log.d("MENU_DEBUG", "onNavigationItemSelected: " + "Saved");
                                openFragment(ProfileFragment.newInstance("", ""));
                                return true;
                        }
                        return false;
                    }
                });

        // Abre o fragmento HomePage por omissão
        openFragment(HomePageFragment.newInstance("", ""));
    }

    // Método utilitário para abrir um fragmento
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideActionBar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void hideActionBar() {
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @SuppressLint("ResourceAsColor")
    public void changeStatusBarColor() {
        getWindow().setStatusBarColor(R.color.black);
    }
}