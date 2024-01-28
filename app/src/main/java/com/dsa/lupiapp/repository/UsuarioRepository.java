package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.api.UsuarioApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {

    private static UsuarioRepository repository;
    private final UsuarioApi usuarioApi;

    public UsuarioRepository() {
        this.usuarioApi = ConfigApi.getUsuarioApi();
    }

    public static UsuarioRepository getInstance() {
        if (repository == null) {
            repository = new UsuarioRepository();
        }
        return repository;
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String contrasenia) {

        // Se crea un MutableLiveData que contendrá la respuesta del servicio web.
        final MutableLiveData<GenericResponse<Usuario>> mutableLiveData = new MutableLiveData<>();

        // Se realiza una llamada asincrónica utilizando Retrofit.
        this.usuarioApi.login(email, contrasenia).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                // En caso de respuesta exitosa, se establece el valor del MutableLiveData con el cuerpo de la respuesta.
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                // En caso de fallo, se establece el valor del MutableLiveData con un nuevo objeto GenericResponse vacío.
                mutableLiveData.setValue(new GenericResponse());

                // Se imprime un mensaje de error en la consola y se imprime la traza del error.
                System.out.println("Se ha producido un error al iniciar sesión: " + t.getMessage());
                t.printStackTrace();
            }
        });

        // Se retorna el MutableLiveData que eventualmente contendrá la respuesta del servicio web.
        return mutableLiveData;
    }

    // La función devuelve LiveData, que es un contenedor observable para los datos.
    public LiveData<GenericResponse<Usuario>> save(Usuario usuario) {
        // MutableLiveData es una implementación de LiveData que permite modificar sus datos.
        final MutableLiveData<GenericResponse<Usuario>> mutableLiveData = new MutableLiveData<>();

        // Se realiza una solicitud a través de usuarioApi.save para guardar el usuario.
        this.usuarioApi.guardarUsuario(usuario).enqueue(new Callback<GenericResponse<Usuario>>() {
            @Override
            public void onResponse(Call<GenericResponse<Usuario>> call, Response<GenericResponse<Usuario>> response) {
                // Se establece el valor del MutableLiveData con la respuesta del servidor.
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Usuario>> call, Throwable t) {
                // Se establece un GenericResponse vacío en caso de error.
                mutableLiveData.setValue(new GenericResponse<>());

                // Se imprime un mensaje de error y se muestra el rastreo de la pila.
                System.out.println("Se ha producido un error: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mutableLiveData;
    }

}
