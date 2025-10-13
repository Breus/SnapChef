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
            // If the path contains /assets/ (not at root), reroute to the correct asset path and return immediately
            if (assetsIndex > 0) {
                String assetPath = path.substring(assetsIndex);
                rc.reroute(assetPath);
                return;
            }
            boolean isApi = path.startsWith("/api"); // API calls should not be redirected to SPA
            boolean isRoot = path.equals("/"); // already on the root page, no need to redirect
            boolean isAsset = path.startsWith("/assets/") || path.matches(".*\\.(js|css|png|jpg|jpeg|svg|ico)$");
            if (!isRoot && !isApi && !isAsset) {
                rc.reroute("/");
            } else {
                rc.next();
            }
        });
    }
}
