package com.example.lchang.sqliteimages;

public class Plato {

    private int codigo;
    private String nombre;
    private Double precio;
    private byte[] imagen;

    public Plato(int codigo, String nombre, Double precio, byte[] imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
