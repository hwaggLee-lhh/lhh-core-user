package com.lhh.user.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;
import org.springframework.util.Assert;

import com.lhh.user.core.service.AuthenticationService;
import com.lhh.user.core.service.ResourceDetails;
import com.lhh.user.core.service.impl.MyResource;

/**
 * AcegiCacheManager 负责初始化缓存
 * 调用 authenticationService 来获取资源和用户实例，并加入UserCache 和 ResourceCache 中
 * 
 * AcegiCacheManager 对缓存进行统一管理，以屏蔽其它类对缓存的直接操作
 * 对缓存中的用户和资源进行初始化、增、删、改、清空等操作
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreSecurityCacheManager implements InitializingBean{

	@Resource
    private AuthenticationService authenticationService;
	@Resource
	private UserCache userCache;

	@Resource
	private LhhCoreResourceCache resourceCache;
	
	//rescTypeMapp 映射资源类型对应的资源的一对多关系，以便快速查找。
	//如method类型对应哪些资源实例，url资源类型对应哪些资源实例
	private Map<String, List<String>> rescTypeMapping;
    
	public void init() {
        Assert.notNull(userCache,"userCache should not be null");
        Assert.notNull(resourceCache,"resourceCache should not be null");
        Assert.notNull(authenticationService,"Authentication Service should not be null");
 
        //初始化缓存
        List<User> users =authenticationService.getUsers();
        for (User user : users) {
        	userCache.putUserInCache(user);
		}
        
        List<MyResource> rescs =authenticationService.getResources();
        for (MyResource myResource : rescs) {
        	resourceCache.putResourceInCache(myResource);
		}
        
		// 获取所有的资源,以初始化 rescTypeMapping
		rescTypeMapping = new HashMap<String, List<String>>();
		List<String> resclist = resourceCache.getAllResources();
		for (String resString : resclist) {
			ResourceDetails resc = resourceCache.getResourceFromCache(resString);
			List<String> typelist = rescTypeMapping.get(resc.getResType());
			if(typelist==null){
				typelist = new ArrayList<String>();
				rescTypeMapping.put(resc.getResType(), typelist);
			}
			typelist.add(resString);
		}
	}
	
	//-----get from cache methods
	public UserDetails getUser(String username) {
		return userCache.getUserFromCache(username);
	}
	
	public ResourceDetails getResourceFromCache(String resString) {
		return resourceCache.getResourceFromCache(resString);
	}
	
	//-----remove from cache methods
	public void removeUser(String username){
		userCache.removeUserFromCache(username);
	}
	
	public void removeResource(String resString){
		ResourceDetails rd = resourceCache.getResourceFromCache(resString);
		List<String> typelist = rescTypeMapping.get(rd.getResType());
		typelist.remove(resString);
		resourceCache.removeResourceFromCache(resString);
	}
	
	public void addUser(UserDetails user){
		userCache.putUserInCache(user);
	}
	public void addResource(ResourceDetails rd){
		List<String> typelist = rescTypeMapping.get(rd.getResType());
		if(typelist==null){
			typelist = new ArrayList<String>();
			rescTypeMapping.put(rd.getResType(), typelist);
		}
		typelist.add(rd.getResString());
		resourceCache.putResourceInCache(rd);
	}
	
	public void renovateUser(String orgUsername, UserDetails user){
		removeUser(orgUsername);
		addUser(user);
	}
	public void renovateResource(String orgResString,ResourceDetails rd){
		removeResource(orgResString);
		addResource(rd);
	}
	
    public void reInit() {
        clearResources();
        init();
    }
    
	//-------getters and setters-------------------
	public void clearResources() {
	    rescTypeMapping.clear();
		resourceCache.removeAllResources();
	}

	public void setResourceCache(LhhCoreResourceCache resourceCache) {
		this.resourceCache = resourceCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
	

	/**
	 * 根据资源类型,在rescTypeMapping职工获取所有该类型资源的对应的resource string
	 * @param resType
	 * @return List
	 */
	public List<String> getResourcesByType(String resType) {	
		return rescTypeMapping.get(resType);
	}
	
	/**
	 * 获取所有资源的对应的resource string
	 * @return
	 */
	public List<String> getAllResources(){
		return resourceCache.getAllResources();
	}
	
	/**
	 * 获取所有用户实例对应的user name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllUsers(){
		EhCacheBasedUserCache ehUserCache = (EhCacheBasedUserCache)userCache;
		return ehUserCache.getCache().getKeys();
	}

    public void afterPropertiesSet() throws Exception {
        init();
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
	
}
