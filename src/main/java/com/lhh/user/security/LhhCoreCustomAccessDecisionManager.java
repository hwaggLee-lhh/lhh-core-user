package com.lhh.user.security;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.lhh.user.LhhUser;

/**
 * 权限认证
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreCustomAccessDecisionManager extends AbstractAccessDecisionManager {
	
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null || configAttributes.size() == 0) {
			return;
		}
		//拥有系统管理员角色，即拥有系统的所有权限
		for (GrantedAuthority ga : authentication.getAuthorities()) {
			if (LhhUser.systemrole.equals(ga.getAuthority())) {
				return;
			}
		}
		for (ConfigAttribute configAttribute : configAttributes) {
			// 访问所请求资源所需要的权限
			String needPermission = configAttribute.getAttribute();
			// 用户所拥有的权限authentication
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (needPermission.equals(ga.getAuthority())) {
					return;
				}
			}
		}
		// 没有权限
		throw new AccessDeniedException(" No Access Dendied ");
	}

}
