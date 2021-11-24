package io.github.mhagnumdw.search;

import org.apache.commons.lang3.StringUtils;

/**
 * Armazena o atributo pelo qual deve ser ordenado e o tipo da ordenação (ascendente ou descendente).
 */
public final class OrderBy {

    private String sort;

    private SortOrder order;

    private OrderBy(String sort, SortOrder order) {
        super();
        this.sort = sort;
        this.order = order;
    }

    public static OrderBy build(String sort, SortOrder order) {
        return new OrderBy(sort, order);
    }

    public boolean hasSort() {
        return StringUtils.isNotBlank(sort);
    }

    public String getSort() {
        return sort;
    }

    public SortOrder getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return String.format("OrderBy [sort=%s, order=%s]", sort, order);
    }

}
