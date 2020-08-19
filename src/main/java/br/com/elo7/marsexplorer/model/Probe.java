package br.com.elo7.marsexplorer.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "probes")
public class Probe extends BaseEntity implements Serializable {
    @NotNull
    private Direction direction;
}
