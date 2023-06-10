package es.uca.iw.farmacia.data.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.farmacia.data.entity.Lotes;

import java.util.List;

@Service
public class LotesService {
    private LotesRepository loteRepository;

    @Autowired
    public LotesService(LotesRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public void guardarLote(Lotes lote) {
        loteRepository.save(lote);
    }

    public void actualizarLote(Lotes lote) {
        loteRepository.save(lote);
    }

    public void eliminarLote(int id) {
        loteRepository.deleteById(id);
    }

    public Lotes obtenerLotePorId(int id) {
        return loteRepository.findById(id).orElse(null);
    }

    public List<Lotes> obtenerTodosLosLotes() {
        return loteRepository.findAll();
    }
}
