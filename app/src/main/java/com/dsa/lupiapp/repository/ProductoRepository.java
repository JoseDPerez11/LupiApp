package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.api.ProductoApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoRepository {

    private final ProductoApi productoApi;
    private static ProductoRepository productoRepository;

    public ProductoRepository() {
        this.productoApi = ConfigApi.getProductoApi();
    }

    public static ProductoRepository getInstance() {
        if (productoRepository == null) {
            productoRepository = new ProductoRepository();
        }
        return productoRepository;
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados() {
        final MutableLiveData<GenericResponse<List<Producto>>> mutableLiveData = new MutableLiveData<>();
        this.productoApi.listarProductosRecomendados().enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC) {
        final MutableLiveData<GenericResponse<List<Producto>>> mutableLiveData = new MutableLiveData<>();
        this.productoApi.listarProductosPorCategoria(idC).enqueue(new Callback<GenericResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }

}
