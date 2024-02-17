package com.dsa.lupiapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.activity.DetalleProductoActivity;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.communication.Communication;
import com.dsa.lupiapp.databinding.ItemProductosBinding;
import com.dsa.lupiapp.entity.service.Producto;
import com.dsa.lupiapp.utils.DateSerializer;
import com.dsa.lupiapp.utils.TimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class ProductosRecomendadosAdapter extends RecyclerView.Adapter<ProductosRecomendadosAdapter.ViewHolder> {

    private List<Producto> productos;
    private final Communication communication;

    public ProductosRecomendadosAdapter(List<Producto> productos, Communication communication) {
        this.productos = productos;
        this.communication = communication;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemProductosBinding view = ItemProductosBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosRecomendadosAdapter.ViewHolder holder, int position) {
        holder.setItem(this.productos.get(position));
    }

    @Override
    public int getItemCount() {
        return this.productos.size();
    }

    public void updateItems(List<Producto> productos) {
        this.productos.clear();
        this.productos.addAll(productos);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProductosBinding binding;
        public ViewHolder(@NonNull ItemProductosBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(final Producto producto) {

            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + producto.getFoto().getFileName();
            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(binding.imgProducto);
            binding.nameProducto.setText(producto.getNombre());
            binding.btnOrdenar.setOnClickListener(v-> {
                Toast.makeText(itemView.getContext(), "Hola Mundo", Toast.LENGTH_SHORT).show();
            });

            itemView.setOnClickListener(v -> {
                final Intent intent = new Intent(itemView.getContext(), DetalleProductoActivity.class);
                final Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new DateSerializer())
                        .registerTypeAdapter(Time.class, new TimeSerializer())
                        .create();
                intent.putExtra("detalleProducto", gson.toJson(producto));
                communication.showDetails(intent);
            });

        }
    }
}
