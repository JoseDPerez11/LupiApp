package com.dsa.lupiapp.adapter;

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
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.entity.service.Producto;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ProductosPorCategoriaAdapter extends RecyclerView.Adapter<ProductosPorCategoriaAdapter.ViewHolder> {

    private List<Producto> listadoDeProductosPorCategoria;

    public ProductosPorCategoriaAdapter(List<Producto> listadoDeProductosPorCategoria) {
        this.listadoDeProductosPorCategoria = listadoDeProductosPorCategoria;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productos_por_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosPorCategoriaAdapter.ViewHolder holder, int position) {
        holder.setItem(this.listadoDeProductosPorCategoria.get(position));
    }

    @Override
    public int getItemCount() {
        return listadoDeProductosPorCategoria.size();
    }

    public void updateItems(List<Producto> productosByCategoria) {
        this.listadoDeProductosPorCategoria.clear();
        this.listadoDeProductosPorCategoria.addAll(productosByCategoria);
        this.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProductoCategoria;
        private TextView nameProductoCategoria;
        private TextView productoPriceCategoria;
        private Button btnOrdenarProductoCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgProductoCategoria = itemView.findViewById(R.id.imgProductoCategoria);
            this.nameProductoCategoria = itemView.findViewById(R.id.txtnameProductoCategoria);
            this.productoPriceCategoria = itemView.findViewById(R.id.txtPrecioProductoCategoria);
            this.btnOrdenarProductoCategoria = itemView.findViewById(R.id.btnOrdenarCategoria);
        }

        public void setItem(final Producto producto) {
            String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/" + producto.getFoto().getFileName();

            Picasso picasso = new Picasso.Builder(itemView.getContext())
                    .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                    .build();
            picasso.load(url)
                    .error(R.drawable.image_not_found)
                    .into(imgProductoCategoria);

            nameProductoCategoria.setText(producto.getNombre());
            productoPriceCategoria.setText(String.format(Locale.ENGLISH, "S/%.2f", producto.getPrecio()));
            btnOrdenarProductoCategoria.setOnClickListener(view -> {
                Toast.makeText(this.itemView.getContext(), "Ordenar producto", Toast.LENGTH_SHORT).show();
            });
        }

    }
}
