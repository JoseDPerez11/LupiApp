package com.dsa.lupiapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.DocumentoAlmacenado;
import com.dsa.lupiapp.repository.DocumentoAlmacenadoRepository;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocumentoAlmacenadoViewModel extends AndroidViewModel {

    private final DocumentoAlmacenadoRepository repository;
    public DocumentoAlmacenadoViewModel(@NonNull Application application) {
        super(application);
        this.repository = DocumentoAlmacenadoRepository.getInstance();
    }

    public LiveData<GenericResponse<DocumentoAlmacenado>> save(MultipartBody.Part part, RequestBody requestBody) {
        return this.repository.savePhoto(part, requestBody);
    }

}
