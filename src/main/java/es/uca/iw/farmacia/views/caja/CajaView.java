package es.uca.iw.farmacia.views.caja;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
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
@PageTitle("Caja Registradora")
@Route(value = "caja", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class CajaView extends VerticalLayout {

    private Grid<Compra> compraGrid;
    private List<Compra> listaCompras;
    private Binder<Compra> binder;
    private MedicamentoService medicamentoService;
    private CompraService compraService;
    private double precioTotal;
    private Button precioTotalLabel; // Store the total price label reference

    public CajaView(MedicamentoService medicamentoService, CompraService compraService) {
    	this.compraService = compraService;
        this.medicamentoService = medicamentoService;
        listaCompras = new ArrayList<>();
        binder = new Binder<>(Compra.class);
        precioTotal = 0.0; // Initialize the total price to 0.0

        compraGrid = new Grid<>(Compra.class);
        compraGrid.setColumns("nombreComercial", "cantidad", "precioUnidad");
        compraGrid.setItems(listaCompras);

        ComboBox<Medicamento> medicamentoComboBox = new ComboBox<>("Medicamento");
        medicamentoComboBox.setItemLabelGenerator(Medicamento::getNombreComercial);
        medicamentoComboBox.setItems(medicamentoService.obtenerTodosLosMedicamentos());

        IntegerField cantidadField = new IntegerField("Cantidad");
        Button agregarButton = new Button("Agregar a la caja");
        agregarButton.addClickListener(e -> agregarMedicamento(medicamentoComboBox.getValue(), cantidadField.getValue()));

        Button eliminarButton = new Button("Eliminar");
        eliminarButton.addClickListener(e -> eliminarMedicamento());

        HorizontalLayout filtroLayout = new HorizontalLayout(medicamentoComboBox, cantidadField, agregarButton, eliminarButton);
        filtroLayout.setVerticalComponentAlignment(Alignment.END, medicamentoComboBox);
        filtroLayout.setAlignSelf(Alignment.END, cantidadField);
        filtroLayout.setAlignSelf(Alignment.END, agregarButton);
        filtroLayout.setAlignSelf(Alignment.END, eliminarButton);
        add(filtroLayout, compraGrid);

        Button finalizarCompraButton = new Button("Finalizar compra");
        finalizarCompraButton.addClickListener(e -> finalizarCompra());
        finalizarCompraButton.getStyle().set("background-color", "red");
        finalizarCompraButton.getStyle().set("color", "white");
        
        add(finalizarCompraButton);
        actualizarPrecioTotal(); // Initialize the total price label
        
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

            actualizarStockMedicamento(medicamento, cantidad);
            actualizarPrecioTotal(); // Update the total price
            compraGrid.getDataProvider().refreshAll();
        }
    }
    
    private void actualizarStockMedicamento(Medicamento medicamento, Integer cantidad) {
        medicamento.setStockDisponible(medicamento.getStockDisponible() - cantidad);
        // Update the medication's stock in the database using the service
        medicamentoService.guardarMedicamento(medicamento);
    }

    private void eliminarMedicamento() {
        Compra selectedCompra = compraGrid.asSingleSelect().getValue();
        if (selectedCompra != null) {
            listaCompras.remove(selectedCompra);
            actualizarStockMedicamento(selectedCompra.getMedicamento(), -selectedCompra.getCantidad());
            actualizarPrecioTotal(); // Update the total price
            compraGrid.getDataProvider().refreshAll();
        }
    }

    private void actualizarPrecioTotal() {
        precioTotal = listaCompras.stream()
                .mapToDouble(compra -> compra.getCantidad() * compra.getPrecioUnidad())
                .sum();

        // Format the price value
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        String formattedPrecioTotal = decimalFormat.format(precioTotal);

        // Remove the previous total price label if it exists
        if (precioTotalLabel != null) {
            remove(precioTotalLabel);
        }

        // Create a new total price label with the updated value
        precioTotalLabel = new Button();
        precioTotalLabel.setText("Precio Total: " + formattedPrecioTotal + "€");
        add(precioTotalLabel);
    }

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

                listaCompras.clear(); // Clear the list of purchases
                actualizarPrecioTotal(); // Reset the total price
                compraGrid.getDataProvider().refreshAll(); // Refresh the grid
            } else {
                // Mostrar un mensaje de error al usuario indicando que no hay suficiente stock disponible
                // Puedes usar una notificación de Vaadin para esto
                Notification.show("No hay suficiente stock disponible para completar la compra.", 3000, Notification.Position.MIDDLE);
            }
        }
    }

    
}
