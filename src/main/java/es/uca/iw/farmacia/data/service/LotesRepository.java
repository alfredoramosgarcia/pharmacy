package es.uca.iw.farmacia.data.service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uca.iw.farmacia.data.entity.Lotes;

@Repository
public interface LotesRepository extends JpaRepository<Lotes, Integer> {
}
