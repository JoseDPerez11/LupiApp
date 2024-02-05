package com.dsa.lupiapp.activity.ui.inicio;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.adapter.SliderAdapter;
import com.dsa.lupiapp.databinding.FragmentInicioBinding;
import com.dsa.lupiapp.entity.service.SliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {

    private SliderView svCarrusel;
    private SliderAdapter sliderAdapter;
    private FragmentInicioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        initAdapter();
        loadData();
    }


    private void init(View view) {
        svCarrusel = view.findViewById(R.id.svCarrusel);

    }

    private void initAdapter() {
        sliderAdapter = new SliderAdapter(getContext());

        svCarrusel.setSliderAdapter(sliderAdapter);

        svCarrusel.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        svCarrusel.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        svCarrusel.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        svCarrusel.setIndicatorSelectedColor(Color.WHITE);
        svCarrusel.setIndicatorUnselectedColor(Color.GRAY);
        svCarrusel.setScrollTimeInSec(4); //set scroll delay in seconds :
        svCarrusel.startAutoCycle();
    }

    private void loadData() {
        List<SliderItem> lista = new ArrayList<>();
        lista.add(new SliderItem(R.drawable.images4carrusel, "Excelente compromiso del personal"));
        lista.add(new SliderItem(R.drawable.fachadalupita, "Local Principal"));
        lista.add(new SliderItem(R.drawable.images3carrusel, "Gran variedad de productos"));
        lista.add(new SliderItem(R.drawable.deliverylupita, "Contamos con servicio de delivery"));

        sliderAdapter.updateItem(lista);
    }
}