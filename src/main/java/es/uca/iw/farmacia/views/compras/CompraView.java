package es.uca.iw.farmacia.views.compras;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.farmacia.data.entity.Compra;
import es.uca.iw.farmacia.data.service.CompraService;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;


@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "compras", layout = MainLayout.class)
@PermitAll
public class CompraView extends VerticalLayout {
	  @SuppressWarnings("unused")
	private CompraService compraService;

	  public CompraView(CompraService compraService) {
	    this.compraService = compraService;
	        // ...

	        Grid<Compra> grid = new Grid<>(Compra.class);
	        grid.setColumns("id", "medicamento.nombreComercial", "cantidad");

	        DecimalFormat decimalFormat = new DecimalFormat("#0.00");

	        grid.addColumn(compra -> decimalFormat.format(compra.getPrecioUnidad()) + " €")
	                .setHeader("Precio Unidad");

	        grid.addColumn(compra -> decimalFormat.format(compra.getPrecio()) + " €")
	                .setHeader("Precio");

	        grid.addColumn(compra -> {
	            Date fechaCompra = compra.getFechaCompra();
	            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	            return formatter.format(fechaCompra);
	        }).setHeader("Fecha Compra");

	        grid.setItems(compraService.listarCompras());

	        add(grid);
	    }

	
	}