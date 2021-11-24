package io.github.mhagnumdw.util;

import java.io.Serializable;

public class Pair<U, V> implements Serializable {

    /**
     * The first element of this {@code Pair}.
     */
    private U first;

    /**
     * The second element of this {@code Pair}.
     */
    private V second;

    /**
     * Constructs a new {@code Pair} with the given values.
     *
     * @param first
     *            the first element
     * @param second
     *            the second element
     */
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public static <U, V> Pair<U, V> build(U first, V second) {
        return new Pair<U, V>(first, second);
    }

    public U getFirst() {
        return first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((first == null) ? 0 : first.hashCode());
        result = prime * result + ((second == null) ? 0 : second.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (first == null) {
            if (other.first != null)
                return false;
        } else if (!first.equals(other.first))
            return false;
        if (second == null) {
            if (other.second != null)
                return false;
        } else if (!second.equals(other.second))
            return false;
        return true;
    }

}
