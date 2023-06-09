package es.uca.iw.farmacia.data.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigoNacional;
    private String nombreComercial;
    private String composicion;
    private String categoria;
    private String formaFarmaceutica;
    private int stockDisponible;
    private double precioPorUnidad;

    // Constructores, getters y setters

    public Medicamento() {
    }

    public Medicamento(String codigoNacional, String nombreComercial, String composicion, String categoria,
            String formaFarmaceutica, int stockDisponible, double precioPorUnidad) {
        this.codigoNacional = codigoNacional;
        this.nombreComercial = nombreComercial;
        this.composicion = composicion;
        this.categoria = categoria;
        this.formaFarmaceutica = formaFarmaceutica;
        this.stockDisponible = stockDisponible;
        this.precioPorUnidad = precioPorUnidad;
    }

    public Medicamento(Medicamento medicamentoSeleccionado) {
        // TODO Auto-generated constructor stub
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoNacional() {
        return codigoNacional;
    }

    public void setCodigoNacional(String codigoNacional) {
        this.codigoNacional = codigoNacional;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getComposicion() {
        return composicion;
    }

    public void setComposicion(String composicion) {
        this.composicion = composicion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(String formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public double getPrecioPorUnidad() {
        return precioPorUnidad;
    }

    public void setPrecioPorUnidad(double precioPorUnidad) {
        this.precioPorUnidad = precioPorUnidad;
    }
}
