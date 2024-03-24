package com.zlc.family.framework.config;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

/**
 * @author zlc
 * @date 2023/12/11 22:40
 */
public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    /**
     * 只要用户有一个权限匹配那么就允许访问该资源
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (antPathMatcher.match(authority.getAuthority(), permission.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 多个权限匹配中，用户只要有一个权限可以匹配上就能访问该资源
     * @param permissions
     * @return
     */
    public boolean hasAnyPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            for (String permission : permissions) {
                if (antPathMatcher.match(authority.getAuthority(), permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 多个权限匹配中，用户必须要全部权限匹配上才能访问该资源
     * @param permissions
     * @return
     */
    public boolean hasAllPermissions(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return false;
        }
        for (String permission : permissions) {
            boolean flag = false;
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (antPathMatcher.match(authority.getAuthority(), permission)) {
                    flag = true;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
