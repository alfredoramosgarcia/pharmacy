package es.uca.iw.farmacia.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.iw.farmacia.data.entity.Medicamento;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    // Aquí puedes agregar consultas personalizadas si lo necesitas
}
