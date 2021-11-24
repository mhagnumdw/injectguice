package io.github.mhagnumdw.search;

public enum SortOrder {

    ASC("asc"), DESC("desc");

    private String nome;

    private SortOrder(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public boolean toBoolean() {
        return SortOrder.ASC.equals(this);
    }

}
