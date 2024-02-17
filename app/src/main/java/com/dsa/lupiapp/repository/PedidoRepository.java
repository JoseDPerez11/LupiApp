package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.api.PedidoApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Pedido;
import com.dsa.lupiapp.entity.service.dto.GenerarPedidoDTO;
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

    public LiveData<GenericResponse<GenerarPedidoDTO>> guardarPedidoconDetalles(GenerarPedidoDTO dto) {
        final MutableLiveData<GenericResponse<GenerarPedidoDTO>> data = new MutableLiveData<>();
        pedidoApi.guardarPedido(dto).enqueue(new Callback<GenericResponse<GenerarPedidoDTO>>() {
            @Override
            public void onResponse(Call<GenericResponse<GenerarPedidoDTO>> call, Response<GenericResponse<GenerarPedidoDTO>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<GenerarPedidoDTO>> call, Throwable t) {
                data.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });

        return data;
    }

    public LiveData<GenericResponse<Pedido>> anularPedido(int idPedido) {
        MutableLiveData<GenericResponse<Pedido>> data = new MutableLiveData<>();
        this.pedidoApi.anularPedido(idPedido).enqueue(new Callback<GenericResponse<Pedido>>() {
            @Override
            public void onResponse(Call<GenericResponse<Pedido>> call, Response<GenericResponse<Pedido>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse<Pedido>> call, Throwable t) {
                data.setValue(new GenericResponse<>());
                t.printStackTrace();
            }
        });
        return data;
    }

}
