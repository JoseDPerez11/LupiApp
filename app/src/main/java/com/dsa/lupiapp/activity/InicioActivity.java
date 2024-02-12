package com.dsa.lupiapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.dsa.lupiapp.databinding.ActivityInicioBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.sql.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class InicioActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityInicioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInicioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarInicio.toolbar);
        binding.appBarInicio.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_mis_compras, R.id.nav_configuracion)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }

    // Método que maneja los eventos cuando se selecciona un elemento del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Verifica si el ID del elemento del menú seleccionado es igual al ID del elemento "cerrarSesion"
        if (item.getItemId() == R.id.cerrarSesion) {
            // Si es así, llama al método logout() para cerrar la sesión del usuario
            this.logout();
            // Indica que el evento de selección del menú ha sido manejado correctamente
            return true;
        }

        // Si el elemento del menú seleccionado no es "cerrarSesion",
        // delega el manejo del evento al método onOptionsItemSelected de la clase base
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    // Método para cargar datos del usuario desde SharedPreferences y mostrarlos en la vista
    private void loadData() {

        // Obtener las preferencias compartidas
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Crear un objeto Gson para serializar y deserializar objetos JSON
        final Gson gson = new GsonBuilder()
                // Registrar adaptadores personalizados para serializar/deserializar objetos Date y Time
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        // Obtener el JSON del usuario almacenado en SharedPreferences
        String usuarioJson = sharedPreferences.getString("UsuarioJson", null);

        // Verificar si el JSON del usuario no es nulo
        if (usuarioJson != null) {
            // Deserializar el JSON del usuario en un objeto Usuario utilizando Gson
            final Usuario usuario = gson.fromJson(usuarioJson, Usuario.class);

            // Obtener la vista del encabezado del NavigationView
            final View viewHeader = binding.navView.getHeaderView(0);

            // Obtener referencias a los TextView y CircleImageView dentro del encabezado
            final TextView tvNombre = viewHeader.findViewById(R.id.tvNombre);
            final TextView tvCorreo =  viewHeader.findViewById(R.id.tvCorreo);
            final CircleImageView imgFoto = viewHeader.findViewById(R.id.imageView);

            // Establecer el nombre y correo del usuario en los TextView correspondientes
            tvNombre.setText(usuario.getCliente().getNombreCompleto());
            tvCorreo.setText(usuario.getEmail());

            // Construir la URL de la imagen del usuario
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + usuario.getCliente().getFoto().getFileName();

            // Crear un objeto Picasso para cargar la imagen desde la URL
            final Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            // Cargar la imagen desde la URL utilizando Picasso y establecer una imagen de error en caso de fallo
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(imgFoto);
        }
    }

    // Método para cerrar sesión del usuario
    private void logout() {
        // Obtener las preferencias compartidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Crear un editor de preferencias para realizar cambios
        SharedPreferences.Editor editor = preferences.edit();

        // Eliminar la clave "UsuarioJson" de las preferencias compartidas, lo que equivale a cerrar sesión
        editor.remove("UsuarioJson");
        // Aplicar los cambios al editor de preferencias
        editor.apply();
        // Finalizar la actividad actual
        this.finish();
        // Aplicar una animación de transición al cerrar la actividad
        this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_inicio);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}