package com.alura.johanmendoza.conversor;

public class Moneda {
    private String nombre;
    private String codigo;
    //Constructor con parámetros
    public Moneda(String nombre, String codigo){
        this.nombre = nombre;
        this.codigo = codigo.toUpperCase();
    }
    //Constructor vacio
    public Moneda(){

    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    @Override
    public String toString() {
        return "Moneda{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
