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

    // Este método devuelve un LiveData que contiene una respuesta genérica que es una lista de productos.
    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados() {
        // Se crea un objeto MutableLiveData para almacenar la respuesta.
        final MutableLiveData<GenericResponse<List<Producto>>> mutableLiveData = new MutableLiveData<>();

        // Se realiza una llamada asíncrona a la API para listar los productos recomendados.
        this.productoApi.listarProductosRecomendados().enqueue(new Callback<GenericResponse<List<Producto>>>() {

            // Este método se llama cuando se recibe una respuesta exitosa desde la API.
            @Override
            public void onResponse(Call<GenericResponse<List<Producto>>> call, Response<GenericResponse<List<Producto>>> response) {
                // Se establece el valor del MutableLiveData con la respuesta recibida.
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<Producto>>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        // Se devuelve el MutableLiveData que contiene la respuesta.
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
