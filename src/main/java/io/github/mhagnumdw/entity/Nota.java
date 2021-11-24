package io.github.mhagnumdw.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Nota extends EntityIDLongAbstract {

    @NotBlank
    @Column(name = "NAME", length = 200, nullable = false)
    private String name;

    @NotBlank
    @Lob
    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "DISABLED", nullable = false)
    private boolean disabled;

    public Nota() {
        super();
    }

    public Nota(String name, String value, boolean disabled) {
        super();
        this.name = name;
        this.value = value;
        this.disabled = disabled;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isDisabled() {
        return disabled;
    }

}
