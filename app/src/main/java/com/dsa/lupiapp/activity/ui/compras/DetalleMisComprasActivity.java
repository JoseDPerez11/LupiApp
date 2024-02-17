package com.dsa.lupiapp.activity.ui.compras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.DetalleMisComprasAdapter;
import com.dsa.lupiapp.databinding.ActivityDetalleMisComprasBinding;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class DetalleMisComprasActivity extends AppCompatActivity {

    private ActivityDetalleMisComprasBinding binding;

    private RecyclerView rcvDetalleMisCompras;
    private DetalleMisComprasAdapter detalleMisComprasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleMisComprasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rcvDetalleMisCompras = binding.rcvDetalleMisCompras;
        init();
        initAdapter();
        loadData();
    }

    private void init() {
        binding.rcvDetalleMisCompras.setLayoutManager(new GridLayoutManager(this, 1));
        binding.toolbar.setNavigationOnClickListener(view -> {
            this.onBackPressed();
        });
    }

    private void initAdapter() {
        detalleMisComprasAdapter = new DetalleMisComprasAdapter(new ArrayList<>());
        rcvDetalleMisCompras.setAdapter(detalleMisComprasAdapter);
    }

    private void loadData() {
        final String detallesPedido = this.getIntent().getStringExtra("detailsPurchases");
        if (detallesPedido != null) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class, new TimeSerializer())
                    .create();
            List<DetallePedido> detalles = gson.fromJson(detallesPedido,
                    new TypeToken<List<DetallePedido>>() {
            }.getType());
            detalleMisComprasAdapter.updateItems(detalles);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        this.overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }
}