package io.vertx.ext.web.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.SecureUpgradeHandlerImpl;

public interface SecureUpgradeHandler extends Handler<RoutingContext> {

    /**
     * Create a handler
     *
     * @return the handler
     */
    static SecureUpgradeHandler create() {
        return new SecureUpgradeHandlerImpl();
    }
}
