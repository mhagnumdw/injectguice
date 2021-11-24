package io.github.mhagnumdw;

import java.util.List;
import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;

import io.github.mhagnumdw.dao.DAO;
import io.github.mhagnumdw.dao.GeneralDAO;
import io.github.mhagnumdw.util.ScanUtils;
import ro.pippo.controller.Controller;
import ro.pippo.controller.ControllerFactory;
import ro.pippo.controller.ControllerInitializationListenerList;
import ro.pippo.controller.ControllerInstantiationListenerList;
import ro.pippo.controller.ControllerInvokeListenerList;
import ro.pippo.controller.Path;
import ro.pippo.controller.extractor.MethodParameterExtractor;
import ro.pippo.core.Application;
import ro.pippo.core.ContentTypeEngines;
import ro.pippo.core.ErrorHandler;
import ro.pippo.core.Initializer;
import ro.pippo.core.Languages;
import ro.pippo.core.Messages;
import ro.pippo.core.Pippo;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.core.TemplateEngine;
import ro.pippo.core.WebServer;
import ro.pippo.core.route.RouteHandler;
import ro.pippo.core.route.RoutePostDispatchListenerList;
import ro.pippo.core.route.RoutePreDispatchListenerList;
import ro.pippo.core.route.Router;
import ro.pippo.core.util.HttpCacheToolkit;
import ro.pippo.core.util.MimeTypes;
import ro.pippo.core.websocket.WebSocketRouter;
import ro.pippo.freemarker.FreemarkerTemplateEngine;
import ro.pippo.jetty.JettyServer;

/**
 * Módulo Guice para os recursos do Pippo.
 */
public final class PippoGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        System.out.println("Start guice module configure");

        bind(DAO.class).to(GeneralDAO.class);

        bind(Application.class).to(PippoApplication.class).asEagerSingleton();
        // bind(Router.class).to(CustomRouter.class).in(Scopes.SINGLETON);
        bind(TemplateEngine.class).to(FreemarkerTemplateEngine.class).asEagerSingleton();
        bind(WebServer.class).to(JettyServer.class).in(Scopes.SINGLETON);

        bind(Pippo.class);

        bindControllers();

        bindOptionalApplication();
        bindOptionalControllerApplication();

        System.out.println("End guice module configure");
    }

    private void bindOptionalApplication() {
        OptionalBinder.newOptionalBinder(binder(), ContentTypeEngines.class);
        OptionalBinder.newOptionalBinder(binder(), ErrorHandler.class);
        OptionalBinder.newOptionalBinder(binder(), HttpCacheToolkit.class);
        OptionalBinder.newOptionalBinder(binder(), Languages.class);
        OptionalBinder.newOptionalBinder(binder(), Messages.class);
        OptionalBinder.newOptionalBinder(binder(), MimeTypes.class);
        OptionalBinder.newOptionalBinder(binder(), Router.class);
        OptionalBinder.newOptionalBinder(binder(), WebSocketRouter.class);
        OptionalBinder.newOptionalBinder(binder(), RequestResponseFactory.class);
        OptionalBinder.newOptionalBinder(binder(), RoutePreDispatchListenerList.class);
        OptionalBinder.newOptionalBinder(binder(), RoutePostDispatchListenerList.class);
        OptionalBinder.newOptionalBinder(binder(), TemplateEngine.class);
        OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<RouteHandler<?>>(){});
        OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<List<Initializer>>(){});
    }

    private void bindOptionalControllerApplication() {
        OptionalBinder.newOptionalBinder(binder(), ControllerFactory.class);
        OptionalBinder.newOptionalBinder(binder(), ControllerInitializationListenerList.class);
        OptionalBinder.newOptionalBinder(binder(), ControllerInstantiationListenerList.class);
        OptionalBinder.newOptionalBinder(binder(), ControllerInvokeListenerList.class);
        OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<List<MethodParameterExtractor>>(){});
    }

    private void bindControllers() {
        // retrieve controller classes
        // Set<Class<? extends Controller>> controllers = ScanUtils.getSubTypesOf(Controller.class);
        Set<Class<? extends Controller>> controllers = ScanUtils.getSubTypesOfAnnotatedWith(Controller.class, Path.class);

        // bind found controllers
        Multibinder<Controller> multibinder = Multibinder.newSetBinder(binder(), Controller.class);
        controllers.forEach(controller -> multibinder.addBinding().to(controller));
    }

}
