package es.uca.iw.farmacia.views.compras;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.mapping.List;
import org.hibernate.mapping.Table;
import org.jsoup.nodes.Document;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import elemental.json.Json;
import es.uca.iw.farmacia.data.entity.Compra;
import es.uca.iw.farmacia.data.entity.Medicamento;
import es.uca.iw.farmacia.data.service.CompraService;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;


@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "compras", layout = MainLayout.class)
@PermitAll
public class CompraView extends VerticalLayout {
	  private CompraService compraService;

	  public CompraView(CompraService compraService) {
	    this.compraService = compraService;

	    Grid<Compra> grid = new Grid<>(Compra.class);
	    grid.setItems(compraService.listarCompras());

	    Button exportButton = new Button("Exportar a PDF");
	    exportButton.addClickListener(e -> {
	      try {
	        exportarComprasAPDF();
	        Notification.show("Las compras se han exportado a PDF y se han descargado localmente.");
	      } catch (IOException ex) {
	        ex.printStackTrace();
	        Notification.show("Error al exportar las compras a PDF.");
	      }
	    });
	    
	    
	    grid.setColumns("id", "medicamento.nombreComercial","fechaCompra",  "cantidad", "precioUnidad", "precio");
	    Grid.Column<Compra> columnaPdf = grid.addComponentColumn(compra -> {
	        Button botonPdf = new Button(new Icon(VaadinIcon.FILE_TEXT));
	        botonPdf.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
	        botonPdf.getStyle().set("padding", "0");
	        botonPdf.getStyle().set("background-color", "transparent");
	        botonPdf.getStyle().set("border", "none");

	        botonPdf.addClickListener(event -> {
	            
	        });

	        return botonPdf;
	    });
	 


	    add(grid);
	  }

	  private void exportarComprasAPDF() throws IOException {
		 
		}
	}