package com.dsa.lupiapp.api;

import com.dsa.lupiapp.entity.GenericResponse;
import com.dsa.lupiapp.entity.service.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UsuarioApi {
    String base = "api/usuario";

    @FormUrlEncoded
    @POST(base + "/login")
    Call<GenericResponse<Usuario>> login(@Field("email") String email, @Field("pass") String contrasenia);

}
