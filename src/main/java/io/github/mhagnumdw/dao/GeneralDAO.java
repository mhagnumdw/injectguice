package io.github.mhagnumdw.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import com.google.inject.Inject;
import com.google.inject.Provider;

import io.github.mhagnumdw.PaginationScrollStatus;
import io.github.mhagnumdw.search.SortOrder;
import io.github.mhagnumdw.util.HibernateUtil;
import io.github.mhagnumdw.util.Pair;
import io.github.mhagnumdw.util.MyStringUtils;

/**
 * DAO de uso geral.
 *
 * <p>
 * Observações:
 * <ul>
 * <li>Da versão 5.1 para a 5.2 do Hibernate query.setResultTransformer ficou depreciado e até o momento - 4/9/2016 - não tem solução:
 * https://vladmihalcea.com/2017/08/29/the-best-way-to-map-a-projection-query-to-a-dto-with-jpa-and-hibernate/#comment-11795</li>
 * </ul>
 * </p>
 */
public class GeneralDAO implements DAO {

    @Inject
    private Provider<EntityManager> emProvider;

    @Override
    public EntityManager getEntityManager() {
        return emProvider.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBySimpleNaturalId(Class<T> resultClass, Object naturalIdValue) {
        return getSession().bySimpleNaturalId(resultClass).load(naturalIdValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getReferenceBySimpleNaturalId(Class<T> resultClass, Object naturalIdValue) {
        return getSession().bySimpleNaturalId(resultClass).getReference(naturalIdValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getById(Serializable id, Class<T> resultClass) {
        T retVal = getEntityManager().find(resultClass, id);
        return retVal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByIds(Collection<?> ids, Class<T> resultClass) {
        final String qlString = "FROM " + resultClass.getName() + " e WHERE e.id in :ids";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass) {
        final String qlString = "FROM " + resultClass.getName() + " e WHERE e." + attributeToSearch.getName() + " = :value";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("value", value);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> List<E> getByDirectAttributeIn(SingularAttribute<?, T> attributeToSearch, Collection<T> values, Class<E> resultClass) {
        final String qlString = "FROM " + resultClass.getName() + " e WHERE e." + attributeToSearch.getName() + " in :values";
        TypedQuery<E> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("values", values);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass) {
        TypedQuery<T> query = createHibernateQuery(resultClass, params);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder) {
        Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes = Collections.singletonMap(orderByAttribute, sortOrder);
        org.hibernate.query.Query<T> query = createHibernateQuery(resultClass, orderByAttributes, true, params);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByDirectAttributeStartWith(SingularAttribute<?, String> attributeToSearch, String value, Class<T> resultClass) {
        final String qlString = "FROM " + resultClass.getName() + " e WHERE e." + attributeToSearch.getName() + " like :value";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("value", value + "%");
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getAll(Class<T> resultClass) {
        final String qlString = "FROM " + resultClass.getName();
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getAll(Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder) {
        return getAll(resultClass, orderByAttribute, sortOrder, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getAll(Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder, boolean lowerInOrderBy) {
        final String qlString;
        if (lowerInOrderBy) {
            qlString = "FROM " + resultClass.getName() + " e ORDER BY lower(e." + orderByAttribute.getName() + ") " + sortOrder.getNome();
        } else {
            qlString = "FROM " + resultClass.getName() + " e ORDER BY e." + orderByAttribute.getName() + " " + sortOrder.getNome();
        }
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResult(String namedQuery, Class<T> resultClass) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResult(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParameters(query, params);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResult(String namedQuery, Class<T> resultClass, Map<String, Object> params) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParametersString(query, params);
        return getSingleResult(query);
    }

    private <T> T getSingleResult(TypedQuery<T> query) {
        try {
            T retVal = query.getSingleResult();
            return retVal;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResultLimitTo1(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        query.setMaxResults(1);
        HibernateUtil.setParameters(query, params);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResultLimitTo1(String namedQuery, Class<T> resultClass, Map<String, Object> params) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        query.setMaxResults(1);
        HibernateUtil.setParametersString(query, params);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResultByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass) {
        final String qlString = "FROM " + resultClass.getName() + " e WHERE e." + attributeToSearch.getName() + " = :value";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("value", value);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getSingleResultByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass, boolean lowerInSearch) {
        final String where;
        if (lowerInSearch) {
            where = "WHERE lower(e." + attributeToSearch.getName() + ") = lower(:value) ";
        } else {
            where = "WHERE e." + attributeToSearch.getName() + " = :value ";
        }
        final String qlString = "FROM " + resultClass.getName() + " e " + where;
        TypedQuery<T> query = getEntityManager().createQuery(qlString, resultClass);
        query.setParameter("value", value);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getSingleResultByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass) {
        TypedQuery<T> query = createHibernateQuery(resultClass, params);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> boolean exist(Class<E> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value) {
        return exist(entityClass, attributeToSearch, value, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> boolean exist(Class<E> entityClass, Map<SingularAttribute<?, ?>, Object> attributesToSearch) {
        final String where = buildWhere("e", attributesToSearch);

        // @formatter:off
        final String qlString =
                "SELECT CASE WHEN (COUNT(*) > 0) THEN true ELSE false END "
                + "FROM " + entityClass.getName() + " e "
                + where;
        // @formatter:on

        final TypedQuery<Boolean> query = getEntityManager().createQuery(qlString, Boolean.class);
        associarParametros(query, attributesToSearch);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> boolean exist(Class<E> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value, boolean lowerOrTruncInSearch) {
        String where = "WHERE e." + attributeToSearch.getName() + " = :value ";
        if (lowerOrTruncInSearch) {
            if (attributeToSearch.getBindableJavaType().isAssignableFrom(CharSequence.class)) {
                where = "WHERE lower(e." + attributeToSearch.getName() + ") = lower(:value) ";
            } else if (attributeToSearch.getBindableJavaType().isAssignableFrom(Date.class)) {
                where = "WHERE trunc(e." + attributeToSearch.getName() + ") = trunc(:value) ";
            }
        }

        // @formatter:off
        final String qlString =
                "SELECT CASE WHEN (COUNT(*) > 0) THEN true ELSE false END "
                + "FROM " + entityClass.getName() + " e "
                + where;
        // @formatter:on

        TypedQuery<Boolean> query = getEntityManager().createQuery(qlString, Boolean.class);
        query.setParameter("value", value);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn) {
        return getAttributes(entityClass, attributeToReturn, attributeToReturn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn, SingularAttribute<?, ?> orderByAttribute) {
        return getAttributes(entityClass, attributeToReturn, orderByAttribute, SortOrder.ASC, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder, boolean lowerInOrderBy) {
        String qlString = "SELECT e." + attributeToReturn.getName() + " FROM " + entityClass.getName() + " e ";
        if (lowerInOrderBy) {
            qlString += " ORDER BY lower(e." + orderByAttribute.getName() + ") " + sortOrder.getNome();
        } else {
            qlString += " ORDER BY e." + orderByAttribute.getName() + " " + sortOrder.getNome();
        }
        TypedQuery<T> query = getEntityManager().createQuery(qlString, attributeToReturn.getBindableJavaType());
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> List<T> getAttributes(Class<E> entityClass, Attribute<?, T> attributeToReturn, SingularAttribute<?, ?> attributeToSearch, Object valueToSearch) {
        final String qlString = "SELECT e." + attributeToReturn.getName() + " FROM " + entityClass.getName() + " e WHERE e." + attributeToSearch.getName() + " = :valueToSearch";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, attributeToReturn.getJavaType());
        query.setParameter("valueToSearch", valueToSearch);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, T> T getAttribute(Class<E> entityClass, Attribute<?, T> attributeToReturn, SingularAttribute<?, ?> attributeToSearch, Object valueToSearch) {
        final String qlString = "SELECT e." + attributeToReturn.getName() + " FROM " + entityClass.getName() + " e WHERE e." + attributeToSearch.getName() + " = :valueToSearch";
        TypedQuery<T> query = getEntityManager().createQuery(qlString, attributeToReturn.getJavaType());
        query.setParameter("valueToSearch", valueToSearch);
        return getSingleResult(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E, K, T> Map<K, T> getAttributesMap(Class<E> entityClass, SingularAttribute<?, K> keyToReturn, SingularAttribute<?, T> valueToReturn) {
        final String qlString = "SELECT e." + keyToReturn.getName() + ", e." + valueToReturn.getName() + " FROM " + entityClass.getName() + " e";
        final List<Object[]> raw = getEntityManager().createQuery(qlString, Object[].class).getResultList();
        return (Map<K, T>) raw.stream().collect(Collectors.toMap(row -> row[0], row -> row[1]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> get(String namedQuery, Class<T> resultClass) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> get(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParameters(query, params);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> get(String namedQuery, Class<T> resultClass, Map<String, Object> params) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParametersString(query, params);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> get(String namedQuery, Class<T> resultClass, Map<String, Object> params, int maxResult) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParametersString(query, params);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void processAll(Class<T> targetClass, Consumer<T> process) {
        final String qlString = "FROM " + targetClass.getName();
        org.hibernate.query.Query<T> query = getSession().createQuery(qlString);
        process(query, process);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void processAllSave(Class<T> targetClass, Consumer<T> process) {
        final String qlString = "FROM " + targetClass.getName();
        org.hibernate.query.Query<T> query = getSession().createQuery(qlString);
        persist(query, process);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void process(String namedQuery, Map<String, Object> params, Consumer<T> process) {
        org.hibernate.query.Query<T> query = getSession().getNamedQuery(namedQuery);
        HibernateUtil.setParametersString(query, params);
        process(query, process);
    }

    private <T> void process(org.hibernate.query.Query<T> query, Consumer<T> process) {
        try (ScrollableResults scrollableResults = query.scroll(ScrollMode.FORWARD_ONLY)) {
            while (scrollableResults.next()) {
                T tt = (T) scrollableResults.get()[0];
                process.accept(tt);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void processSave(String namedQuery, Map<String, Object> params, Consumer<T> process) {
        org.hibernate.query.Query<T> query = getSession().getNamedQuery(namedQuery);
        HibernateUtil.setParametersString(query, params);
        persist(query, process);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> List<T> getByNativeQuery(String nativeQuery, String resultSetMapping) {
        Query query = getEntityManager().createNativeQuery(nativeQuery, resultSetMapping);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, int page, int pageSize) {
        return getPaged(resultClass, null, page, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes, int page, int pageSize) {
        return getPaged(resultClass, orderByAttributes, false, page, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes, boolean nullsFirst, int page, int pageSize) {
        return getPaged(resultClass, null, orderByAttributes, nullsFirst, page, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, Object> params, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes,
            boolean nullsFirst, int page, int pageSize) {
        org.hibernate.query.Query<T> query = createHibernateQuery(resultClass, orderByAttributes, nullsFirst, params);
        return returnPagedList(page, pageSize, query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, String namedQuery, Map<String, Object> params, int page, int pageSize) {
        org.hibernate.query.Query<T> query = getSession().createNamedQuery(namedQuery, resultClass);
        HibernateUtil.setParametersString(query, params);
        return returnPagedList(page, pageSize, query);
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public <T, S extends SearchAbstract<T>> Pair<List<T>, PaginationScrollStatus> getPaged(S search, int page, int pageSize) {
//        PaginationScrollStatus pss = new PaginationScrollStatus(page, pageSize, getTotalDeRegistros(search));
//        TypedQuery<T> query = HibernateUtil.createQuery(search, getEntityManager());
//        query.setFirstResult(pss.getOffset());
//        query.setMaxResults(pss.getPageSize());
//        List<T> resultList = query.getResultList();
//        return new Pair<List<T>, PaginationScrollStatus>(resultList, pss);
//    }

    private <T> Pair<List<T>, PaginationScrollStatus> returnPagedList(int page, int pageSize, org.hibernate.query.Query<T> query) {
        PaginationScrollStatus pss = new PaginationScrollStatus(page, pageSize, getTotalDeRegistros(query));
        query.setFirstResult(pss.getOffset());
        query.setMaxResults(pss.getPageSize());
        List<T> resultList = query.getResultList();
        return new Pair<List<T>, PaginationScrollStatus>(resultList, pss);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int deleteAll(Class<T> entityClass) {
        final String qlString = "DELETE FROM " + entityClass.getName();
        Query query = getEntityManager().createQuery(qlString);
        return executeUpdate(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int deleteByDirectAttribute(Class<T> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value) {
        final String qlString = "DELETE FROM " + entityClass.getName() + " e WHERE e." + attributeToSearch.getName() + " = :value";
        Query query = getEntityManager().createQuery(qlString);
        query.setParameter("value", value);
        return executeUpdate(query);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void deleteById(Serializable id, Class<T> entityClass) {
        T entity = getEntityManager().find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException("Unable to find " + entityClass.getName() + " with id " + id);
        }
        // Case of attached entity - simply remove it
        if (getEntityManager().contains(entity)) {
            getEntityManager().remove(entity);
        } else {
            // Case of unattached entity, first it is necessary to perform
            // a merge, before doing the remove
            entity = getEntityManager().merge(entity);
            getEntityManager().remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int executeUpdate(String namedQuery, Map<String, Object> params) {
        Query query = getEntityManager().createNamedQuery(namedQuery);
        HibernateUtil.setParametersString(query, params);
        return executeUpdate(query);
    }

    /**
     * Executa um {@code query.executeUpdate()}.
     *
     * @param query
     *            instância de {@link Query}
     *
     * @return a quantidade de entidades atualizadas ou deletadas
     */
    private int executeUpdate(Query query) {
        return query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T persist(T entity) {
        // Case of new, non-persisted entity
        if (extractId(entity) == null) {
            getEntityManager().persist(entity);
        } else if (!getEntityManager().contains(entity)) {
            // In the case of an attached entity, we do nothing (it
            // will be persisted automatically on synchronisation)
            // But... in the case of an unattached, but persisted entity
            // we perform a merge to re-attach and persist it
            entity = getEntityManager().merge(entity);
        }
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void persist(Collection<T> entities) {
        for (T entity : entities) {
            entity = persist(entity);
        }
    }

    private <T> void persist(org.hibernate.query.Query<T> query, Consumer<T> process) {
        try (ScrollableResults scrollableResults = query.scroll(ScrollMode.FORWARD_ONLY)) {
            while (scrollableResults.next()) {
                T entity = (T) scrollableResults.get()[0];
                process.accept(entity);
                entity = persist(entity);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T refresh(T entity) {
        // Attempting to refresh a non-persisted entity
        // will result in an exception
        if (extractId(entity) == null) {
            // causes exception
            getEntityManager().refresh(entity);
        } else if (getEntityManager().contains(entity)) {
            // Case of attached empty - this gets refreshed
            getEntityManager().refresh(entity);
        } else {
            // Case of unattached entity, first it is necessary to perform
            // a merge, before doing the refresh
            entity = getEntityManager().merge(entity);
            getEntityManager().refresh(entity);
        }

        return entity;
    }

    private Object extractId(Object entity) {
        return getEntityManager().getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
    }

    private Session getSession() {
        return HibernateUtil.getSession(getEntityManager());
    }

    private int getTotalDeRegistros(org.hibernate.query.Query query) {
        ScrollableResults scroll = query.scroll(ScrollMode.FORWARD_ONLY);
        scroll.last();
        return scroll.getRowNumber() + 1;
    }

//    private <T, S extends SearchAbstract<T>> int getTotalDeRegistros(S search) {
//        TypedQuery<Long> query = HibernateUtil.createCountQuery(search, getEntityManager());
//        Long qtdLongValue = query.getSingleResult();
//        return qtdLongValue.intValue();
//    }

    private <T> org.hibernate.query.Query<T> createHibernateQuery(Class<T> resultClass, Map<SingularAttribute<?, ?>, Object> condicoes) {
        return createHibernateQuery(resultClass, null, false, condicoes);
    }

    private <T> org.hibernate.query.Query<T> createHibernateQuery(Class<T> resultClass, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes, boolean nullsFirst,
            Map<SingularAttribute<?, ?>, Object> condicoes) {
        final StringBuilder qlString = new StringBuilder("FROM " + resultClass.getName() + " e ");
        if (condicoes != null) {
            qlString.append(buildWhere("e", condicoes));
        }
        if (orderByAttributes != null && !orderByAttributes.isEmpty()) {
            final String nf = nullsFirst ? "NULLS FIRST" : "";
            qlString.append(" ORDER BY ");

            List<String> attributes = new ArrayList<>();
            orderByAttributes.forEach((attribute, typeOrder) -> {
                attributes.add("e." + attribute.getName() + " " + typeOrder.getNome() + " " + nf);
            });

            qlString.append(attributes.stream().map(param -> param.toString()).collect(Collectors.joining(", ")));

        }
        org.hibernate.query.Query<T> query = getSession().createQuery(qlString.toString(), resultClass);
        if (condicoes != null) {
            associarParametros(query, condicoes);
        }
        return query;
    }

    private String buildWhere(String alias, Map<SingularAttribute<?, ?>, Object> condicoes) {
        final String AND = " AND ";
        final StringBuilder qlString = new StringBuilder();
        if (condicoes != null) {
            if (condicoes.size() > 0) {
                qlString.append(" WHERE ");
            }
            condicoes.forEach((key, value) -> {
                final String attrName = key.getName();
                final String paramName = attrName + key.hashCode();
                if (value == null) {
                    qlString.append(" e." + attrName + " is null" + AND);
                } else {
                    qlString.append(" e." + attrName + " = :" + paramName + AND);
                }
            });
            MyStringUtils.replaceLast(qlString, AND, "");
        }
        return qlString.toString();
    }

    private void associarParametros(javax.persistence.Query query, Map<SingularAttribute<?, ?>, Object> condicoes) {
        condicoes.forEach((key, value) -> {
            final String attrName = key.getName();
            final String paramName = attrName + key.hashCode();
            if (value != null) {
                query.setParameter(paramName, value);
            }
        });
    }

}
