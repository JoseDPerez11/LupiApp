package com.dsa.lupiapp.api;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Cliente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClienteApi {

    String base = "api/cliente";

    @POST(base)
    Call<GenericResponse<Cliente>> guardarCliente(@Body Cliente cliente);

}
