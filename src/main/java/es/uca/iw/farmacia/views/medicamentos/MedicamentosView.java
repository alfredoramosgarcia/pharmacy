package es.uca.iw.farmacia.views.medicamentos;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import java.util.List;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "medicamentos", layout = MainLayout.class)
@PermitAll
public class MedicamentosView extends VerticalLayout {
	
	@PersistenceContext
	private EntityManager entityManager;

	private Grid<Medicamento> grid;

	public MedicamentosView(EntityManager entityManager) {
		this.entityManager = entityManager;

		// Crear un grid para mostrar los medicamentos
		grid = new Grid<>(Medicamento.class);

		// Obtener los medicamentos desde la base de datos
		List<Medicamento> medicamentos = obtenerMedicamentosDesdeBaseDeDatos();
		
		System.out.print(medicamentos.isEmpty());

		// Agregar los medicamentos al grid
		grid.setItems(medicamentos);

		// Configurar las columnas del grid
		grid.setColumns("id", "codigoNacional", "nombreComercial", "composicion", "categoria", "formaFarmaceutica", "stockDisponible");

		// Agregar el grid al layout
		add(grid);
	}

	private List<Medicamento> obtenerMedicamentosDesdeBaseDeDatos() {
		String jpql = "SELECT m FROM Medicamento m";
		jakarta.persistence.TypedQuery<Medicamento> query = entityManager.createQuery(jpql, Medicamento.class);
	
		return query.getResultList();
	}
}
