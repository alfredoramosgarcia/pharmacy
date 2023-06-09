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
    private Button precioTotalLabel; // Store the total price label reference

    public CajaView(MedicamentoService medicamentoService, CompraService compraService) {
    	this.compraService = compraService;
        this.medicamentoService = medicamentoService;
        listaCompras = new ArrayList<>();
        precioTotal = 0.0; // Initialize the total price to 0.0

        compraGrid = new Grid<>(Compra.class);
        compraGrid.setColumns("nombreComercial", "cantidad");
        compraGrid.addColumn(medicamento -> medicamento.getPrecioUnidad() + " €")
                .setHeader("Precio Unidad");
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

            if(cantidad <= medicamento.getStockDisponible()) actualizarStockMedicamento(medicamento, cantidad);
            else { mostrarNotificacionError("No hay stock suficiente para la compra");
            return;}
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
             // Create a dialog to confirm the purchase
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
                    actualizarPrecioTotal(); // Reset the total price
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

                listaCompras.clear(); // Clear the list of purchases
                actualizarPrecioTotal(); // Reset the total price
                compraGrid.getDataProvider().refreshAll(); // Refresh the grid
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
