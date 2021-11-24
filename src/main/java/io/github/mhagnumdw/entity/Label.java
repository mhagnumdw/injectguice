package io.github.mhagnumdw.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

@Entity
//@formatter:off
@Table(name = "LABEL", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"}, name = "unique_label_name")
})
//@formatter:on
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NaturalIdCache
public class Label extends EntityIDLongAbstract implements Comparable<Label> {

    private static final long serialVersionUID = 6240931529933224805L;

    @NaturalId(mutable = true) // default
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "DESCRIPTION", nullable = false, length = 100)
    private String description;

    public Label() {

    }

    public Label(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Label other) {
        return name.compareTo(other.name);
    }

}
