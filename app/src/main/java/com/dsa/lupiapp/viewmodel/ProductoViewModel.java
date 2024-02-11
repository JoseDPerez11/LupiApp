package com.dsa.lupiapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Producto;
import com.dsa.lupiapp.repository.ProductoRepository;

import java.util.List;

public class ProductoViewModel extends AndroidViewModel {

    private final ProductoRepository productoRepository;
    public ProductoViewModel(@NonNull Application application) {
        super(application);
        productoRepository = ProductoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosRecomendados() {
        return this.productoRepository.listarProductosRecomendados();
    }

    public LiveData<GenericResponse<List<Producto>>> listarProductosPorCategoria(int idC) {
        return this.productoRepository.listarProductosPorCategoria(idC);
    }
}
