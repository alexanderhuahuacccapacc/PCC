package com.pcge.pcgebackend.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class VentaCreditoRequest {
    private String cliente;
    private BigDecimal montoTotal;
    private String descripcion;
    private String tipoComprobante;
    private String numeroSerie;
    private String numeroDocumento;
    private String tipoDocumentoIdentidad;
    private String numeroDocumentoIdentidad;
    private String fechaEmision;
    private String fechaVencimiento;

    // Constructores
    public VentaCreditoRequest() {}

    public VentaCreditoRequest(String cliente, BigDecimal montoTotal, String descripcion,
                               String tipoComprobante, String numeroSerie, String numeroDocumento,
                               String tipoDocumentoIdentidad, String numeroDocumentoIdentidad,
                               String fechaEmision, String fechaVencimiento) {
        this.cliente = cliente;
        this.montoTotal = montoTotal;
        this.descripcion = descripcion;
        this.tipoComprobante = tipoComprobante;
        this.numeroSerie = numeroSerie;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumentoIdentidad = tipoDocumentoIdentidad;
        this.numeroDocumentoIdentidad = numeroDocumentoIdentidad;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
    }
}
