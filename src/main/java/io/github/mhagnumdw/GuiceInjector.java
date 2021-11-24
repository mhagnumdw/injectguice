package io.github.mhagnumdw;

import com.google.inject.Injector;

/**
 * Recupera e seta o {@code Injector} do Guice utilizado pela aplicação.
 */
public final class GuiceInjector {

    private static final String KEY = Constantes.KEY_PROP_GUICE_INJECTOR;

    /**
     * Recupera o {@code Injetor} do Guice utilizado pela aplicação.
     *
     * @return {@code Injetor} do Guice utilizado pela aplicação
     */
    public static Injector get() {
        return (Injector) MemoryStorage.get(KEY);
    }

    /**
     * Seta {@code Injetor} do Guice utilizado pela aplicação.
     *
     * @param injector
     *            Guice Injector
     */
    static void set(Injector injector) {
        MemoryStorage.put(KEY, injector);
    }

}
