package com.dsa.lupiapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Pedido;
import com.dsa.lupiapp.entity.service.dto.GenerarPedidoDTO;
import com.dsa.lupiapp.entity.service.dto.PedidoConDetallesDTO;
import com.dsa.lupiapp.repository.PedidoRepository;

import java.util.List;


public class PedidoViewModel extends AndroidViewModel {

    private final PedidoRepository repository;
    public PedidoViewModel(@NonNull Application application) {
        super(application);
        this.repository = PedidoRepository.getInstance();
    }

    public LiveData<GenericResponse<List<PedidoConDetallesDTO>>> listarPedidosPorCliente(int idCli){
        return this.repository.listarPedidosPorCliente(idCli);
    }

    public LiveData<GenericResponse<GenerarPedidoDTO>> guardarPedido(GenerarPedidoDTO dto) {
        return this.repository.guardarPedidoconDetalles(dto);
    }

    public LiveData<GenericResponse<Pedido>> anularPedido(int idPe) {
        return repository.anularPedido(idPe);
    }

}
