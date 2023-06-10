package es.uca.iw.farmacia.views.registrarLotes;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import es.uca.iw.farmacia.data.entity.Lotes;
import es.uca.iw.farmacia.data.service.LotesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistorialLotesView extends VerticalLayout {
    private LotesService loteService;
    private Grid<Lotes> grid;

    @Autowired
    public HistorialLotesView(LotesService loteService) {
        this.loteService = loteService;
        
        grid = new Grid<>(Lotes.class);
        grid.setColumns("id", "distribuidor", "medicamento", "cantidad");
        
        List<Lotes> lotes = loteService.obtenerTodosLosLotes();
        grid.setItems(lotes);
        add(grid);
    }
}