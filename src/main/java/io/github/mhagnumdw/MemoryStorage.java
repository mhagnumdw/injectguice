package io.github.mhagnumdw;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Singleton que carrega em memória diversos objetos para o funcionamento da aplicação.
 */
public class MemoryStorage {

    private static HashMap<String, Object> instance = null;

    private static Map<String, Object> getInstance() {
        if (instance == null) {
            synchronized (MemoryStorage.class) {
                if (instance == null) {
                    instance = new HashMap<String, Object>();
                }
            }
        }
        return instance;
    }

    /**
     * Adiciona uma chave/valor ao mapa {@link #instance}.
     *
     * @param key
     *            chave
     * @param value
     *            valor
     */
    public static void put(String key, Object value) {
        getInstance().put(key, value);
    }

    /**
     * Remove uma chave (e seu valor) do mapa.
     *
     * @param key
     *            chave
     */
    public static void remove(String key) {
        if (StringUtils.isNotBlank(key)) {
            getInstance().remove(key);
        }
    }

    /**
     * Retorna um valor do mapa {@link #instance} com base na chave especificada.
     *
     * @param key
     *            chave
     * @return valor da chave
     */
    public static Object get(String key) {
        return getInstance().get(key);
    }

    /**
     * Retorna um valor do mapa {@link #instance} com base na chave especificada.
     * <p>
     * Força um cast para {@code T}, logo pode surgir um {@link ClassCastException}.
     * </p>
     *
     * @param key
     *            chave
     * @param typeOfT
     *            tipo do retorno (vai forçar um cast)
     *
     * @return valor da chave
     */
    public static <T> T get(String key, Class<T> typeOfT) {
        return (T) getInstance().get(key);
    }

}
