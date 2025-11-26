package com.pcge.pcgebackend.model;
import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "comprobantes")
public class Comprobante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_operacion", unique = true)
    private Long numeroOperacion;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "tipo_comprobante")
    private String tipoComprobante;

    @Column(name = "numero_serie")
    private String numeroSerie;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "tipo_documento_identidad")
    private String tipoDocumentoIdentidad;

    @Column(name = "numero_documento_identidad")
    private String numeroDocumentoIdentidad;

    @Column(name = "cliente")
    private String cliente;

    @Column(name = "tipo_venta")
    private String tipoVenta;

    @Column(name = "monto_total")
    private Double montoTotal;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;
}
