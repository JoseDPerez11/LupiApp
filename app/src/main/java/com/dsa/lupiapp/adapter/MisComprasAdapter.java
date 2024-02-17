package com.dsa.lupiapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.activity.ui.compras.DetalleMisComprasActivity;
import com.dsa.lupiapp.communication.AnularPedidoCommunication;
import com.dsa.lupiapp.communication.Communication;
import com.dsa.lupiapp.databinding.ItemMisComprasBinding;
import com.dsa.lupiapp.entity.service.dto.PedidoConDetallesDTO;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {

    private final List<PedidoConDetallesDTO> pedidos;
    private final Communication communication;
    private final AnularPedidoCommunication anularPedidoCommunication;

    public MisComprasAdapter(List<PedidoConDetallesDTO> pedidos, Communication communication, AnularPedidoCommunication anularPedidoCommunication) {
        this.pedidos = pedidos;
        this.communication = communication;
        this.anularPedidoCommunication = anularPedidoCommunication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMisComprasBinding view = ItemMisComprasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.pedidos.get(position));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void updateItems(List<PedidoConDetallesDTO> pedido) {
        this.pedidos.clear();
        this.pedidos.addAll(pedido);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMisComprasBinding binding;

        public ViewHolder(@NonNull ItemMisComprasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(final PedidoConDetallesDTO pedidoDetalleDto) {

            binding.txtValueCodPurchases.setText("C000" + Integer.toString(pedidoDetalleDto.getPedido().getId()));
            binding.txtValueDatePurchases.setText(pedidoDetalleDto.getPedido().getFechaCompra().toString());
            binding.txtValueAmount.setText(String.format(Locale.ENGLISH, "S/%.2f", pedidoDetalleDto.getPedido().getMonto()));
            binding.txtValueOrder.setText(pedidoDetalleDto.getPedido().isAnularPedido() ? "Pedido Cancelado" : "Despachado, en proceso de envío");

            itemView.setOnClickListener(view -> {
                final Intent intent = new Intent(itemView.getContext(), DetalleMisComprasActivity.class);
                final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();

                intent.putExtra("detailsPurchases", gson.toJson(pedidoDetalleDto.getDetallePedido()));
                communication.showDetails(intent);
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    anularPedido(pedidoDetalleDto.getPedido().getId());
                    return true;
                }
            });
        }

        private void anularPedido(int id) {
            new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Aviso del sistema !")
                    .setContentText("¿Estás seguro de cancelar el pedido solicitado?, Una vez cancelado no podrás deshacer los cambios")
                    .setCancelText("No, Cancelar!").setConfirmText("Sí, Confirmar")
                    .showCancelButton(true)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen Trabajo")
                                .setContentText(anularPedidoCommunication.anularPedido(id))
                                .show();
                    }).setCancelClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Operación Cancelada !")
                                .setContentText("No cancelaste ningún pedido")
                                .show();
                    }).show();
        }



    }
}
