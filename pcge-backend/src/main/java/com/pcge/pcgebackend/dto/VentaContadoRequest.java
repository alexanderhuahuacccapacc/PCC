package com.pcge.pcgebackend.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VentaContadoRequest {
    private String cliente;
    private BigDecimal montoTotal;
    private String descripcion;
}
