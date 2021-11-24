package io.github.mhagnumdw.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Entidade mais abstrata.
 *
 * <pre>
 *
 * Já contém:
 *
 * version  : long
 * selected : boolean - transient
 *
 * equals e hashcode com base no id utilizando o get
 * </pre>
 *
 * @param <T>
 *            id type
 *
 * @author MhagnumDw
 */
@MappedSuperclass
public abstract class EntityGenericAbstract<T extends Serializable> extends RootObject {

    /**
     * <pre>
     * Não precisa de GET e SET
     * Version é Optimistic Lock
     * Só bloqueia na hora que modifica o registro.
     * Pode ser do tipo: int e Integer, short e Short, long e Long, {@link java.sql.Timestamp}
     *
     * <b>Funcionamento:</b>
     *
     * Em português:
     * Quando uma entidade é lida, o valor atual da coluna de versão é preenchido. Quando
     * ocorre uma atualização, o número da versão é comparado com o número da versão
     * atual na base de dados. Se os dois valores diferem, uma OptimisticLockException é
     * lançada. É da responsabilidade do programador para desenvolver a lógica de re-ler os
     * dados e voltar a tentar a atualização.
     *
     * Em inglês:
     * When an entity is read, the current value of the version column is populated. When
     * an update occurs, the version number is compared with the current version number in
     * the database. If the two values differ, an OptimisticLockException is thrown. It is the
     * responsibility of the programmer to develop logic to re-read the data and re-try the
     * update.
     * </pre>
     */
    @Version
    @Column(name = "VERSION", nullable = false)
    private long version;

    public EntityGenericAbstract() {
        super();
    }

    public EntityGenericAbstract(T id) {
        super();
        setId(id);
    }

    public abstract T getId();

    public abstract void setId(T id);

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EntityGenericAbstract))
            return false;
        EntityGenericAbstract other = (EntityGenericAbstract) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }

}
