package com.dsa.lupiapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.entity.service.Categoria;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {

    private final String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/";
    public CategoriaAdapter(@NonNull Context context, int resource, @NonNull List<Categoria> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_categories, parent, false);
        }

        Categoria categoria = this.getItem(position);
        ImageView imgCategoria = convertView.findViewById(R.id.imgCategoria);
        TextView txtNombreCategoria = convertView.findViewById(R.id.txtNombreCategoria);

        Picasso picasso = new Picasso.Builder(convertView.getContext())
                .downloader(new OkHttp3Downloader(ConfigApi.getClient()))
                .build();

        picasso.load(url + categoria.getFoto().getFileName())
                .error(R.drawable.image_not_found)
                .into(imgCategoria);

        txtNombreCategoria.setText(categoria.getNombre());

        return convertView;
    }
}
