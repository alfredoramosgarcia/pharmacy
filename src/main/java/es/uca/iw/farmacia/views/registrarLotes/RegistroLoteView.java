package es.uca.iw.farmacia.views.registrarLotes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import es.uca.iw.farmacia.data.entity.Lotes;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.LotesService;
import es.uca.iw.farmacia.data.service.MedicamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class RegistroLoteView extends VerticalLayout {
    @SuppressWarnings("unused")
	private LotesService loteService;
	@SuppressWarnings("unused")
	private MedicamentoService medicamentoService;

    @Autowired
    public RegistroLoteView(LotesService loteService, MedicamentoService medicamentoService) {
        this.loteService = loteService;
        this.medicamentoService = medicamentoService;

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
                Medicamento medicamento = medicamentoService.obtenerMedicamentoPorNombre(medicamentoSeleccionado);
                if (medicamento != null) {
                    Lotes lote = new Lotes();
                    lote.setMedicamento(medicamento.getNombreComercial());
                    lote.setDistribuidor(distribuidor);
                    lote.setCantidad(cantidad);

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