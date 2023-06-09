package es.uca.iw.farmacia.data.service;

import java.util.List;


import org.springframework.stereotype.Service;

import es.uca.iw.farmacia.data.entity.Compra;

@Service
public class CompraService {
  private CompraRepository compraRepository;

  public CompraService(CompraRepository compraRepository) {
    this.compraRepository = compraRepository;
  }

  public List<Compra> listarCompras() {
    return compraRepository.findAll();
  }

  public void guardarCompra(Compra compra) {
    compraRepository.save(compra);
  }

  public void eliminarCompra(Long id) {
    compraRepository.deleteById(id);
  }

  public List<Compra> getComprasByMedicamentoId(Long medicamentoId) {
    return compraRepository.findByMedicamentoId(medicamentoId);
  }

}
