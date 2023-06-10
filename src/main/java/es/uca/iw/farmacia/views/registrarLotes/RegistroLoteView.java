package es.uca.iw.farmacia.views.registrarLotes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import es.uca.iw.farmacia.data.entity.Lotes;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.LotesService;
import es.uca.iw.farmacia.data.service.MedicamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("serial")
@Component
public class RegistroLoteView extends VerticalLayout {
    private LotesService loteService;
	private MedicamentoService medicamentoService;
	private HistorialLotesView historialLotes;

    @Autowired
    public RegistroLoteView(LotesService loteService, MedicamentoService medicamentoService, HistorialLotesView historialLotes) {
        this.loteService = loteService;
        this.medicamentoService = medicamentoService;
        this.historialLotes = historialLotes;

   
        FormLayout formLayout = new FormLayout();

        ComboBox<Medicamento> medicamentoComboBox = new ComboBox<>("Medicamento");
        medicamentoComboBox.setItemLabelGenerator(Medicamento::getNombreComercial);
        medicamentoComboBox.setItems(medicamentoService.obtenerTodosLosMedicamentos());
        formLayout.add(medicamentoComboBox);

        TextField distribuidorField = new TextField("Distribuidor");
        distribuidorField.setRequired(true);
        formLayout.add(distribuidorField);

        IntegerField cantidadField = new IntegerField("Cantidad");
        cantidadField.setRequired(true);
        formLayout.add(cantidadField);

        Button guardarButton = new Button("Guardar");
        guardarButton.addClickListener(event -> {
        	Medicamento medicamentoS = medicamentoComboBox.getValue();
            String medicamentoSeleccionado = medicamentoS.getNombreComercial();
            String distribuidor = distribuidorField.getValue();
            int cantidad = cantidadField.getValue();

            if (medicamentoSeleccionado != null && distribuidor != null && !distribuidor.isEmpty() && cantidad > 0) {
                // Obtener el medicamento seleccionado por su nombre
                Medicamento medicamento = medicamentoService.obtenerMedicamentoPorNombre(medicamentoSeleccionado);
                if (medicamento != null) {
                    // Crear el objeto Lote con los datos ingresados
                    Lotes lote = new Lotes();
                    lote.setMedicamento(medicamento.getNombreComercial());
                    lote.setDistribuidor(distribuidor);
                    lote.setCantidad(cantidad);

                    // Guardar el lote utilizando el servicio
                    loteService.guardarLote(lote);
                    medicamento.setStockDisponible(medicamento.getStockDisponible() + cantidad);
                    medicamentoService.guardarMedicamento(medicamento);
                  

                    Notification.show("Lote registrado correctamente", 3000, Notification.Position.MIDDLE);
                } else {
                    Notification.show("El medicamento seleccionado no existe", 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Completa todos los campos correctamente", 3000, Notification.Position.MIDDLE);
            }
        });
        formLayout.add(guardarButton);

        add(formLayout);
    }
}