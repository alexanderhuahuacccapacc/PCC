package com.pcge.pcgebackend.repository;

import com.pcge.pcgebackend.model.MovimientoContable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface MovimientoContableRepository extends JpaRepository<MovimientoContable, Long> {
    List<MovimientoContable> findByCuentaCodigo(String codigoCuenta);

    @Query("SELECT COALESCE(SUM(m.debe), 0) - COALESCE(SUM(m.haber), 0) FROM MovimientoContable m WHERE m.cuenta.codigo = :codigoCuenta")
    BigDecimal calcularSaldoCuenta(@Param("codigoCuenta") String codigoCuenta);

    @Query("SELECT m FROM MovimientoContable m WHERE m.cuenta.codigo LIKE :codigoCuenta%")
    List<MovimientoContable> findByCuentaStartingWith(@Param("codigoCuenta") String codigoCuenta);
}
