package com.example.csdevelop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.csdevelop.fragments.BuscarFragment;
import com.example.csdevelop.fragments.GruposFragment;
import com.example.csdevelop.fragments.InicioFragment;
import com.example.csdevelop.fragments.PerfilFragment;
import com.example.csdevelop.fragments.SocialFragment;
import com.example.csdevelop.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FloatingActionButton btnGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //intent de inicio de sesion recibido
        Intent isR= getIntent();
        btnGrupos=findViewById(R.id.btnGrupos);

        replaceFragment(new InicioFragment());
        binding.bottomNavigation.setBackground(null);

        binding.bottomNavigation.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.inicio:
                    replaceFragment(new InicioFragment());
                    break;
                case R.id.buscar:
                    replaceFragment(new BuscarFragment());
                    break;
                case R.id.social:
                    replaceFragment(new SocialFragment());
                    break;
                case R.id.perfil:
                    replaceFragment(new PerfilFragment());
                    break;
            }

            return true;
        });

        btnGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new GruposFragment());
            }
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}