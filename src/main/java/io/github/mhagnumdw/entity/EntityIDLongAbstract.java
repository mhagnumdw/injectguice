package io.github.mhagnumdw.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntityIDLongAbstract extends EntityGenericAbstract<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Obs: IDENTITY generators disables JDBC batching for INSERT statements
    @Column(name = "ID", unique = true, updatable = false, nullable = false)
    private Long id;

    public EntityIDLongAbstract() {

    }

    public EntityIDLongAbstract(Long id) {
        super();
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return getId() == null || getId() == 0;
    }

}
