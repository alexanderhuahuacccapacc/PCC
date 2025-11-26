package com.pcge.pcgebackend.dto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AsientoContableResponse {
    private Long id;
    private String numeroAsiento;
    private LocalDateTime fecha;
    private String descripcion;
    private List<MovimientoResponse> movimientos;
}

@Data
class MovimientoResponse {
    private String cuentaCodigo;
    private String cuentaNombre;
    private BigDecimal debe;
    private BigDecimal haber;
    private String descripcion;

}
