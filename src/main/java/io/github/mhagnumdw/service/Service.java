package io.github.mhagnumdw.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.annotations.NaturalId;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

import io.github.mhagnumdw.dao.DAO;
import io.github.mhagnumdw.helper.TypeHelper;
import io.github.mhagnumdw.search.SortOrder;

public class Service<T> {

    @Inject
    private DAO dao;

    @SuppressWarnings("unchecked")
    private Class<T> getEntityType() {
        return (Class<T>) TypeHelper.getTypeArguments(Service.class, this.getClass()).get(0);
    }

    public T getById(Serializable id) {
        return getDAO().getById(id, getEntityType());
    }

    public List<T> getAll() {
        return getDAO().getAll(getEntityType());
    }

    public List<T> getAll(SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder) {
        return getDAO().getAll(getEntityType(), orderByAttribute, sortOrder);
    }

    public List<T> getAll(SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder, boolean lowerInOrderBy) {
        return getDAO().getAll(getEntityType(), orderByAttribute, sortOrder, lowerInOrderBy);
    }

    /**
     * Persiste a entidade {@code entity} fazendo validações <i>Bean Validation</i>.
     *
     * @param entity
     *            entidade a ser persistida
     *
     * @see #save(List)
     */
    // TODO: esse método pode E DEVE simplesmente chamar o método #save(List<T> entities) passando um Collections.singletonList(entity)
    @Transactional
    public void save(T entity) {
        getDAO().persist(entity);
        getDAO().getEntityManager().flush();
    }

    /**
     * Persiste a coleção de entidades {@code entities} fazendo validações <i>Bean Validation</i>.
     *
     * @param entities
     *            coleção de entidades para persistir
     *
     * @see #save(Object)
     */
    @Transactional
    public void save(Collection<T> entities) {
        getDAO().persist(entities);
        getDAO().getEntityManager().flush();
    }

    /**
     * Deleta um registro/entidade.
     *
     * @param id
     *            ID
     */
    @Transactional
    public void deleteById(Serializable id) {
        getDAO().deleteById(id, getEntityType());
    }

    /**
     * Aplicável se a entidade definir um atributo com {@link NaturalId}.
     *
     * @param naturalIdValue
     *            o valor do id-natural (chave de negócio)
     *
     * @return instância da entidade ou nulo se a entidade não existir
     */
    public T getBySimpleNaturalId(Object naturalIdValue) {
        return getDAO().getBySimpleNaturalId(getEntityType(), naturalIdValue);
    }

    /**
     * Aplicável se a entidade definir um atributo com {@link NaturalId}.
     *
     * <p>
     * <b>Assume que a entidade existe.</b>
     * </p>
     *
     * @param naturalIdValue
     *            o valor do id-natural (chave de negócio)
     *
     * @return instância da entidade ou apenas uma referência que pode ou não existir
     */
    public T getReferenceBySimpleNaturalId(Object naturalIdValue) {
        return getDAO().getReferenceBySimpleNaturalId(getEntityType(), naturalIdValue);
    }

    public DAO getDAO() {
        return dao;
    }

    @VisibleForTesting
    void setDAO(DAO dao) {
        this.dao = dao;
    }

}
