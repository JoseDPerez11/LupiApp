package com.dsa.lupiapp.api;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.DocumentoAlmacenado;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DocumentoAlmacenadoApi {

    String base = "api/documento-almacenado";

    @Multipart
    @POST(base)
    Call<GenericResponse<DocumentoAlmacenado>> guardarDocumentoAlmacenado(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);

}
