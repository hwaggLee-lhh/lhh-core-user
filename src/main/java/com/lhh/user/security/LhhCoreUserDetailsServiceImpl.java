package com.lhh.user.security;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 登陆时从缓存里获取用户，而不是从数据库中获取用户实例
 * 实现loadUserByUsername(String username) 方法
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreUserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserCache userCache;
	
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		UserDetails ud = userCache.getUserFromCache(username);
		if(ud==null) throw new UsernameNotFoundException(username+"用户不存在");
		return ud;
	}
	
}