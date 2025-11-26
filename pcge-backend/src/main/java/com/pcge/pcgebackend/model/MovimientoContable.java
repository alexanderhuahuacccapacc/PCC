package com.pcge.pcgebackend.model;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "movimiento_contable")
@Data
public class MovimientoContable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asiento_id")
    private AsientoContable asiento;

    @ManyToOne
    @JoinColumn(name = "cuenta_codigo")
    private Cuenta cuenta;

    private BigDecimal debe;
    private BigDecimal haber;
    private String descripcion;
}
