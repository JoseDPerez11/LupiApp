package com.dsa.lupiapp.entity.service;

public class SliderItem {
    private String titulo;
    private String name;

    public SliderItem() {}

    public SliderItem(String titulo, String name) {
        this.titulo = titulo;
        this.name = name;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
