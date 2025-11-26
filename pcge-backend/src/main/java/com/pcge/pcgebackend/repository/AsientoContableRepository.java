package com.pcge.pcgebackend.repository;

import com.pcge.pcgebackend.model.AsientoContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AsientoContableRepository extends JpaRepository<AsientoContable, Long> {
    List<AsientoContable> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT a FROM AsientoContable a ORDER BY a.fecha DESC")
    List<AsientoContable> findAllOrderByFechaDesc();
}
