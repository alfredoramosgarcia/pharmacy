package es.uca.iw.farmacia.views.caja;

import com.vaadin.flow.component.Text;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import es.uca.iw.farmacia.data.entity.Compra;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.CompraService;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;

import jakarta.annotation.security.PermitAll;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "caja", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class CajaView extends VerticalLayout {

    private Grid<Compra> compraGrid;
    private List<Compra> listaCompras;
    private MedicamentoService medicamentoService;
    private CompraService compraService;
    private double precioTotal;
    private Button precioTotalLabel; 

    @SuppressWarnings("unused")
	public CajaView(MedicamentoService medicamentoService, CompraService compraService) {
        this.compraService = compraService;
        this.medicamentoService = medicamentoService;
        listaCompras = new ArrayList<>();
        precioTotal = 0.0;

        compraGrid = new Grid<>(Compra.class);
        compraGrid.setColumns("nombreComercial");
        compraGrid.addColumn(medicamento -> medicamento.getPrecioUnidad() + " €")
                .setHeader("Precio Unidad");

       
        Grid.Column<Compra> cantidadColumn = compraGrid.addComponentColumn(compra -> {
            IntegerField cantidadField = new IntegerField();
            cantidadField.setValue(compra.getCantidad());
            cantidadField.addValueChangeListener(event -> {
                int nuevaCantidad = event.getValue();
                compra.setCantidad(nuevaCantidad);
                actualizarPrecioTotal();
            });

            Button botonIncrementar = new Button("+");
            Button botonDecrementar = new Button("-");
            
            HorizontalLayout botonesLayout = new HorizontalLayout(botonIncrementar, botonDecrementar);
            botonesLayout.setAlignItems(Alignment.END);
            
            botonIncrementar.addClickListener(event -> {
                int cantidadActual = cantidadField.getValue();
                cantidadField.setValue(cantidadActual + 1);
            });
            
            botonDecrementar.addClickListener(event -> {
                int cantidadActual = cantidadField.getValue();
                if (cantidadActual > 0) {
                    cantidadField.setValue(cantidadActual - 1);
                }
            });

            HorizontalLayout cantidadLayout = new HorizontalLayout(cantidadField, botonesLayout);
            cantidadLayout.setJustifyContentMode(JustifyContentMode.END);
            cantidadLayout.getStyle().set("max-height", "50px");

            return cantidadLayout;
        }).setHeader("Cantidad");

        compraGrid.setItems(listaCompras);

        ComboBox<Medicamento> medicamentoComboBox = new ComboBox<>("Medicamento");
        medicamentoComboBox.setItemLabelGenerator(Medicamento::getNombreComercial);
        medicamentoComboBox.setItems(medicamentoService.obtenerTodosLosMedicamentos());

        IntegerField cantidadField = new IntegerField("Cantidad");
        Button agregarButton = new Button("Agregar");
        agregarButton.addClickListener(e -> agregarMedicamento(medicamentoComboBox.getValue(), cantidadField.getValue()));

        Button eliminarButton = new Button("Eliminar");
        eliminarButton.addClickListener(e -> eliminarMedicamento());
        eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout filtroLayout = new HorizontalLayout(medicamentoComboBox, cantidadField, agregarButton, eliminarButton);
        filtroLayout.setVerticalComponentAlignment(Alignment.END, medicamentoComboBox);
        filtroLayout.setAlignSelf(Alignment.END, cantidadField);
        filtroLayout.setAlignSelf(Alignment.END, agregarButton);
        filtroLayout.setAlignSelf(Alignment.END, eliminarButton);

        Button finalizarCompraButton = new Button("Finalizar compra");
        finalizarCompraButton.addClickListener(e -> finalizarCompra());
        finalizarCompraButton.getStyle().set("background-color", "red");
        finalizarCompraButton.getStyle().set("color", "white");

        HorizontalLayout finalizarCompraLayout = new HorizontalLayout(finalizarCompraButton);
        finalizarCompraLayout.setJustifyContentMode(JustifyContentMode.END);
        finalizarCompraLayout.getStyle().set("padding", "8px");

        add(filtroLayout, compraGrid);
        actualizarPrecioTotal();
        add(finalizarCompraLayout);
    }


    private void agregarMedicamento(Medicamento medicamento, Integer cantidad) {
        if (medicamento != null && cantidad != null && cantidad > 0) {
            Compra compraExistente = listaCompras.stream()
                    .filter(c -> c.getMedicamento().equals(medicamento))
                    .findFirst()
                    .orElse(null);

            if (compraExistente != null) {
                compraExistente.setCantidad(compraExistente.getCantidad() + cantidad);
            } else {
                Compra compra = new Compra();
                compra.setMedicamento(medicamento);
                compra.setCantidad(cantidad);
                compra.setFechaCompra(new Date());
                compra.setPrecioUnidad(medicamento.getPrecioPorUnidad());
                listaCompras.add(compra);
            }

            if(cantidad <= medicamento.getStockDisponible()) actualizarStockMedicamento(medicamento, cantidad);
            else { mostrarNotificacionError("No hay stock suficiente para la compra");
            return;}
            actualizarPrecioTotal(); 
            compraGrid.getDataProvider().refreshAll();
        }
    }
    
    private void actualizarStockMedicamento(Medicamento medicamento, Integer cantidad) {
        medicamento.setStockDisponible(medicamento.getStockDisponible() - cantidad);
        medicamentoService.guardarMedicamento(medicamento);
    }

    private void eliminarMedicamento() {
        Compra selectedCompra = compraGrid.asSingleSelect().getValue();
        if (selectedCompra != null) {
            listaCompras.remove(selectedCompra);
            actualizarStockMedicamento(selectedCompra.getMedicamento(), -selectedCompra.getCantidad());
            actualizarPrecioTotal(); 
            compraGrid.getDataProvider().refreshAll();
        }
    }

    private void actualizarPrecioTotal() {
        precioTotal = listaCompras.stream()
                .mapToDouble(compra -> compra.getCantidad() * compra.getPrecioUnidad())
                .sum();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        String formattedPrecioTotal = decimalFormat.format(precioTotal);

        if (precioTotalLabel != null) {
            remove(precioTotalLabel);
        }

        precioTotalLabel = new Button();
        precioTotalLabel.setText("Precio Total: " + formattedPrecioTotal + "€");
        add(precioTotalLabel);
    }

    @SuppressWarnings("unused")
	private void finalizarCompra() {
        if (!listaCompras.isEmpty()) {
            boolean stockSuficiente = true;

            for (Compra compra : listaCompras) {
                Medicamento medicamento = compra.getMedicamento();
                int cantidadCompra = compra.getCantidad();

                if (cantidadCompra > medicamento.getStockDisponible()) {
                    stockSuficiente = false;
                    break;
                }
            }

            if (stockSuficiente) {
                double precioTotal = listaCompras.stream()
                        .mapToDouble(compra -> compra.getCantidad() * compra.getPrecioUnidad())
                        .sum();
                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(false);
                dialog.setCloseOnOutsideClick(false);

                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setSpacing(true);
                dialogLayout.setPadding(true);

                Text confirmText = new Text("¿Desea confirmar la compra?");
                dialogLayout.add(confirmText);

                HorizontalLayout buttonsLayout = new HorizontalLayout();
                buttonsLayout.setSpacing(true);

                Button confirmButton = new Button("Confirmar");
                confirmButton.addClickListener(e -> {
                    for (Compra compra : listaCompras) {
                        compraService.guardarCompra(compra);
                    }

                    listaCompras.clear();
                    actualizarPrecioTotal(); 
                    compraGrid.getDataProvider().refreshAll();

                    dialog.close();
                    mostrarNotificacionExito("Compra realizada con éxito");
                });

                Button cancelButton = new Button("Cancelar");
                cancelButton.addClickListener(e -> dialog.close());

                buttonsLayout.add(confirmButton, cancelButton);
                dialogLayout.add(buttonsLayout);

                dialog.add(dialogLayout);
                dialog.open();
            }
                for (Compra compra : listaCompras) {
                    Medicamento medicamento = compra.getMedicamento();
                    medicamento.setStockDisponible(medicamento.getStockDisponible() - compra.getCantidad());
                    medicamentoService.guardarMedicamento(medicamento);
                    compra.setFechaCompra(new Date());
                    compra.setPrecioTotal(precioTotal);
                    compra.setCantidad(compra.getCantidad());
                    compra.setMedicamento(medicamento);
                    compraService.guardarCompra(compra);
                }

                listaCompras.clear(); 
                actualizarPrecioTotal(); 
                compraGrid.getDataProvider().refreshAll();
            }
    }
    
    private void mostrarNotificacionError(String mensaje) {
        Dialog dialog = new Dialog();
        dialog.add(new Text(mensaje));
        dialog.open();
    }

    private void mostrarNotificacionExito(String mensaje) {
        Notification.show(mensaje, 3000, Notification.Position.MIDDLE);
    }  
}
