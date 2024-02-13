package com.dsa.lupiapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ItemMisComprasBinding;
import com.dsa.lupiapp.entity.service.dto.PedidoConDetallesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {

    private List<PedidoConDetallesDTO> pedidosConDetalle;

    public MisComprasAdapter(List<PedidoConDetallesDTO> pedidosConDetalle) {
        this.pedidosConDetalle = pedidosConDetalle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMisComprasBinding view = ItemMisComprasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.pedidosConDetalle.get(position));
    }

    @Override
    public int getItemCount() {
        return pedidosConDetalle.size();
    }

    public void updateItems(List<PedidoConDetallesDTO> pedido) {
        this.pedidosConDetalle.clear();
        this.pedidosConDetalle.addAll(pedido);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMisComprasBinding binding;

        public ViewHolder(@NonNull ItemMisComprasBinding binding) {
            super(binding.getRoot());
        }

        public void setItem(final PedidoConDetallesDTO pedidoDetalleDto) {
            binding.txtValueCodPurchases.setText("C000" + Integer.toString(pedidoDetalleDto.getPedido().getId()));
            binding.txtValueDatePurchases.setText(pedidoDetalleDto.getPedido().getFechaCompra().toString());
            binding.txtValueAmount.setText(String.format(Locale.ENGLISH, "S/%.2f", pedidoDetalleDto.getPedido().getMonto()));
            binding.txtValueOrder.setText(pedidoDetalleDto.getPedido().isAnularPedido() ? "Pedido Cancelado" : "Despachado, en proceso de envío");
        }

    }
}
