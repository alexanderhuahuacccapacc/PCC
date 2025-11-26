package com.pcge.pcgebackend.repository;

import com.pcge.pcgebackend.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    List<Cuenta> findByNombreContainingIgnoreCase(String nombre);
    List<Cuenta> findByPadreId(String padreId);
}
