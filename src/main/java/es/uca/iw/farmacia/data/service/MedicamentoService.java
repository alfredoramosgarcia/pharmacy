package es.uca.iw.farmacia.data.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import es.uca.iw.farmacia.data.entity.Medicamento;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    private MedicamentoRepository medicamentoRepository;

    @Autowired
    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<Medicamento> obtenerTodosLosMedicamentos() {
        return medicamentoRepository.findAll();
    }

    public Medicamento guardarMedicamento(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public void eliminarMedicamento(Long medicamentoId) {
        medicamentoRepository.deleteById(medicamentoId);
    }
    
    public Medicamento obtenerMedicamentoPorId(Long id) {
        Optional<Medicamento> optionalMedicamento = medicamentoRepository.findById(id);
        return optionalMedicamento.orElse(null);
    }
    
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreComercialContainingIgnoreCase(nombre);
    }

    public Medicamento obtenerMedicamentoPorNombre(String medicamentoSeleccionado) {
        return medicamentoRepository.findByNombreComercial(medicamentoSeleccionado);     
    }
}
