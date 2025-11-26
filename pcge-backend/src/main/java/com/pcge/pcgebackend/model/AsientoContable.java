package com.pcge.pcgebackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asiento_contable")
@Data
public class AsientoContable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroAsiento;
    private LocalDateTime fecha;
    private String descripcion;
    private String tipoOperacion; // VENTA_CONTADO, COMPRA, etc.

    @OneToMany(mappedBy = "asiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MovimientoContable> movimientos = new ArrayList<>();;

}
