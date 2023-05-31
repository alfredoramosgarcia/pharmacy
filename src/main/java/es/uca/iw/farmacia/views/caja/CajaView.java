package es.uca.iw.farmacia.views.caja;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;

@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "caja", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class CajaView extends VerticalLayout {

    public CajaView() {
    	
    }
    
   

}
