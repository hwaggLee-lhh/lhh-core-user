package com.lhh.user.core.service;

import java.util.List;

import com.lhh.user.core.service.impl.MyResource;

import org.springframework.security.core.userdetails.User;

/**
 * 为缓存提供 User 和 Resource 实例
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
public interface AuthenticationService {
	
	public List<User> getUsers();
	
	public List<MyResource> getResources();
}
