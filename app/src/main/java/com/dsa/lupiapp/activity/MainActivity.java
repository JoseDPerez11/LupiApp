package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.dsa.lupiapp.databinding.ActivityMainBinding;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.dsa.lupiapp.viewmodel.UsuarioViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.initViewModel();
        this.init();
    }

    private void initViewModel() {
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {
        binding.btnIniciarSesion.setOnClickListener(viewModel -> {
            usuarioViewModel.login(binding.edtMail.getText().toString(), binding.edtPassword.getText().toString()).observe(this, response -> {
                if (response.getRpta() == 1) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                    Usuario usuario = response.getBody();
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    final Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    editor.putString("UsuarioJson", gson.toJson(usuario, new TypeToken<Usuario>(){

                    }.getType()));

                    editor.apply();
                    binding.edtMail.setText("");
                    binding.edtPassword.setText("");

                    startActivity(new Intent(this, InicioActivity.class));

                } else {
                    Toast.makeText(this, "Ocurrió un error " + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}