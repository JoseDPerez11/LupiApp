package com.dsa.lupiapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Usuario;
import com.dsa.lupiapp.repository.UsuarioRepository;

public class UsuarioViewModel extends AndroidViewModel {

    private final UsuarioRepository repository;
    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        this.repository = UsuarioRepository.getInstance();
    }

    public LiveData<GenericResponse<Usuario>> login(String email, String pass) {
        return this.repository.login(email, pass);
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario usuario) {
        return this.repository.save(usuario);
    }
}
