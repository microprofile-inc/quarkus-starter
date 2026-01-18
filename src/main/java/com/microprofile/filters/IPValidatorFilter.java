/**
 * usage @IPBound // 然后检查IP地址
 */

package com.microprofile.filters;

import io.vertx.core.http.HttpServerRequest;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Provider
@IPBound
@ApplicationScoped
@Priority(Priorities.AUTHENTICATION + 10) // 确保在JWT身份验证之后运行
public class IPValidatorFilter implements ContainerRequestFilter {

    @Inject
    JsonWebToken jwt;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        // 如果JWT不存在或无效，Quarkus的JWT扩展会先处理，这里jwt.getName()会是null
        if (jwt == null || jwt.getName() == null) {
            // 让Quarkus的默认安全机制处理
            return;
        }

        // 从JWT中获取IP地址
        String tokenIp = jwt.getClaim("ip");
        
        // 获取当前请求的IP地址
        String requestIp = request.remoteAddress().host();

        if (tokenIp == null || !tokenIp.equals(requestIp)) {
            // IP不匹配，拒绝请求
            requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN)
                        .entity("IP address mismatch")
                        .build()
            );
        }
    }
}