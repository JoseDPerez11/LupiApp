package com.dsa.lupiapp.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.communication.CarritoCommunication;
import com.dsa.lupiapp.databinding.ItemProductosCarritoBinding;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ProductoCarritoAdapter extends RecyclerView.Adapter<ProductoCarritoAdapter.ViewHolder> {

    private final List<DetallePedido> detalles;
    private final CarritoCommunication carritoCommunication;

    public ProductoCarritoAdapter(List<DetallePedido> detalles, CarritoCommunication carritoCommunication) {
        this.detalles = detalles;
        this.carritoCommunication = carritoCommunication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemProductosCarritoBinding view = ItemProductosCarritoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view, this.carritoCommunication);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProductosCarritoBinding binding;
        private final CarritoCommunication carritoCommunication;

        public ViewHolder(@NonNull ItemProductosCarritoBinding binding, CarritoCommunication carritoCommunication) {
            super(binding.getRoot());
            this.binding = binding;
            this.carritoCommunication = carritoCommunication;
        }

        public void setItem(final DetallePedido detallePedido) {
            this.binding.tvNombreProductoDCarrito.setText(detallePedido.getProducto().getNombre());
            this.binding.tvPrecioPDCarrito.setText(String.format(Locale.ENGLISH, "S/%.2f", detallePedido.getPrecio()));

            int cantidad = detallePedido.getCantidad();
            this.binding.edtCantidad.setText(Integer.toString(cantidad));

            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + detallePedido.getProducto().getFoto().getFileName();
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(binding.imgProductoDCarrito);

            //Eliminar item del carrito
            this.binding.btnEliminarPCarrito.setOnClickListener(view -> {
                toastCorrecto("Platillo eliminado exitado");
                carritoCommunication.eliminarDetalle(detallePedido.getId());
            });

            //Actualizar cantidad del carrito
            binding.btnAdd.setOnClickListener(view -> {
                if (detallePedido.getCantidad() != 10) {
                    detallePedido.addOne();
                    ProductoCarritoAdapter.this.notifyDataSetChanged();
                }
            });

            binding.btnDecrease.setOnClickListener(view -> {
                if (detallePedido.getCantidad() != 1) {
                    detallePedido.removeOne();
                    ProductoCarritoAdapter.this.notifyDataSetChanged();
                }
            });

        }

        private void toastCorrecto(String message) {
            LayoutInflater layoutInflater = LayoutInflater.from(itemView.getContext());
            View layoutView = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) itemView.findViewById(R.id.ll_custom_toast_ok));
            TextView textView = layoutView.findViewById(R.id.txtMensajeToastOk);
            textView.setText(message);

            Toast toast = new Toast(itemView.getContext());
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layoutView);
            toast.show();

        }

    }

}
