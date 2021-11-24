package io.github.mhagnumdw.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

public final class CollectionUtils {

    private CollectionUtils() {
        // nothing
    }

    @SafeVarargs
    public static <T> boolean isFilled(final T... array) {
        return !isNullOrEmpty(array);
    }

    @SafeVarargs
    public static <T> boolean isNullOrEmpty(final T... array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    @SafeVarargs
    public static <T> boolean isNotEmpty(final T... array) {
        return !isNullOrEmpty(array);
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * Converte um varargs em Set (HashSet).
     */
    @SafeVarargs
    public static <T> Set<T> newSet(T... values) {
        Set<T> ret = new HashSet<T>();
        for (T t : values) {
            ret.add(t);
        }
        return ret;
    }

    /**
     * Converte um varargs em List (ArrayList).
     */
    @SafeVarargs
    public static <T> List<T> newList(T... values) {
        List<T> ret = new ArrayList<T>();
        for (T t : values) {
            ret.add(t);
        }
        return ret;
    }

    /**
     * Converte vários arrays em List.
     */
    public static <T> List<T> newList(final T[] array1, final T... array2) {
        return Arrays.asList(ArrayUtils.addAll(array1, array2));
    }

    /**
     * Cria um LinkedHashMap a partir de um varargs.
     *
     * @param key
     *            chave da primeira posição
     * @param value
     *            valor da primeira posição
     * @param alteratingsKeysAndValues
     *            chaves e valores das posições seguintes, sempre na sequência: chave, valor, chave, valor, chave, valor, ...
     *
     * @return LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(K key, V value, Object... alteratingsKeysAndValues) {
        LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
        map.put(key, value);
        for (int i = 0; i < alteratingsKeysAndValues.length; i += 2) {
            map.put((K) alteratingsKeysAndValues[i], (V) alteratingsKeysAndValues[i + 1]);
        }
        return map;
    }

    /**
     * Cria um novo mapa do tipo {@link HashMap} a partir de múltiplos mapas. Na prática é um merge.
     *
     * @param maps
     *            mapas
     *
     * @return mapas mergeados
     */
    public static <K, V> HashMap<K, V> newHashMap(Map<K, V>... maps) {
        HashMap<K, V> ret = new HashMap<K, V>();
        for (Map<K, V> map : maps) {
            ret.putAll(map);
        }
        return ret;
    }

    public static String[] toArray(Collection<String> values) {
        return values.stream().toArray(String[]::new);
    }

    public static <T> T[] toArray(T... values) {
        return values;
    }

    /**
     * Converte uma coleção de {@code <T>} em um {@code HashSet<T>}.
     * <p>
     * Esse método também é interessante para remover objetos duplicados.
     * </p>
     *
     * @param values
     *            coleções de {@code <T>}
     *
     * @return {@code HashSet<T>} contendo os elementos de entrada
     */
    @SafeVarargs
    public static <T> HashSet<T> toSet(Collection<T>... values) {
        HashSet<T> ret = new HashSet<T>();
        for (Collection<T> collection : values) {
            ret.addAll(collection);
        }
        return ret;
    }

    /**
     * Esse método utiliza {@code mapa.getClass().newInstance()} para que o mapa de retorno seja do mesmo tipo do mapa de entrada. Se o mapa de entrada for {@code Unmodifiable}, um {@code HashMap}
     * será utilizado.
     */
    public static Map<String, String> toMapStringString(Map<String, ?> mapa) {
        Map ret;
        try {
            ret = mapa.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            ret = new HashMap<String, String>();
        }
        for (Map.Entry<String, ?> entry : mapa.entrySet()) {
            final String vS;
            final Object v = entry.getValue();
            if (v instanceof Collection) {
                vS = Arrays.toString(((Collection) v).toArray());
            } else {
                vS = v == null ? null : v.toString();
            }
            ret.put(entry.getKey(), vS);
        }
        return ret;
    }

    /**
     * Retorna um novo {@link List} contendo <tt><i>a</i> - <i>b</i></tt>.
     *
     * @param a
     *            a coleção a ser subtraída, não deve ser nulo
     * @param b
     *            a coleção a subtrair, não deve ser nulo
     *
     * @return {@link List} com o resultado
     *
     * @see org.apache.commons.collections.CollectionUtils#subtract(Collection, Collection)
     */
    public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
        ArrayList<T> list = new ArrayList<T>(a);
        for (Iterator<T> it = b.iterator(); it.hasNext();) {
            list.remove(it.next());
        }
        return list;
    }

    /**
     * Retorna um novo {@link Set} contendo <tt><i>a</i> - <i>b</i></tt>.
     *
     * @param a
     *            a coleção a ser subtraída, não deve ser nulo
     * @param b
     *            a coleção a subtrair, não deve ser nulo
     *
     * @return {@link Set} com o resultado
     *
     * @see org.apache.commons.collections.CollectionUtils#subtract(Collection, Collection)
     */
    public static <T> Set<T> subtractToSet(final Collection<T> a, final Collection<T> b) {
        return new HashSet<T>(subtract(a, b));
    }

    /**
     * Retorna um novo {@link Set} contendo a união de <tt><i>a</i> e <i>b</i></tt>.
     *
     * @param a
     *            Set 1, não deve ser nulo
     * @param b
     *            Set 2, pode ser nulo
     *
     * @return novo Set com o resultado
     */
    public static <T> Set<T> union(final Set<T> a, final Collection<T> b) {
        if (b == null) {
            return a;
        }
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet());
    }

    /**
     * Retorna o menor elemento da coleção de acordo com a ordem natural dos elementos.
     * <p>
     * {@code coll} pode ser {@code null}
     * </p>
     *
     * @param coll
     *            coleção
     *
     * @return menor elmento
     *
     * @see Collections#min(Collection)
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll) {
        if (isNullOrEmpty(coll)) {
            return null;
        }
        return Collections.min(coll);
    }

    /**
     * Retorna o primeiro elemento de {@code first} que está contido em {@code second}.
     *
     * <p>
     * Exemplo:
     * </p>
     * <p>
     * Entrada:
     * <ul>
     * <li>first: {0, 3, 5, 9, 3}</li>
     * <li>second: {7, 2, 9, 1, 3}</li>
     * </ul>
     * Saída: 3
     * </p>
     *
     * @param first
     *            primeira coleção
     * @param second
     *            segunda coleção
     *
     * @return elemento encontrado
     */
    public static <T> Optional<T> findFirst(Collection<T> first, Collection<T> second) {
        if (first == null || second == null) {
            return Optional.empty();
        }
        return first.stream().filter(second::contains).findFirst();
    }

}
