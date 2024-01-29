package com.dsa.lupiapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Cliente;
import com.dsa.lupiapp.repository.ClienteRepository;

public class ClienteViewModel extends AndroidViewModel {

    private final ClienteRepository repository;
    public ClienteViewModel(@NonNull Application application) {
        super(application);
        this.repository = ClienteRepository.getInstance();
    }

    public LiveData<GenericResponse<Cliente>> save(Cliente cliente) {
        return this.repository.save(cliente);
    }

}
