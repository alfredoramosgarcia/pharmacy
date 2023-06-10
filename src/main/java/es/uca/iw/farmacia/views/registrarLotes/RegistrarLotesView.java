package es.uca.iw.farmacia.views.registrarLotes;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import es.uca.iw.farmacia.views.registrarLotes.RegistroLoteView;

import es.uca.iw.farmacia.data.entity.Lotes;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.entity.User;
import es.uca.iw.farmacia.data.service.LotesService;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
import es.uca.iw.farmacia.views.MainLayout.MenuItemInfo;
import es.uca.iw.farmacia.views.caja.CajaView;
import es.uca.iw.farmacia.views.compras.CompraView;
import es.uca.iw.farmacia.views.medicamentos.MedicamentosView;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "registrar-lotes", layout = MainLayout.class)
@PermitAll
public class RegistrarLotesView extends VerticalLayout {

   
	private LotesService loteService;
    private Button opcion1Button;
    private Button opcion2Button;
    private Grid<Lotes> grid;
    private VerticalLayout contenido;
    private RegistroLoteView registroLote;
    private HistorialLotesView historialLotes;
   	private MedicamentoService medicamentoService;

    private boolean opcion1Seleccionada = true;
    private boolean opcion2Seleccionada = false;

    @Autowired
    public RegistrarLotesView(LotesService loteService,  HistorialLotesView historialLotes, RegistroLoteView registroLote, MedicamentoService medicamentoService) {
    	this.loteService = loteService;
    	this.historialLotes = historialLotes;
    	this.registroLote = registroLote;
    	this.medicamentoService = medicamentoService;
      
    	
        Header header = new Header();
       
        
        
        
        opcion1Button = new Button("Registro");
        
        //opcion1Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Opción 1 seleccionada por defecto
        opcion1Button.getStyle().set("margin-right", "10px"); // Agregar margen derecho
        opcion1Button.getStyle().set("box-shadow", "none"); // Quitar contorno

        opcion2Button = new Button("Historial");
       
        opcion2Button.getStyle().set("margin-left", "10px"); // Agregar margen izquierdo
        opcion2Button.getStyle().set("box-shadow", "none"); // Quitar contorno
        opcion2Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Opción 1 seleccionada por defecto
        header.add(opcion1Button, opcion2Button);
        add(header);
        
        historialLotes();
        opcion1Button.addClickListener(event -> {
            opcion1Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            opcion2Button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            add(registroLote);
            remove(grid);
            
            
        });
        
        opcion2Button.addClickListener(event -> {
            opcion2Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            opcion1Button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            remove(registroLote);
            historialLotes();
            
        });

       
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        
    }
    
    private void historialLotes() {
    	
    	grid = new Grid<>(Lotes.class);
    	grid.setColumns("id", "distribuidor", "medicamento", "cantidad");
    	        
    	List<Lotes> lotes = loteService.obtenerTodosLosLotes();
    	grid.setItems(lotes);
    	add(grid);
   }
    
    private void registroLotes() {
    	 
         
    }
    
}
