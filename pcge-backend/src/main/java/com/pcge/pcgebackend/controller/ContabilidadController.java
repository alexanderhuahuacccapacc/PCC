package com.pcge.pcgebackend.controller;
import com.pcge.pcgebackend.dto.VentaContadoRequest;
import com.pcge.pcgebackend.dto.VentaCreditoRequest;
import com.pcge.pcgebackend.model.AsientoContable;
import com.pcge.pcgebackend.model.Comprobante;
import com.pcge.pcgebackend.model.MovimientoContable;
import com.pcge.pcgebackend.service.ContabilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/contabilidad")
@CrossOrigin(origins = "*")
public class ContabilidadController {
    private final ContabilidadService contabilidadService;

    public ContabilidadController(ContabilidadService contabilidadService) {
        this.contabilidadService = contabilidadService;
    }

    @PostMapping("/venta-contado")
    public ResponseEntity<AsientoContable> registrarVentaContado(@RequestBody VentaContadoRequest request) {
        try {
            AsientoContable asiento = contabilidadService.registrarVentaContado(request);
            return ResponseEntity.ok(asiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/venta-credito")
    public ResponseEntity<AsientoContable> registrarVentaCredito(@RequestBody VentaCreditoRequest request) {
        try {
            AsientoContable asiento = contabilidadService.registrarVentaCredito(request);
            return ResponseEntity.ok(asiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/comprobantes")
    public ResponseEntity<List<Comprobante>> obtenerTodosComprobantes() {
        try {
            List<Comprobante> comprobantes = contabilidadService.obtenerTodosComprobantes();
            return ResponseEntity.ok(comprobantes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/libro-mayor/{codigoCuenta}")
    public ResponseEntity<List<MovimientoContable>> obtenerLibroMayor(@PathVariable String codigoCuenta) {
        List<MovimientoContable> movimientos = contabilidadService.obtenerLibroMayor(codigoCuenta);
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/saldo/{codigoCuenta}")
    public ResponseEntity<BigDecimal> obtenerSaldoCuenta(@PathVariable String codigoCuenta) {
        BigDecimal saldo = contabilidadService.obtenerSaldoCuenta(codigoCuenta);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/asientos")
    public ResponseEntity<List<AsientoContable>> obtenerTodosAsientos() {
        List<AsientoContable> asientos = contabilidadService.obtenerTodosAsientos();
        return ResponseEntity.ok(asientos);
    }
}
