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
        // Se establece un listener para el evento de clic en el botón de inicio de sesión
        binding.btnIniciarSesion.setOnClickListener(viewModel -> {
            // Se obtienen los valores ingresados por el usuario en los campos de correo y contraseña
            String email = binding.edtMail.getText().toString();
            String password = binding.edtPassword.getText().toString();

            // Se realiza el intento de inicio de sesión a través del ViewModel asociado al usuario
            usuarioViewModel.login(email, password).observe(this, response -> {

                // Se verifica la respuesta obtenida del intento de inicio de sesión
                if (response.getRpta() == 1) {
                    // Si la respuesta es exitosa (rpta = 1), se muestra un mensaje y se realiza el siguiente conjunto de acciones
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

                    // Se obtiene el objeto Usuario desde la respuesta
                    Usuario usuario = response.getBody();

                    // Se guarda el objeto Usuario en SharedPreferences para persistencia entre sesiones
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Se utiliza Gson para convertir el objeto Usuario a formato JSON y guardarlo en SharedPreferences
                    final Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    editor.putString("UsuarioJson", gson.toJson(usuario, new TypeToken<Usuario>(){

                    }.getType()));

                    editor.apply();

                    // Se limpian los campos de correo y contraseña
                    binding.edtMail.setText("");
                    binding.edtPassword.setText("");

                    // Se inicia una nueva actividad (InicioActivity) después de un inicio de sesión exitoso
                    startActivity(new Intent(this, InicioActivity.class));

                } else {
                    // Si la respuesta no es exitosa, se muestra un mensaje de error
                    Toast.makeText(this, "Ocurrió un error " + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}