package com.zlc.tienchin.framework.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author zlc
 * @date 2023/12/11 22:47
 */
public class CustomDefaultMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomSecurityExpressionRoot customSecurityExpressionRoot = new CustomSecurityExpressionRoot(authentication);
        customSecurityExpressionRoot.setTrustResolver(getTrustResolver());
        customSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
        customSecurityExpressionRoot.setRoleHierarchy(getRoleHierarchy());
        customSecurityExpressionRoot.setDefaultRolePrefix(getDefaultRolePrefix());
        return customSecurityExpressionRoot;
    }
}
