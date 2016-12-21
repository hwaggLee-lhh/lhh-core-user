package com.lhh.user.security;

import java.util.List;

import com.lhh.user.core.service.ResourceDetails;

/**
 * 为 {@link com.lhh.core.core.model.MyResource.components.acegi.resource.Resource} 实例提供缓存.
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
public interface LhhCoreResourceCache {

	public ResourceDetails getResourceFromCache(String resString);
	
	public void putResourceInCache(ResourceDetails resourceDetails);
	
	public void removeResourceFromCache(String resString);
	
	public List<String> getAllResources();

	public void removeAllResources();
	
}