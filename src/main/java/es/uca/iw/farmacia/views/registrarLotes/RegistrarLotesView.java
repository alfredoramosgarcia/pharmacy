package es.uca.iw.farmacia.views.registrarLotes;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import es.uca.iw.farmacia.data.entity.Lotes;
import es.uca.iw.farmacia.data.service.LotesService;
import es.uca.iw.farmacia.data.service.MedicamentoService;
import es.uca.iw.farmacia.views.MainLayout;
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

    @Autowired
    public RegistrarLotesView(LotesService loteService, RegistroLoteView registroLote, MedicamentoService medicamentoService) {
    	this.loteService = loteService;
          	
        Header header = new Header();
      
        opcion1Button = new Button("Registro");
        opcion1Button.getStyle().set("margin-right", "10px"); 
        opcion1Button.getStyle().set("box-shadow", "none"); 

        opcion2Button = new Button("Historial");
       
        opcion2Button.getStyle().set("margin-left", "10px"); 
        opcion2Button.getStyle().set("box-shadow", "none"); 
        opcion2Button.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
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
    
}
