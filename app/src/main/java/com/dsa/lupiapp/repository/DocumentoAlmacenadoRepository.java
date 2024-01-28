package com.dsa.lupiapp.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dsa.lupiapp.api.ConfigApi;
import com.dsa.lupiapp.api.DocumentoAlmacenadoApi;
import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentoAlmacenadoRepository {

    private static DocumentoAlmacenadoRepository repository;
    private final DocumentoAlmacenadoApi documentoAlmacenadoApi;

    public DocumentoAlmacenadoRepository() {
        this.documentoAlmacenadoApi = ConfigApi.getDocumentoAlmacenadoApi();
    }

    public static DocumentoAlmacenadoRepository getInstance() {
        if (repository == null) {
            repository = new DocumentoAlmacenadoRepository();
        }
        return repository;
    }


    // Este método utiliza Retrofit para enviar una imagen al servidor y observar la respuesta utilizando LiveData.
    public LiveData<GenericResponse<DocumentoAlmacenado>> savePhoto(MultipartBody.Part part, RequestBody requestBody) {
        // Se declara un LiveData que llevará la respuesta del servidor.
        // La respuesta es de tipo GenericResponse<DocumentoAlmacenado>.
        final MutableLiveData<GenericResponse<DocumentoAlmacenado>> mutableLiveData = new MutableLiveData<>();

        // Se utiliza el objeto documentoAlmacenadoApi para llamar al método guardarDocumentoAlmacenado del servidor.
        this.documentoAlmacenadoApi.guardarDocumentoAlmacenado(part, requestBody).enqueue(new Callback<GenericResponse<DocumentoAlmacenado>>() {
            @Override
            public void onResponse(Call<GenericResponse<DocumentoAlmacenado>> call, Response<GenericResponse<DocumentoAlmacenado>> response) {
                // Se establece el valor del mutableLiveData con la respuesta del servidor.
                mutableLiveData.setValue(response.body());
            }

            // Este método se llama en caso de un fallo en la llamada al servidor.
            @Override
            public void onFailure(Call<GenericResponse<DocumentoAlmacenado>> call, Throwable t) {
                mutableLiveData.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error: " + t.getMessage());
                t.printStackTrace();
            }
        });

        return mutableLiveData;
    }

}
