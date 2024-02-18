package com.dsa.lupiapp.activity.ui.compras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.MisComprasAdapter;
import com.dsa.lupiapp.communication.AnularPedidoCommunication;
import com.dsa.lupiapp.communication.Communication;
import com.dsa.lupiapp.databinding.FragmentMisComprasBinding;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.dsa.lupiapp.viewmodel.PedidoViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class MisComprasFragment extends Fragment implements Communication, AnularPedidoCommunication {

    private PedidoViewModel pedidoViewModel;
    private RecyclerView rcvPedidos;
    private MisComprasAdapter misComprasAdapter;

    private FragmentMisComprasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMisComprasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvPedidos = binding.rcvMisCompras;
        initViewModel();
        initAdapter();
        loadData();
    }

    private void initViewModel() {
        pedidoViewModel = new ViewModelProvider(this).get(PedidoViewModel.class);
    }

    private void initAdapter() {
        misComprasAdapter = new MisComprasAdapter(new ArrayList<>(), this, this);
        rcvPedidos.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rcvPedidos.setAdapter(misComprasAdapter);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();

        String usuarioJson = sharedPreferences.getString("UsuarioJson", null);
        if (usuarioJson != null) {
            final Usuario usuario = gson.fromJson(usuarioJson, Usuario.class);
            this.pedidoViewModel.listarPedidosPorCliente(usuario.getCliente().getId()).observe(getViewLifecycleOwner(), response -> {
                misComprasAdapter.updateItems(response.getBody());
            });
        }
    }

    @Override
    public void showDetails(Intent intent) {
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.above_in, R.anim.above_out);
    }

    @Override
    public String anularPedido(int id) {
        this.pedidoViewModel.anularPedido(id).observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                loadData();
            }
        });
        return "El pedido ha sido cancelado";
    }
}