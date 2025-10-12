package dev.blaauwendraad.recipe_book.config;

import io.vertx.ext.web.Router;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

/**
 * This ensures that all requests that do not start with /api/ are redirected to the VueJS (SPA) frontend
 */
@ApplicationScoped
public class SPARouting {

    public void init(@Observes Router router) {
        router.get("/*").handler(rc -> {
            String path = rc.normalizedPath();
            boolean isApi = path.startsWith("/api");
            boolean isRoot = path.equals("/");
            boolean isAsset = path.contains("/assets/") || path.matches(".*\\.(js|css|png|jpg|jpeg|svg|ico)$");
            if (!isRoot && !isApi && !isAsset) {
                rc.reroute("/");
            } else {
                rc.next();
            }
        });
    }
}
