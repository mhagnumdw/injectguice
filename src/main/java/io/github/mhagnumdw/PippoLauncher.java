package io.github.mhagnumdw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;

import ro.pippo.core.Pippo;

public class PippoLauncher {

    private static final Logger log = LoggerFactory.getLogger(PippoLauncher.class);

    /**
     * Método principal que inicia a aplicação no modo standalone.
     */
    public static void main(String[] args) {
        log.info("Iniciando aplicação ...");

        // TODO: tornar o enable/disable disso via aplicação (talvez na tela Sistema > Admin)
        // Tracing XML request/responses with JAX-WS to console
        // https://stackoverflow.com/questions/1945618/tracing-xml-request-responses-with-jax-ws#16338394
        // System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        // System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        // System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        // System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        Injector injector = Guice.createInjector(new PippoGuiceModule(), new JpaPersistModule(Constantes.PU_NAME));

        GuiceInjector.set(injector);
        Pippo pippo = injector.getInstance(Pippo.class);

        pippo.start();

        log.info("Aplicação iniciada!");
    }

}
