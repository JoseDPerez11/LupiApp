package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.ProductosPorCategoriaAdapter;
import com.dsa.lupiapp.databinding.ActivityInicioBinding;
import com.dsa.lupiapp.databinding.ActivityListarProductosPorCategoriaBinding;
import com.dsa.lupiapp.entity.service.Producto;
import com.dsa.lupiapp.viewmodel.ProductoViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListarProductosPorCategoriaActivity extends AppCompatActivity {

    private ProductoViewModel productoViewModel;
    private ProductosPorCategoriaAdapter productosPorCategoriaAdapter;
    private List<Producto> productos = new ArrayList<>();
    private RecyclerView rcvProductoPorCategoria;

    private ActivityListarProductosPorCategoriaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListarProductosPorCategoriaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());;

        init();
        initViewModel();
        initAdapter();
        loadData();
    }

    private void init() {
        navigationBack();
    }

    private void initViewModel() {
        final ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        this.productoViewModel = viewModelProvider.get(ProductoViewModel.class);
    }

    private void initAdapter() {
        productosPorCategoriaAdapter = new ProductosPorCategoriaAdapter(productos);
        rcvProductoPorCategoria = findViewById(R.id.rcvProductosPorCategoria);
        rcvProductoPorCategoria.setAdapter(productosPorCategoriaAdapter);
        rcvProductoPorCategoria.setLayoutManager(new LinearLayoutManager(this));

    }

    private void loadData() {
        int idCategoria = getIntent().getIntExtra("idC", 0);
        productoViewModel.listarProductosPorCategoria(idCategoria).observe(this, response -> {
            productosPorCategoriaAdapter.updateItems(response.getBody());
        });
    }

    private void navigationBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        binding.toolbar.setNavigationOnClickListener(view -> {
            this.finish();

            this.overridePendingTransition(R.anim.left_in, R.anim.left_out);
        });
    }
}