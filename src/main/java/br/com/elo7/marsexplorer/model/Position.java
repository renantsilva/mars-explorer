package br.com.elo7.marsexplorer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Embeddable
@Data
@EqualsAndHashCode(of = {"x", "y"})
public class Position {
    @NotNull
    @Min(1)
    private Integer x;

    @NotNull
    @Min(1)
    private Integer y;

    public Position() {}
    public Position(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
}
