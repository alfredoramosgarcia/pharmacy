package es.uca.iw.farmacia.views.caja;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;

import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@PageTitle("Caja Registradora")
@Route(value = "caja", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class CajaView extends VerticalLayout {

    private Grid<Medicamento> medicamentoGrid;
    private List<Medicamento> listaCaja;
    private Binder<Medicamento> binder;
    private MedicamentoService medicamentoService;
    private double precioTotal;

    public CajaView() {
        listaCaja = new ArrayList<>();
        binder = new Binder<>(Medicamento.class);
        precioTotal = 0.0; // Inicializar el precio total en 0.0

        medicamentoGrid = new Grid<>(Medicamento.class);
        medicamentoGrid.setColumns("codigoNacional", "nombreComercial", "composicion", "categoria", "formaFarmaceutica", "stockDisponible" , "precioPorUnidad");
        medicamentoGrid.setItems(listaCaja);

        IntegerField cantidadField = new IntegerField("Cantidad");
        Button agregarButton = new Button("Agregar a la caja");
        agregarButton.addClickListener(e -> agregarMedicamento(cantidadField.getValue()));

        add(medicamentoGrid, cantidadField, agregarButton);
    }

    private void agregarMedicamento(Integer cantidad) {
        Medicamento medicamentoSeleccionado = medicamentoGrid.getSelectedItems().stream().findFirst().orElse(null);
        if (medicamentoSeleccionado != null && cantidad != null && cantidad > 0) {
            Medicamento medicamentoCaja = new Medicamento(medicamentoSeleccionado);
            medicamentoCaja.setStockDisponible(cantidad);
            listaCaja.add(medicamentoCaja);
            actualizarStockMedicamento(medicamentoSeleccionado, cantidad);
            actualizarPrecioTotal(); // Actualizar el precio total
            medicamentoGrid.getDataProvider().refreshAll();
        }
    }

    private void actualizarStockMedicamento(Medicamento medicamento, Integer cantidad) {
        medicamento.setStockDisponible(medicamento.getStockDisponible() - cantidad);
    }

    private void actualizarPrecioTotal() {
        precioTotal = listaCaja.stream()
                .mapToDouble(medicamento -> medicamento.getPrecioPorUnidad() * medicamento.getStockDisponible())
                .sum();
        Button precioTotalLabel = null;
		// Actualizar la visualización del precio total en la interfaz de usuario
        // (puede ser un componente Label o cualquier otro)
        // por ejemplo:
        precioTotalLabel.setText("Precio Total: " + precioTotal + "€");
    }
}