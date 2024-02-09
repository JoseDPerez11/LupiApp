package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.CategoriaApi;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Categoria;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriaRepository {

    private final CategoriaApi categoriaApi;
    private static CategoriaRepository categoriaRepository;

    public CategoriaRepository() {
        this.categoriaApi = ConfigApi.getCategoriaApi();
    }

    public static CategoriaRepository getInstance() {
        if (categoriaRepository == null) {
            categoriaRepository = new CategoriaRepository();
        }
        return categoriaRepository;
    }

    public LiveData<GenericResponse<List<Categoria>>> listarCategoriasActivas() {
        final MutableLiveData<GenericResponse<List<Categoria>>> mutableLiveData = new MutableLiveData<>();
        this.categoriaApi.listarCategoriasActivas().enqueue(new Callback<GenericResponse<List<Categoria>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Categoria>>> call, Response<GenericResponse<List<Categoria>>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Categoria>>> call, Throwable t) {
                System.out.println("Error al obtener las categorias: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mutableLiveData;
    }

}
