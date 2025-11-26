package com.pcge.pcgebackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cuenta")
@Data
public class Cuenta {

    @Id
    private String codigo;
    private String nombre;
    private Integer nivel;
    private String tipo;
    private String padreId;
}
