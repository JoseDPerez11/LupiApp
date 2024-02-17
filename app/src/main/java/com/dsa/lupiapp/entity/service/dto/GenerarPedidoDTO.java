package com.dsa.lupiapp.entity.service.dto;

import com.dsa.lupiapp.entity.service.Cliente;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.dsa.lupiapp.entity.service.Pedido;

import java.util.ArrayList;

public class GenerarPedidoDTO {
    private Pedido pedido;
    private ArrayList<DetallePedido> detallePedidos;
    private Cliente cliente;

    public GenerarPedidoDTO() {
        this.pedido = new Pedido();
        this.detallePedidos = new ArrayList<>();
        this.cliente = new Cliente();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ArrayList<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public void setDetallePedidos(ArrayList<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
