package com.dsa.lupiapp.entity.service;

public class SliderItem {
    private String titulo;
    private int imagen;

    public SliderItem() {}

    public SliderItem(int imagen, String titulo) {
        this.titulo = titulo;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int name) {
        this.imagen = imagen;
    }
}
