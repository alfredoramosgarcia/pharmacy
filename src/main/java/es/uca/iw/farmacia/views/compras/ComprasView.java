package es.uca.iw.farmacia.views.compras;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.farmacia.views.MainLayout;
import jakarta.annotation.security.PermitAll;


@SuppressWarnings("serial")
@PageTitle("FARMACIA")
@Route(value = "compras", layout = MainLayout.class)
@PermitAll
public class ComprasView extends VerticalLayout {



    public ComprasView() {
      
    }

}
