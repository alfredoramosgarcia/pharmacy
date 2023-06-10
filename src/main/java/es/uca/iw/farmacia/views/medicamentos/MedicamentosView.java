package es.uca.iw.farmacia.views.medicamentos;

import com.vaadin.flow.component.UI;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import es.uca.iw.farmacia.views.agregarMedicamento.AgregarMedicamentoView;
import es.uca.iw.farmacia.views.detalleMedicamento.DetalleMedicamentoView;

import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "medicamentos", layout = MainLayout.class)
@PermitAll
public class MedicamentosView extends VerticalLayout {

    private MedicamentoService medicamentoService;
    private Grid<Medicamento> grid;

    @SuppressWarnings("unused")
	public MedicamentosView(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;

        Button agregarMedicamentoButton = new Button("Agregar Medicamento", VaadinIcon.PLUS.create());
        agregarMedicamentoButton.addClickListener(e -> {
            UI.getCurrent().navigate(AgregarMedicamentoView.class);
        });

        TextField buscarField = new TextField("Buscar por nombre");
        Button buscarButton = new Button("Buscar", VaadinIcon.SEARCH.create());
        buscarButton.addClickListener(e -> {
            String nombre = buscarField.getValue();
            List<Medicamento> medicamentosFiltrados = medicamentoService.buscarPorNombre(nombre);
            grid.setItems(medicamentosFiltrados);
        });

        HorizontalLayout filtroLayout = new HorizontalLayout(buscarField, buscarButton);
        filtroLayout.setVerticalComponentAlignment(Alignment.END, buscarButton);
        filtroLayout.setAlignSelf(Alignment.END, buscarButton);

        grid = new Grid<>(Medicamento.class);
        List<Medicamento> medicamentos = medicamentoService.obtenerTodosLosMedicamentos();

        grid.setColumns("id", "codigoNacional", "nombreComercial", "composicion", "categoria", "formaFarmaceutica",
                "stockDisponible");

        grid.addColumn(medicamento -> medicamento.getPrecioPorUnidad() + " €")
                .setHeader("Precio Unidad");
        Grid.Column<Medicamento> columnaBorrar = grid.addComponentColumn(medicamento -> {
            Button botonBorrar = new Button(new Icon(VaadinIcon.TRASH));
            botonBorrar.addClickListener(event -> {
                medicamentoService.eliminarMedicamento(medicamento.getId());
                actualizarGrid();
            });
            return botonBorrar;
        });

        Grid.Column<Medicamento> columnaModificar = grid.addComponentColumn(medicamento -> {
            Button botonModificar = new Button(new Icon(VaadinIcon.EDIT));
            botonModificar.addClickListener(event -> {
                UI.getCurrent().navigate(DetalleMedicamentoView.class, medicamento.getId());
            });
            return botonModificar;
        });
        add(filtroLayout, grid, agregarMedicamentoButton);
        actualizarGrid();
    }

    private void actualizarGrid() {
        List<Medicamento> medicamentos = medicamentoService.obtenerTodosLosMedicamentos();
        grid.setItems(medicamentos);
    }
}
