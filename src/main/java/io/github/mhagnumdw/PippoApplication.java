package io.github.mhagnumdw;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.persist.PersistService;

import io.github.mhagnumdw.filter.PersistFilter;
import ro.pippo.controller.Controller;
import ro.pippo.controller.ControllerApplication;

/**
 * Configuração da aplicação.
 *
 * @see io.github.mhagnumdw.PippoLauncher#main(String[])
 */
public class PippoApplication extends ControllerApplication {

    private static final Logger log = LoggerFactory.getLogger(PippoApplication.class);

    @Inject
    private PersistService service;

//    @Inject
//    private NotaService notaService;

    @Inject
    // private List<? extends Controller> controllers;
    // private Set<? extends Controller> controllers;
    // private Set<Controller> controllers;
    private Set<Controller> controllers;

    @Override
    protected void onInit() {
        log.info("onInit()");

        // http://docs.jboss.org/hibernate/orm/5.2/topical/html_single/logging/Logging.html
        System.setProperty("org.jboss.logging.provider", "slf4j"); // Necessário para o Hibernate quando se usa o slf4j

        // PERSISTENCE
        log.info("Starting Persistence Unit (JPA)");
        service.start(); // start persistence unit (JPA)

        // ENDPOINT / API
        // setControllerFactory(new GuiceControllerFactory(injector)); // registrando GuiceControllerFactory

        // add routes for static content
        addPublicResourceRoute();
        addWebjarsResourceRoute();

        // **************************************************************************
        // ****** FILTER's - A Ordem É Relevante! ***********************************
        // **************************************************************************

        ANY("/webapp/api/.*", PersistFilter.BEGIN);

        addControllers(controllers.toArray(new Controller[0]));

        // Cleanup - Deve ser realmente a última chamada do método onInit() e "/.*"
        ANY("/webapp/api/.*", PersistFilter.END).runAsFinally();

    }

}
