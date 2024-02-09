package com.dsa.lupiapp.activity.ui.inicio;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.CategoriaAdapter;
import com.dsa.lupiapp.adapter.SliderAdapter;
import com.dsa.lupiapp.databinding.FragmentInicioBinding;
import com.dsa.lupiapp.entity.service.SliderItem;
import com.dsa.lupiapp.viewmodel.CategoriaViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private CategoriaViewModel categoriaViewModel;
    private GridView gvCategorias;
    private CategoriaAdapter categoriaAdapter;

    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    private FragmentInicioBinding binding;


    // Método llamado cuando se crea la vista del fragmento
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento utilizando View Binding
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    // Método llamado después de que la vista del fragmento se haya creado
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inicializar los componentes y cargar los datos
        init(view);
        initAdapter();
        loadData();
    }

    // Método para inicializar los componentes del fragmento
    private void init(View view) {
        // Obtener referencias a los componentes de la vista
        svCarrusel = view.findViewById(R.id.svCarrusel);

        // Este proveedor se utiliza para obtener o crear instancias de ViewModels asociados con este fragmento.
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        categoriaViewModel = viewModelProvider.get(CategoriaViewModel.class);

        gvCategorias = view.findViewById(R.id.gvCategorias);
    }

    // Método para inicializar los adaptadores del carrusel y la cuadrícula de categorías
    private void initAdapter() {
        // Inicializar el adaptador del carrusel
        sliderAdapter = new SliderAdapter(getContext());
        svCarrusel.setSliderAdapter(sliderAdapter);

        // Configurar opciones del carrusel
        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();

        // Inicializar el adaptador de la cuadrícula de categorías
        categoriaAdapter = new CategoriaAdapter(getContext(), R.layout.item_categories, new ArrayList<>());
        gvCategorias.setAdapter(categoriaAdapter);
    }

    // Método para cargar los datos en el carrusel y la cuadrícula de categorías

    private void loadData() {
        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.images4carrusel, "Excelente compromiso del personal"));
        lista.add(new SliderItem(R.drawable.fachadalupita, "Local Principal"));
        lista.add(new SliderItem(R.drawable.images3carrusel, "Gran variedad de productos"));
        lista.add(new SliderItem(R.drawable.deliverylupita, "Contamos con servicio de delivery"));

        sliderAdapter.updateItem(lista);

        categoriaViewModel.listarCategoriasActivas().observe(getViewLifecycleOwner(), response -> {
            if (response.getRpta() == 1) {
                categoriaAdapter.clear();
                categoriaAdapter.addAll(response.getBody());
                categoriaAdapter.notifyDataSetChanged();
            } else {
                System.out.println("Error al obtener categorias activas");
            }
        });

    }
}