package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ActivityRegistrarUsuarioBinding;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private ActivityRegistrarUsuarioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}