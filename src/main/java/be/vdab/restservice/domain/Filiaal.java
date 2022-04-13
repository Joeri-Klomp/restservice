package be.vdab.restservice.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Entity
@Table(name = "filialen")
public class Filiaal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String naam;
    @NotBlank
    private String gemeente;
    @NotNull @PositiveOrZero
    private BigDecimal omzet;

    public Filiaal(long id, String naam, String gemeente, BigDecimal omzet) {
        this.id = id;
        this.naam = naam;
        this.gemeente = gemeente;
        this.omzet = omzet;
    }

    protected Filiaal() {}

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getGemeente() {
        return gemeente;
    }

    public BigDecimal getOmzet() {
        return omzet;
    }

    public Filiaal withId(long id) {
        var filiaalMetId = new Filiaal(id, naam, gemeente, omzet);
        return filiaalMetId;
    }
}
