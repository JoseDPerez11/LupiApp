package com.dsa.lupiapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ActivityRegistrarUsuarioBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private ActivityRegistrarUsuarioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }


    public void succesMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Buen trabajo")
                .setContentText(message).show();
    }

    public void errorMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setContentText("Ops...")
                .setContentText(message).show();
    }

    public void warningMessage(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setContentText("Notificación")
                .setContentText(message).show();
    }

}