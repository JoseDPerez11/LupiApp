package com.dsa.lupiapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.databinding.ItemDetalleMisComprasBinding;
import com.dsa.lupiapp.entity.service.DetallePedido;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class DetalleMisComprasAdapter extends RecyclerView.Adapter<DetalleMisComprasAdapter.ViewHolder> {

    private final List<DetallePedido> detalles;

    public DetalleMisComprasAdapter(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemDetalleMisComprasBinding view = ItemDetalleMisComprasBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(this.detalles.get(position));
    }

    @Override
    public int getItemCount() {
        return detalles.size();
    }

    public void updateItems(List<DetallePedido> detalles) {
        this.detalles.clear();
        this.detalles.addAll(detalles);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ItemDetalleMisComprasBinding binding;
        public ViewHolder(@NonNull ItemDetalleMisComprasBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(final DetallePedido detallePedido) {
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + detallePedido.getProducto().getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(binding.imgProductoDetail);

            binding.txtValueCodDetail.setText("C000" + Integer.toString(detallePedido.getPedido().getId()));
            binding.txtValueProductoDetail.setText(detallePedido.getProducto().getNombre());
            binding.txtValueQuantityDetail.setText(Integer.toString(detallePedido.getCantidad()));
            binding.txtValuePriceProductoDetail.setText(String.format(Locale.ENGLISH, "S/%.2f", detallePedido.getPrecio()));

        }
    }
}
