package com.lhh.user.security;

import java.util.Collection;

import javax.annotation.Resource;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lhh.user.LhhUser;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;


/**
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreAuthenticationUtil {

	public void setAccessDecisionVoter(RoleVoter accessDecisionVoter) {
		this.accessDecisionVoter = accessDecisionVoter;
	}

	private RoleVoter accessDecisionVoter;
	@Resource
	private UserService userService;
	
    public void setSecurityMetadataSource(
			LhhCoreCustomFilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	private LhhCoreCustomFilterInvocationSecurityMetadataSource securityMetadataSource;

	public String getCurrentUserName() {
        Authentication currentUser = getCurrentUserAuthentication();
        if (currentUser != null) {
            return currentUser.getName();
        }
        return null;
    }

    public User getCurrentUserPO() {
        String userName = getCurrentUserName();
        if  (userName == null) {
            return null;
        }
        return userService.findByUserName(userName);
    }
    
    public Authentication getCurrentUserAuthentication() {
        Authentication currentUser = null;
        SecurityContext context = SecurityContextHolder.getContext();
        if (null != context) {
            currentUser = context.getAuthentication();
        }
        return currentUser;
    }

    public boolean isAccessableTo(String accessPattern) {
    	Collection<ConfigAttribute> cad = securityMetadataSource.getAttributes(accessPattern);
        if (null == cad) {
            return true;
        }
        Authentication authen = getCurrentUserAuthentication();
        if(authen != null) {
            //拥有系统管理员角色，即拥有系统的所有权限
      		for (GrantedAuthority ga : authen.getAuthorities()) {
      			if (LhhUser.systemrole.equals(ga.getAuthority())) {
      				return true;
      			}
      		}
            return AccessDecisionVoter.ACCESS_GRANTED == accessDecisionVoter.vote(authen, null, cad);
        }else {
            return false;
        }
    }
    
    /**
     * 判断是否是管理员(true:是管理员，false:不是管理员)
     * @return
     */
    public boolean isSystemMROLE(){
    	 Authentication authen = getCurrentUserAuthentication();
         if(authen != null) {
             //拥有系统管理员角色，即拥有系统的所有权限
       		for (GrantedAuthority ga : authen.getAuthorities()) {
       			if (LhhUser.systemrole.equals(ga.getAuthority())) {
       				return true;
       			}
       		}
         }
         return false;
    }
}
