package io.vertx.ext.web.handler.impl;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.SecureUpgradeHandler;


public class SecureUpgradeHandlerImpl implements SecureUpgradeHandler {

    final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    final String X_FORWARDED_HOST = "X-Forwarded-Host";
    final String X_FORWARDED_PORT = "X-Forwarded-Port";

    @Override
    public void handle(RoutingContext event) {
        HttpServerRequest request = event.request();
        String uri = request.uri();

        if (uri.startsWith("http://") || uri.startsWith("https://")) {
            event.next();
            return;
        }
        if (uri.startsWith("//")) {
            event.reroute(getScheme(event) + ":" + uri);
            return;
        }
        if (uri.startsWith("/")) {
            event.reroute(getScheme(event) + "://" + getHost(event) + uri);
            return;
        }
        //TODO this is not fully correct
        event.reroute(getScheme(event) + "://" + getHost(event) + ":" + getPort(event) + "/" + uri);
    }

    private String getPort(RoutingContext event) {
        if (hasHeader(event, X_FORWARDED_PORT)) {
            return event.request().headers().get(X_FORWARDED_PORT);
        }
        return Integer.toString(event.request().localAddress().port());
    }

    private String getHost(RoutingContext event) {
        if (hasHeader(event, X_FORWARDED_HOST)) {
            return event.request().headers().get(X_FORWARDED_HOST);
        }
        //TODO host might contain port
        return event.request().localAddress().host();
    }

    private String getScheme(RoutingContext event) {
        if (hasHeader(event, X_FORWARDED_PROTO)) {
            return event.request().headers().get(X_FORWARDED_PROTO);
        }
        //Unknown if this is the right scheme
        return event.request().isSSL() ? "https" : "http";
    }

    private boolean hasHeader(RoutingContext event, String header) {
        return event.request().headers().contains(header);
    }
}
