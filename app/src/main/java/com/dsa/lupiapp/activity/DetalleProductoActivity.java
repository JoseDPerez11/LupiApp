package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.databinding.ActivityDetalleMisComprasBinding;
import com.dsa.lupiapp.databinding.ActivityDetalleProductoBinding;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.dsa.lupiapp.entity.service.Producto;
import com.dsa.lupiapp.utils.Carrito;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetalleProductoActivity extends AppCompatActivity {

    private ActivityDetalleProductoBinding binding;

    final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();
    Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleProductoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        loadData();
    }

    private void init() {

        back();
    }

    private void loadData() {
        final String detalleString = this.getIntent().getStringExtra("detalleProducto");

        if (detalleString != null) {
            Producto producto;
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateSerializer())
                    .registerTypeAdapter(Time.class, new TimeSerializer())
                    .create();

            producto = gson.fromJson(detalleString, Producto.class);

            binding.tvNameProductoDetalle.setText(producto.getNombre());
            binding.tvPrecioProductoDetalle.setText(String.format(Locale.ENGLISH, "S/%.2f", producto.getPrecio()));
            binding.tvDescripcionProductoDetalle.setText(producto.getDescripcionProducto());

            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + producto.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(binding.imgProductoDetalle);

        } else {
            System.out.println("Error al obtener los detalles del producto");
        }

        this.binding.btnAgregarCarrito.setOnClickListener(view -> {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(1);
            detallePedido.setPrecio(producto.getPrecio());
            successMessage(Carrito.agregarProductos(detallePedido));
        });

    }

    private void back() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        binding.toolbar.setNavigationOnClickListener(view -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
    }

    public void successMessage(String message) {
        new SweetAlertDialog(this,
                SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo!")
                .setContentText(message).show();
    }

}