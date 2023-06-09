package es.uca.iw.farmacia.data.service;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.iw.farmacia.data.entity.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

	List<Compra> findByMedicamentoId(Long medicamentoId);
}
