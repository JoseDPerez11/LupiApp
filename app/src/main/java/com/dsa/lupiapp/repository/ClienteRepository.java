package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ClienteApi;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepository {

    private static ClienteRepository repository;
    private final ClienteApi clienteApi;

    public static ClienteRepository getInstance() {
        if (repository == null) {
            repository = new ClienteRepository();
        }
        return repository;
    }

    private ClienteRepository() {
        clienteApi = ConfigApi.getClienteApi();
    }

    public LiveData<GenericResponse<Cliente>> save(Cliente cliente) {
        final MutableLiveData<GenericResponse<Cliente>> mutableLiveData = new MutableLiveData<>();
        this.clienteApi.guardarCliente(cliente).enqueue(new Callback<GenericResponse<Cliente>>() {
            @Override
            public void onResponse(Call<GenericResponse<Cliente>> call, Response<GenericResponse<Cliente>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Cliente>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mutableLiveData;
    }

}
