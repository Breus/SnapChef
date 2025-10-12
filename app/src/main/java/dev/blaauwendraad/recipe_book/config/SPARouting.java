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
            int assetsIndex = path.indexOf("/assets/");
            if (assetsIndex > 0) {
                // Redirect to '/assets/' root path, where they are packaged in the Quarkus app as static resources
                String assetPath = path.substring(assetsIndex);
                rc.reroute(assetPath);
                return;
            }
            boolean isApi = path.startsWith("/api"); // API calls should not be redirected to SPA
            boolean isRoot = path.equals("/"); // already on the root page, no need to redirect
            if (!isRoot && !isApi) {
                rc.reroute("/");
            } else {
                rc.next();
            }
        });
    }
}
