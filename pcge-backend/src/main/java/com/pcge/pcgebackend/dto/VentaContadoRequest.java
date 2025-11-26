package com.pcge.pcgebackend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VentaContadoRequest {
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


}
