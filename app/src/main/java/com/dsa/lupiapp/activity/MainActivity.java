package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ActivityMainBinding;
import com.dsa.lupiapp.databinding.CustomToastErrorBinding;
import com.dsa.lupiapp.databinding.CustomToastOkBinding;
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
        this.registerUser();
    }

    private void initViewModel() {
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {

        // Se establece un listener para el evento de clic en el botón de inicio de sesión
        binding.btnIniciarSesion.setOnClickListener(viewModel -> {
            try {
                if (validar()) {
                    // Se obtienen los valores ingresados por el usuario en los campos de correo y contraseña
                    String email = binding.edtMail.getText().toString();
                    String password = binding.edtPassword.getText().toString();

                    // Se realiza el intento de inicio de sesión a través del ViewModel asociado al usuario
                    usuarioViewModel.login(email, password).observe(this, response -> {

                        // Se verifica la respuesta obtenida del intento de inicio de sesión
                        if (response.getRpta() == 1) {
                            // Si la respuesta es exitosa (rpta = 1), se muestra un mensaje y se realiza el siguiente conjunto de acciones
                            toastCorrecto(response.getMessage());

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
                            toastIncorrecto("Credenciales inválidas");
                        }
                    });

                } else {
                    toastIncorrecto("Por favor, complete todos los campos");
                }
            } catch (Exception e) {
                toastIncorrecto("Se ha producido un error al intentar loguearte " + e.getMessage());
            }

        });

        this.textChangedListener();

    }

    private void registerUser() {
        binding.txtNuevoUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrarUsuarioActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
    }

    private void textChangedListener() {
        binding.edtMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtInputUsuario.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.txtInputPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }


    public void toastCorrecto(String message) {

        CustomToastOkBinding bindingToast = CustomToastOkBinding.inflate(getLayoutInflater());
        bindingToast.txtMensajeToastOk.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(bindingToast.getRoot());
        toast.show();

    }

    public void toastIncorrecto(String message) {

        CustomToastErrorBinding bindingToastErrpr = CustomToastErrorBinding.inflate(getLayoutInflater());
        bindingToastErrpr.txtMensajeToastError.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(bindingToastErrpr.getRoot());
        toast.show();
    }

    private boolean validar() {
        boolean valorRetorno = true;
        String usuario = binding.edtMail.getText().toString();
        String password = binding.edtPassword.getText().toString();

        if (usuario.isEmpty()) {
            binding.txtInputUsuario.setError("Ingrese su usuario y/o correo electrónico");
            valorRetorno = false;
        } else { binding.txtInputUsuario.setErrorEnabled(false); }

        if (password.isEmpty()) {
            binding.txtInputPassword.setError("Ingrese su contraseña");
            valorRetorno = false;
        } else { binding.txtInputPassword.setErrorEnabled(false); }

        return valorRetorno;
    }

}