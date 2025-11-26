package com.pcge.pcgebackend.service;
import com.pcge.pcgebackend.dto.VentaContadoRequest;
import com.pcge.pcgebackend.model.AsientoContable;
import com.pcge.pcgebackend.model.Cuenta;
import com.pcge.pcgebackend.model.MovimientoContable;
import com.pcge.pcgebackend.repository.AsientoContableRepository;
import com.pcge.pcgebackend.repository.CuentaRepository;
import com.pcge.pcgebackend.repository.MovimientoContableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContabilidadService {
    private final AsientoContableRepository asientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoContableRepository movimientoRepository;

    // Códigos de cuentas según PCGE
    private static final String CUENTA_CAJA = "101";
    private static final String CUENTA_VENTAS = "701";
    private static final String CUENTA_IGV = "40111";
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");

    public ContabilidadService(AsientoContableRepository asientoRepository,
                               CuentaRepository cuentaRepository,
                               MovimientoContableRepository movimientoRepository) {
        this.asientoRepository = asientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional
    public AsientoContable registrarVentaContado(VentaContadoRequest request) {
        // Validar que las cuentas existan
        Cuenta cuentaCaja = obtenerCuentaOError(CUENTA_CAJA, "Cuenta Caja no configurada");
        Cuenta cuentaVentas = obtenerCuentaOError(CUENTA_VENTAS, "Cuenta Ventas no configurada");
        Cuenta cuentaIgv = obtenerCuentaOError(CUENTA_IGV, "Cuenta IGV no configurada");

        // Calcular montos
        BigDecimal montoIgv = request.getMontoBase().multiply(IGV_PORCENTAJE);
        BigDecimal montoTotal = request.getMontoBase().add(montoIgv);

        // Crear asiento
        AsientoContable asiento = new AsientoContable();
        asiento.setNumeroAsiento(generarNumeroAsiento());
        asiento.setFecha(LocalDateTime.now());
        asiento.setDescripcion("Venta al contado - " + request.getCliente());
        asiento.setTipoOperacion("VENTA_CONTADO");

        // Crear movimientos
        List<MovimientoContable> movimientos = new ArrayList<>();

        // 1. Débito a Caja
        movimientos.add(crearMovimiento(asiento, cuentaCaja, montoTotal, BigDecimal.ZERO, "Cobro venta contado"));

        // 2. Crédito a Ventas
        movimientos.add(crearMovimiento(asiento, cuentaVentas, BigDecimal.ZERO, request.getMontoBase(), "Venta de mercaderías"));

        // 3. Crédito a IGV
        movimientos.add(crearMovimiento(asiento, cuentaIgv, BigDecimal.ZERO, montoIgv, "IGV venta"));

        // Validar que el asiento esté cuadrado
        validarAsientoCuadrado(movimientos);

        asiento.setMovimientos(movimientos);
        return asientoRepository.save(asiento);
    }

    private Cuenta obtenerCuentaOError(String codigo, String mensajeError) {
        return cuentaRepository.findById(codigo)
                .orElseThrow(() -> new RuntimeException(mensajeError));
    }

    private MovimientoContable crearMovimiento(AsientoContable asiento, Cuenta cuenta,
                                               BigDecimal debe, BigDecimal haber, String descripcion) {
        MovimientoContable movimiento = new MovimientoContable();
        movimiento.setAsiento(asiento);
        movimiento.setCuenta(cuenta);
        movimiento.setDebe(debe);
        movimiento.setHaber(haber);
        movimiento.setDescripcion(descripcion);
        return movimiento;
    }

    private void validarAsientoCuadrado(List<MovimientoContable> movimientos) {
        BigDecimal totalDebe = movimientos.stream()
                .map(MovimientoContable::getDebe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHaber = movimientos.stream()
                .map(MovimientoContable::getHaber)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebe.compareTo(totalHaber) != 0) {
            throw new RuntimeException("Asiento no cuadrado. Débito: " + totalDebe + ", Crédito: " + totalHaber);
        }
    }

    private String generarNumeroAsiento() {
        return "AS-" + System.currentTimeMillis();
    }

    public List<MovimientoContable> obtenerLibroMayor(String codigoCuenta) {
        return movimientoRepository.findByCuentaCodigo(codigoCuenta);
    }

    public BigDecimal obtenerSaldoCuenta(String codigoCuenta) {
        return movimientoRepository.calcularSaldoCuenta(codigoCuenta);
    }

    public List<AsientoContable> obtenerTodosAsientos() {
        return asientoRepository.findAllOrderByFechaDesc();
    }

}
