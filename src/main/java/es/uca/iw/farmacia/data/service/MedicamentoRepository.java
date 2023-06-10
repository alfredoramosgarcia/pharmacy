package es.uca.iw.farmacia.data.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.iw.farmacia.data.entity.Medicamento;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

	List<Medicamento> findByNombreComercialContainingIgnoreCase(String nombreComercial);
	
	Medicamento findByNombreComercial(String nombreComercial);


}
