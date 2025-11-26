package com.pcge.pcgebackend.service;
import com.pcge.pcgebackend.dto.VentaContadoRequest;
import com.pcge.pcgebackend.dto.VentaCreditoRequest;
import com.pcge.pcgebackend.model.AsientoContable;
import com.pcge.pcgebackend.model.Comprobante;
import com.pcge.pcgebackend.model.Cuenta;
import com.pcge.pcgebackend.model.MovimientoContable;
import com.pcge.pcgebackend.repository.AsientoContableRepository;
import com.pcge.pcgebackend.repository.ComprobanteRepository;
import com.pcge.pcgebackend.repository.CuentaRepository;
import com.pcge.pcgebackend.repository.MovimientoContableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContabilidadService {
    private final AsientoContableRepository asientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoContableRepository movimientoRepository;
    private final ComprobanteRepository comprobanteRepository;

    // Códigos de cuentas según PCGE
    private static final String CUENTA_CAJA = "121";
    private static final String CUENTA_VENTAS = "701";
    private static final String CUENTA_IGV = "40111";
    private static final String CUENTA_EFECTIVO = "101"; // ✅ NUEVA CUENTA
    private static final BigDecimal IGV_PORCENTAJE = new BigDecimal("0.18");

    public ContabilidadService(AsientoContableRepository asientoRepository,
                               CuentaRepository cuentaRepository,
                               MovimientoContableRepository movimientoRepository, ComprobanteRepository comprobanteRepository) {
        this.asientoRepository = asientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.comprobanteRepository = comprobanteRepository;
    }
    private Long generarNumeroOperacion() {
        Long ultimoNumero = comprobanteRepository.findMaxNumeroOperacion();
        return (ultimoNumero == null ? 0 : ultimoNumero) + 1;
    }


    @Transactional
    public AsientoContable registrarVentaContado(VentaContadoRequest request) {
        Comprobante comprobante = new Comprobante();
        comprobante.setNumeroOperacion(generarNumeroOperacion());
        comprobante.setFechaEmision(LocalDate.parse(request.getFechaEmision()));
        comprobante.setFechaVencimiento(request.getFechaVencimiento() != null ?
                LocalDate.parse(request.getFechaVencimiento()) : null);
        comprobante.setTipoComprobante(request.getTipoComprobante());
        comprobante.setNumeroSerie(request.getNumeroSerie());
        comprobante.setNumeroDocumento(request.getNumeroDocumento());
        comprobante.setTipoDocumentoIdentidad(request.getTipoDocumentoIdentidad());
        comprobante.setNumeroDocumentoIdentidad(request.getNumeroDocumentoIdentidad());
        comprobante.setCliente(request.getCliente());
        comprobante.setTipoVenta("CONTADO");
        comprobante.setMontoTotal(request.getMontoTotal().doubleValue());
        comprobante.setDescripcion(request.getDescripcion());
        comprobante.setFechaRegistro(LocalDate.now());

        comprobanteRepository.save(comprobante);
        // Validar que las cuentas existan
        Cuenta cuentaCaja = obtenerCuentaOError(CUENTA_CAJA, "Cuenta Caja no configurada");
        Cuenta cuentaVentas = obtenerCuentaOError(CUENTA_VENTAS, "Cuenta Ventas no configurada");
        Cuenta cuentaIgv = obtenerCuentaOError(CUENTA_IGV, "Cuenta IGV no configurada");
        Cuenta cuentaEfectivo = obtenerCuentaOError(CUENTA_EFECTIVO, "Cuenta Efectivo no configurada"); // ✅ NUEVA

        // Calcular montos
        BigDecimal montoTotal = request.getMontoTotal();
        BigDecimal montoBase = montoTotal.divide(new BigDecimal("1.18"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal montoIgv = montoTotal.subtract(montoBase);

        // ✅ PRIMER ASIENTO: REGISTRO DE VENTA
        AsientoContable asientoVenta = new AsientoContable();
        asientoVenta.setNumeroAsiento(generarNumeroAsiento() + "-V");
        asientoVenta.setFecha(LocalDateTime.now());
        asientoVenta.setDescripcion("Venta al contado - " + request.getCliente() +  " - Comp: " + request.getTipoComprobante() +
                "-" + request.getNumeroSerie() +
                "-" + request.getNumeroDocumento());
        asientoVenta.setTipoOperacion("VENTA_CONTADO");
        asientoVenta.setMovimientos(new ArrayList<>());

        // Movimientos del asiento de venta
        MovimientoContable movCajaVenta = crearMovimiento(asientoVenta, cuentaCaja, montoTotal, BigDecimal.ZERO, "Cobro venta contado");
        MovimientoContable movVentas = crearMovimiento(asientoVenta, cuentaVentas, BigDecimal.ZERO, montoBase, "Venta de mercaderías");
        MovimientoContable movIgv = crearMovimiento(asientoVenta, cuentaIgv, BigDecimal.ZERO, montoIgv, "IGV venta");

        asientoVenta.getMovimientos().add(movCajaVenta);
        asientoVenta.getMovimientos().add(movVentas);
        asientoVenta.getMovimientos().add(movIgv);

        validarAsientoCuadrado(asientoVenta.getMovimientos());
        AsientoContable asientoVentaGuardado = asientoRepository.save(asientoVenta);

        // ✅ SEGUNDO ASIENTO: INGRESO A CAJA (EFECTIVO)
        AsientoContable asientoCaja = new AsientoContable();
        asientoCaja.setNumeroAsiento(generarNumeroAsiento() + "-C");
        asientoCaja.setFecha(LocalDateTime.now());
        asientoCaja.setDescripcion("Ingreso a caja por venta - " + request.getCliente());
        asientoCaja.setTipoOperacion("INGRESO_CAJA");
        asientoCaja.setMovimientos(new ArrayList<>());

        // Movimientos del asiento de caja
        MovimientoContable movEfectivo = crearMovimiento(asientoCaja, cuentaEfectivo, montoTotal, BigDecimal.ZERO, "Ingreso de efectivo");
        MovimientoContable movCajaCierre = crearMovimiento(asientoCaja, cuentaCaja, BigDecimal.ZERO, montoTotal, "Cierre de caja venta");

        asientoCaja.getMovimientos().add(movEfectivo);
        asientoCaja.getMovimientos().add(movCajaCierre);

        validarAsientoCuadrado(asientoCaja.getMovimientos());
        AsientoContable asientoCajaGuardado = asientoRepository.save(asientoCaja);

        // Retornar el asiento de venta (puedes cambiar esto si prefieres retornar ambos)
        return asientoVentaGuardado;
    }
    @Transactional
    public AsientoContable registrarVentaCredito(VentaCreditoRequest request) {
        // Cuentas para venta a crédito
        Comprobante comprobante = new Comprobante();
        comprobante.setNumeroOperacion(generarNumeroOperacion());
        comprobante.setFechaEmision(LocalDate.parse(request.getFechaEmision()));
        comprobante.setFechaVencimiento(request.getFechaVencimiento() != null ?
                LocalDate.parse(request.getFechaVencimiento()) : null);
        comprobante.setTipoComprobante(request.getTipoComprobante());
        comprobante.setNumeroSerie(request.getNumeroSerie());
        comprobante.setNumeroDocumento(request.getNumeroDocumento());
        comprobante.setTipoDocumentoIdentidad(request.getTipoDocumentoIdentidad());
        comprobante.setNumeroDocumentoIdentidad(request.getNumeroDocumentoIdentidad());
        comprobante.setCliente(request.getCliente());
        comprobante.setTipoVenta("CREDITO");
        comprobante.setMontoTotal(request.getMontoTotal().doubleValue());
        comprobante.setDescripcion(request.getDescripcion());
        comprobante.setFechaRegistro(LocalDate.now());

        Cuenta cuentaClientes = obtenerCuentaOError("121", "Cuenta Clientes no configurada");
        Cuenta cuentaVentas = obtenerCuentaOError(CUENTA_VENTAS, "Cuenta Ventas no configurada");
        Cuenta cuentaIgv = obtenerCuentaOError(CUENTA_IGV, "Cuenta IGV no configurada");

        // Calcular montos
        BigDecimal montoTotal = request.getMontoTotal();
        BigDecimal montoBase = montoTotal.divide(new BigDecimal("1.18"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal montoIgv = montoTotal.subtract(montoBase);

        // Asiento de venta a crédito
        AsientoContable asientoVenta = new AsientoContable();
        asientoVenta.setNumeroAsiento(generarNumeroAsiento() + "-VC");
        asientoVenta.setFecha(LocalDateTime.now());
        asientoVenta.setDescripcion("Venta a crédito - " + request.getCliente() +
                " - Comp: " + request.getTipoComprobante() +
                "-" + request.getNumeroSerie() +
                "-" + request.getNumeroDocumento());
        asientoVenta.setTipoOperacion("VENTA_CREDITO");
        asientoVenta.setMovimientos(new ArrayList<>());

        // Movimientos del asiento de venta a crédito
        MovimientoContable movClientes = crearMovimiento(asientoVenta, cuentaClientes, montoTotal, BigDecimal.ZERO, "Cuenta por cobrar cliente");
        MovimientoContable movVentas = crearMovimiento(asientoVenta, cuentaVentas, BigDecimal.ZERO, montoBase, "Venta de mercaderías a crédito");
        MovimientoContable movIgv = crearMovimiento(asientoVenta, cuentaIgv, BigDecimal.ZERO, montoIgv, "IGV venta a crédito");

        asientoVenta.getMovimientos().add(movClientes);
        asientoVenta.getMovimientos().add(movVentas);
        asientoVenta.getMovimientos().add(movIgv);

        validarAsientoCuadrado(asientoVenta.getMovimientos());
        return asientoRepository.save(asientoVenta);
    }
    public List<Comprobante> obtenerTodosComprobantes() {
        return comprobanteRepository.findAllByOrderByNumeroOperacionDesc();
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
        List<MovimientoContable> movimientos = movimientoRepository.findByCuentaCodigo(codigoCuenta);

        BigDecimal saldo = BigDecimal.ZERO;
        for (MovimientoContable mov : movimientos) {
            saldo = saldo.add(mov.getDebe().subtract(mov.getHaber()));
        }

        return saldo;
    }

    public List<AsientoContable> obtenerTodosAsientos() {
        return asientoRepository.findAllOrderByFechaDesc();
    }
}