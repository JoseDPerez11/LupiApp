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

    // URL base para descargar las imágenes de las categorías
    private final String url = ConfigApi.baseUrlE + "/api/documento-almacenado/download/";

    // Constructor del adaptador
    public CategoriaAdapter(@NonNull Context context, int resource, @NonNull List<Categoria> objects) {
        super(context, resource, objects);
    }

    // Método para obtener la vista de cada elemento en la lista
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Verifica si la vista reciclada es nula
        if (convertView == null) {
            // Si es nula, infla la vista del layout de cada elemento de la lista
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_categories, parent, false);
        }

        // Obtiene el objeto Categoria en la posición actual de la lista
        Categoria categoria = this.getItem(position);

        // Obtiene referencias a los elementos de la vista
        ImageView imgCategoria = convertView.findViewById(R.id.imgCategoria);
        TextView txtNombreCategoria = convertView.findViewById(R.id.txtNombreCategoria);

        // Crea una instancia de Picasso para cargar las imágenes de forma eficiente
        Picasso picasso = new Picasso.Builder(convertView.getContext())
                .downloader(new OkHttp3Downloader(ConfigApi.getClient())) // Configura el cliente OkHttp para descargas
                .build();

        // Carga la imagen de la categoría desde la URL completa
        picasso.load(url + categoria.getFoto().getFileName())
                .error(R.drawable.image_not_found) // Establece una imagen de error en caso de fallo de carga
                .into(imgCategoria); // Coloca la imagen en el ImageView

        // Establece el nombre de la categoría en el TextView
        txtNombreCategoria.setText(categoria.getNombre());

        // Devuelve la vista actualizada
        return convertView;
    }
}
