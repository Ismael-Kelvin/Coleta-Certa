package com.example.coletacerta.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.coletacerta.fragmentos.UsuariosFragment;


import com.example.coletacerta.fragmentos.HomeFragment;
import com.example.coletacerta.R;
import com.example.coletacerta.fragmentos.RegisterFragment;
import com.example.coletacerta.fragmentos.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btt_logout;
    private BottomNavigationView bottom_navigation;
    private DrawerLayout drawer_layout;
    private NavigationView navigation_view;
    private Toolbar toolbar;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        drawer_layout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        navigation_view.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            if (id == R.id.menu_acompanhar_coleta) {

            } else if (id == R.id.menu_tipo_resido) {

            } else if (id == R.id.menu_edcucacao) {

            } else if (id == R.id.menu_duvidas){

            } else if (id == R.id.menu_contatos){

            } else if (id == R.id.menu_perfil){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new UsuariosFragment())
                        .commit();
            } else if (id == R.id.menu_sair){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }

            drawer_layout.closeDrawer(GravityCompat.START);
            return true;
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }


        bottom_navigation = findViewById(R.id.bottom_navigation);
        //cb_mostrar_senha.setButtonTintList(ContextCompat.getColorStateList(this, R.color.cbx_color_login));
        bottom_navigation.setItemActiveIndicatorColor(ContextCompat.getColorStateList(this, R.color.nav_item_selected_background));
        bottom_navigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_register) {
                selectedFragment = new RegisterFragment();
            } else if (itemId == R.id.nav_requests) {
                selectedFragment = new RequestsFragment();
            } else if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });



    }

    private void abrir_tela_login() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        if(current_user == null){
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

}