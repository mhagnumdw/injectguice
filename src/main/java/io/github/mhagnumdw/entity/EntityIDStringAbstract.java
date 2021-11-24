package io.github.mhagnumdw.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public abstract class EntityIDStringAbstract extends EntityGenericAbstract<String> {

    @Id
    @Column(name = "ID", unique = true, updatable = false, nullable = false)
    private String id;

    public EntityIDStringAbstract() {

    }

    public EntityIDStringAbstract(String id) {
        super();
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return StringUtils.isBlank(getId());
    }

}
