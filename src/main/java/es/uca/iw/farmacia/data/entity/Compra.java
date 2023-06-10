package es.uca.iw.farmacia.data.entity;

import java.util.Date;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class Compra {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "medicamento_id")
  private Medicamento medicamento;
  
  private Date fechaCompra;
  private int cantidad;
  private double precio;
  private double precioUnidad;  
  public Compra() {
    // Default constructor required by JPA
  }

  public Compra(Long medicamentoId, Date fechaCompra, int cantidad, double precioUnidad, Medicamento medicamento) {
    this.fechaCompra = fechaCompra;
    this.cantidad = cantidad;
    this.precioUnidad = precioUnidad;
    this.precio = cantidad * precioUnidad;
    this.medicamento = medicamento;
  }

  public Long getId() { 
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getFechaCompra() {
    return fechaCompra;
  }

  public void setFechaCompra(Date fechaCompra) {
    this.fechaCompra = fechaCompra;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }
  
  public double getPrecio() {
	  return precio;
  }
  
  public void setPrecio() {
	  this.precio = this.cantidad * this.precioUnidad;
  }
  
  public void setPrecioUnidad(double precioUnidad) {
	  this.precioUnidad = precioUnidad;
  }
  
  public double getPrecioUnidad() {
	  return precioUnidad;
  }

  public void setMedicamento(Medicamento medicamento) {
	    this.medicamento = medicamento;
	}


public String getNombreComercial() {
	return medicamento.getNombreComercial();
}

public Medicamento getMedicamento() {
	// TODO Auto-generated method stub
	return this.medicamento;
}

public void setPrecioTotal(double precioTotal) {
	this.precio = precioTotal;
	
}

}
