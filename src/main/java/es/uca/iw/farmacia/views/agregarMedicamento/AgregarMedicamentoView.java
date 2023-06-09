package es.uca.iw.farmacia.views.agregarMedicamento;

import com.vaadin.flow.component.UI;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "agregar-medicamento", layout = MainLayout.class)
@PermitAll
public class AgregarMedicamentoView extends FormLayout {

    /**
	 * 
	 */
	private final TextField codigoNacional = new TextField("Código Nacional");
    private final TextField nombreComercial = new TextField("Nombre Comercial");
    private final TextField composicion = new TextField("Composición");
    private final TextField categoria = new TextField("Categoría");
    private final TextField formaFarmaceutica = new TextField("Forma Farmacéutica");
    private final TextField stockDisponible = new TextField("Stock Disponible");
    private final TextField precioPorUnidad = new TextField("Precio por Unidad");

    private final Button guardarButton = new Button("Guardar");

    private final Binder<Medicamento> binder = new Binder<>(Medicamento.class);

    public AgregarMedicamentoView(MedicamentoService medicamentoService) {

        binder.forField(stockDisponible)
                .withConverter(new StringToIntegerConverter("Ingrese un número válido"))
                .bind(Medicamento::getStockDisponible, Medicamento::setStockDisponible);

        binder.forField(precioPorUnidad)
                .withConverter(new StringToDoubleConverter("Ingrese un número válido"))
                .bind(Medicamento::getPrecioPorUnidad, Medicamento::setPrecioPorUnidad);

        add(codigoNacional, nombreComercial, composicion, categoria, formaFarmaceutica, stockDisponible, precioPorUnidad, guardarButton);
        binder.bindInstanceFields(this);

        guardarButton.addClickListener(e -> {
            try {
                Medicamento medicamento = new Medicamento();
                binder.writeBean(medicamento);
                medicamentoService.guardarMedicamento(medicamento);
                Notification.show("Medicamento guardado correctamente");

                UI currentUI = UI.getCurrent();
                if (currentUI != null) {
                    // Navegar a la vista "/medicamentos"
                    currentUI.navigate("medicamentos");}
            } catch (ValidationException ex) {
                Notification.show("Error al guardar el medicamento. Verifique los datos ingresados.", 3000, Notification.Position.MIDDLE);
            }
        });

    }

}
