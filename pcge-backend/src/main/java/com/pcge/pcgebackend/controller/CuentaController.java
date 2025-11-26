package com.pcge.pcgebackend.controller;

import com.pcge.pcgebackend.model.Cuenta;
import com.pcge.pcgebackend.repository.CuentaRepository;
import com.pcge.pcgebackend.service.CuentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public List<Cuenta> listarTodas() {
        return cuentaService.listarTodas();
    }

    @GetMapping("/{codigo}")
    public Cuenta obtenerPorCodigo(@PathVariable String codigo) {
        return cuentaService.obtenerPorCodigo(codigo).orElse(null);
    }

    @GetMapping("/buscar")
    public List<Cuenta> buscarPorNombre(@RequestParam String nombre) {
        return cuentaService.buscarPorNombre(nombre);
    }

    @GetMapping("/subcuentas/{padreId}")
    public List<Cuenta> obtenerSubcuentas(@PathVariable String padreId) {
        return cuentaService.obtenerSubcuentas(padreId);
    }
}
