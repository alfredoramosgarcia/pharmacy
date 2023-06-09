package es.uca.iw.farmacia.views.detalleMedicamento;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import es.uca.iw.farmacia.views.medicamentos.MedicamentosView;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "detalle-medicamento", layout = MainLayout.class)
@PermitAll
public class DetalleMedicamentoView extends VerticalLayout implements HasUrlParameter<Long> {

    private MedicamentoService medicamentoService;
    private Medicamento medicamento;

    private TextField codigoNacionalField;
    private TextField nombreComercialField;
    private TextField composicionField;
    private TextField categoriaField;
    private TextField stockDisponibleField;
    private TextField precioUnidadField;
    private int stock;
    private double precio;

    public DetalleMedicamentoView(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;

        codigoNacionalField = new TextField("Código Nacional");
        nombreComercialField = new TextField("Nombre Comercial");
        composicionField = new TextField("Composicion");
        categoriaField = new TextField("Categoria");
        stockDisponibleField =  new TextField("Stock Disponible");
        precioUnidadField =  new TextField("Precio por unidad");

        Button guardarButton = new Button("Guardar");
        guardarButton.addClickListener(e -> {
            guardarMedicamento();
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(codigoNacionalField, nombreComercialField,composicionField,categoriaField, stockDisponibleField, precioUnidadField,   guardarButton);

        add(formLayout);
    }

    private void guardarMedicamento() {
        medicamento.setCodigoNacional(codigoNacionalField.getValue());
        medicamento.setNombreComercial(nombreComercialField.getValue());
        medicamento.setComposicion(composicionField.getValue());
        medicamento.setCategoria(categoriaField.getValue());
        medicamento.setStockDisponible(Integer.parseInt(stockDisponibleField.getValue()));
        medicamento.setPrecioPorUnidad(Double.parseDouble(precioUnidadField.getValue()));
        

        medicamentoService.guardarMedicamento(medicamento);
        Notification.show("Medicamento guardado correctamente", 3000, Notification.Position.MIDDLE);

        UI.getCurrent().navigate(MedicamentosView.class);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id == null) {
            medicamento = new Medicamento();
        } else {
            medicamento = medicamentoService.obtenerMedicamentoPorId(id);
            if (medicamento == null) {
                medicamento = new Medicamento();
                Notification.show("El medicamento no existe", 3000, Notification.Position.MIDDLE);
                UI.getCurrent().navigate(MedicamentosView.class);
            } else {
                codigoNacionalField.setValue(medicamento.getCodigoNacional());
                nombreComercialField.setValue(medicamento.getNombreComercial());
                composicionField.setValue(medicamento.getComposicion());
                categoriaField.setValue(medicamento.getCategoria());
                stockDisponibleField.setValue(String.valueOf(medicamento.getStockDisponible()));
                precioUnidadField.setValue(String.valueOf(medicamento.getPrecioPorUnidad()));
                
            
            }
        }
    }
}
