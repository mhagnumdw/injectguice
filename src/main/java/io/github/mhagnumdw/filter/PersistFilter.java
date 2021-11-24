package io.github.mhagnumdw.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.persist.UnitOfWork;

import io.github.mhagnumdw.helper.UnitOfWorkHelper;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteHandler;

/**
 * Filtro criado com base na ideia do filtro {@link com.google.inject.persist.PersistFilter}.
 * <p>
 * https://github.com/google/guice/wiki/JPA
 * </p>
 */
public class PersistFilter implements RouteHandler<RouteContext> {

    private static final Logger log = LoggerFactory.getLogger(PersistFilter.class);

    private static final String KEY_UNIT_OF_WORK = "UnitOfWork";

    public static final PersistFilter BEGIN = new PersistFilter(true);
    public static final PersistFilter END = new PersistFilter(false);

    private final boolean begin;

    private PersistFilter(boolean begin) {
        this.begin = begin;
    }

    @Override
    public void handle(RouteContext routeContext) {
        if (begin) { // begin
            final UnitOfWork unitOfWork = UnitOfWorkHelper.begin(log);
            routeContext.setLocal(KEY_UNIT_OF_WORK, unitOfWork);
            routeContext.next();
        } else { // end
            final UnitOfWork unitOfWork = routeContext.removeLocal(KEY_UNIT_OF_WORK);
            if (unitOfWork != null) {
                UnitOfWorkHelper.end(unitOfWork, log);
            }
            // routeContext.next(); // Comentado de propósito pois este DEVE ser o último filtro
        }
    }

}
