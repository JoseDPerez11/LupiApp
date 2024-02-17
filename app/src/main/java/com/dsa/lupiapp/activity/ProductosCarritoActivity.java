package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.ProductoCarritoAdapter;
import com.dsa.lupiapp.communication.CarritoCommunication;
import com.dsa.lupiapp.databinding.ActivityProductosCarritoBinding;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.entity.service.dto.GenerarPedidoDTO;
import com.dsa.lupiapp.utils.Carrito;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.dsa.lupiapp.viewmodel.PedidoViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ProductosCarritoActivity extends AppCompatActivity implements CarritoCommunication {

    private ActivityProductosCarritoBinding binding;

    private ProductoCarritoAdapter productoCarritoAdapter;
    private RecyclerView rcvBolsaCompras;

    private PedidoViewModel pedidoViewModel;

    final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Time.class, new TimeSerializer())
            .create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductosCarritoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initViewModel();
        initAdapter();
    }


    private void init() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_volver_atras);
        binding.toolbar.setNavigationOnClickListener(view -> {
            this.finish();
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });

        binding.btnFinalizarCompra.setOnClickListener(view -> {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pref = preferences.getString("UsuarioJson", "");
            Usuario usuario = gson.fromJson(pref, Usuario.class);
            int idCliente = usuario.getCliente().getId();
            if (idCliente != 0) {
                toastCorrecto("Hay un Usuario en sesión, registrando venta...");
                registrarPedido(idCliente);
            } else {
                toastIncorrecto("No ha iniciado sesión, se le redigirá al login");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }

        });
    }

    private void initViewModel() {
        final ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        this.pedidoViewModel = viewModelProvider.get(PedidoViewModel.class);
    }

    private void initAdapter() {
        productoCarritoAdapter = new ProductoCarritoAdapter(Carrito.getDetallesPedidos(), this);
        rcvBolsaCompras.setLayoutManager(new LinearLayoutManager(this));
        rcvBolsaCompras.setAdapter(productoCarritoAdapter);
    }

    private void registrarPedido(int idCliente) {
        ArrayList<DetallePedido> detallePedidos = Carrito.getDetallesPedidos();
        GenerarPedidoDTO dto = new GenerarPedidoDTO();
        java.util.Date date = new java.util.Date();
        dto.getPedido().setFechaCompra(new Date(date.getTime()));
        dto.getPedido().setAnularPedido(false);
        dto.getPedido().setMonto(getTotalVenta(detallePedidos));
        dto.getCliente().setId(idCliente);
        dto.setDetallePedidos(detallePedidos);
        this.pedidoViewModel.guardarPedido(dto).observe(this, response -> {
            if (response.getRpta() == 1) {
                toastCorrecto("Pedido registrado con éxito");
                Carrito.limpiar();
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            } else {
                toastIncorrecto("Demonios!, No se pudo registrar pedido");
            }
        });
    }

    private double getTotalVenta(List<DetallePedido> detalles) {
        float total = 0;
        for (DetallePedido detallePedido: detalles) {
            total += detallePedido.getTotal();
        }
        return total;
    }

    public void toastIncorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView textView = layouView.findViewById(R.id.txtMensajeToastError);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();

    }

    public void toastCorrecto(String texto) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layouView = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView textView = layouView.findViewById(R.id.txtMensajeToastOk);
        textView.setText(texto);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layouView);
        toast.show();
    }

    @Override
    public void eliminarDetalle(int idProducto) {
        Carrito.eliminar(idProducto);
        this.productoCarritoAdapter.notifyDataSetChanged();
    }
}