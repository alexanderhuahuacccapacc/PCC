package com.pcge.pcgebackend.repository;

import com.pcge.pcgebackend.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    @Query("SELECT COALESCE(MAX(c.numeroOperacion), 0) FROM Comprobante c")
    Long findMaxNumeroOperacion();

    List<Comprobante> findAllByOrderByNumeroOperacionDesc();
}
