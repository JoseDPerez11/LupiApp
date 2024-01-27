package com.dsa.lupiapp.activity.ui.compras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dsa.lupiapp.databinding.FragmentMisComprasBinding;

public class MisComprasFragment extends Fragment {

    private FragmentMisComprasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMisComprasBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
}