package com.pcge.pcgebackend.service;

import com.pcge.pcgebackend.model.Cuenta;
import com.pcge.pcgebackend.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> listarTodas() {
        return cuentaRepository.findAll();
    }

    public Optional<Cuenta> obtenerPorCodigo(String codigo) {
        return cuentaRepository.findById(codigo);
    }

    public List<Cuenta> buscarPorNombre(String nombre) {
        return cuentaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Cuenta> obtenerSubcuentas(String padreId) {
        return cuentaRepository.findByPadreId(padreId);
    }
}