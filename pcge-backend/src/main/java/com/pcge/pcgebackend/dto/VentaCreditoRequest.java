package com.pcge.pcgebackend.dto;

import java.math.BigDecimal;

public class VentaCreditoRequest {
    private String cliente;
    private BigDecimal montoTotal;
    private String descripcion;

    // Constructores
    public VentaCreditoRequest() {}

    public VentaCreditoRequest(String cliente, BigDecimal montoTotal, String descripcion) {
        this.cliente = cliente;
        this.montoTotal = montoTotal;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
