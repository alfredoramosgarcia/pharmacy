package es.uca.iw.farmacia.views.medicamentos;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import com.vaadin.flow.router.PageTitle;

import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import es.uca.iw.farmacia.views.agregarMedicamento.AgregarMedicamentoView;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "medicamentos", layout = MainLayout.class)
@PermitAll
public class MedicamentosView extends VerticalLayout {

    private MedicamentoService medicamentoService;

    private Grid<Medicamento> grid;

    public MedicamentosView(MedicamentoService medicamentoService) {
    	
    	this.medicamentoService = medicamentoService;
    	Button agregarMedicamentoButton = new Button("Agregar Medicamento", VaadinIcon.PLUS.create());
        agregarMedicamentoButton.addClickListener(e -> {
            UI.getCurrent().navigate(AgregarMedicamentoView.class);
        });
        
        add(agregarMedicamentoButton);

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
        add(agregarMedicamentoButton);
        add(grid);
    }
}
