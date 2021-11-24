package io.github.mhagnumdw.search;

import java.util.LinkedHashMap;

import io.github.mhagnumdw.util.HibernateUtil;

/**
 * <i>Value object</i> abstrato que deve ser herdado para realizar pesquisas sem a necessidade de manipulação da query literal ou <i>criteria</i>.
 *
 * @see SearchFilter
 * @see HibernateUtil#createQuery(SearchAbstract, javax.persistence.EntityManager)
 */
public abstract class SearchAbstract<T> {

    protected static final boolean ASC = true;
    protected static final boolean DESC = false;

    private LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();

    public abstract Class<T> getType();

    public LinkedHashMap<String, Boolean> getOrderBy() {
        return orderBy;
    }

    /**
     * Adiciona uma ordenação. O parâmetro {@code orderBy} pode ser nulo.
     * <p>
     * Se objeto {@link OrderBy} não definir o atributo de ordenação, não será adicionada ordenação; se não definir a ordem assumirá o valor padrão.
     * </p>
     *
     * @param orderBy
     *            ordenação
     */
    public void addOrderBy(OrderBy orderBy) {
        if (orderBy != null && orderBy.hasSort()) {
            addOrderBy(orderBy.getSort(), orderBy.getOrder());
        }
    }

    private void addOrderBy(String sortAttribute, SortOrder sortOrder) {
        final Boolean order = sortOrder == null ? null : sortOrder.toBoolean();
        this.orderBy.put(sortAttribute, order);
    }

}
