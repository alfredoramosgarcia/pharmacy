package es.uca.iw.farmacia.views.medicamentos;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import com.vaadin.flow.router.PageTitle;

import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "medicamentos", layout = MainLayout.class)
@PermitAll
@AnonymousAllowed
public class MedicamentosView extends VerticalLayout {

    private MedicamentoService medicamentoService;

    private Grid<Medicamento> grid;

    public MedicamentosView(MedicamentoService medicamentoService) {
    	
    	this.medicamentoService = medicamentoService;

        // Crear un grid para mostrar los medicamentos
        grid = new Grid<>(Medicamento.class);

        // Obtener los medicamentos desde el servicio
        List<Medicamento> medicamentos = medicamentoService.obtenerTodosLosMedicamentos();
        System.out.print(medicamentos.isEmpty());

        // Agregar los medicamentos al grid
        grid.setItems(medicamentos);

        // Configurar las columnas del grid
        grid.setColumns("id", "codigoNacional", "nombreComercial", "composicion", "categoria", "formaFarmaceutica", "stockDisponible", "precioPorUnidad");

        // Agregar el grid al layout
        add(grid);
    }
}
