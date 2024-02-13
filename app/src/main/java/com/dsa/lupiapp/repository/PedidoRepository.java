package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.api.PedidoApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.dto.PedidoConDetallesDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoRepository {

    private final PedidoApi pedidoApi;
    private static PedidoRepository pedidoRepository;

    public PedidoRepository() {
        this.pedidoApi = ConfigApi.getPedidoApi();
    }

    public static PedidoRepository getInstance() {
        if (pedidoRepository == null) {
            pedidoRepository = new PedidoRepository();
        }
        return pedidoRepository;
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idCliente) {
        final MutableLiveData<GenericResponse<List<PedidoConDetallesDTO>>> mutableLiveData = new  MutableLiveData<>();
        this.pedidoApi.listarPedidosPorCliente(idCliente).enqueue(new Callback<GenericResponse<List<PedidoConDetallesDTO>>>() {
            @Override
            public void onResponse(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Response<GenericResponse<List<PedidoConDetallesDTO>>> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<List<PedidoConDetallesDTO>>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse());
                System.out.println("Error al obtener los pedidos: " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mutableLiveData;
    }




}
