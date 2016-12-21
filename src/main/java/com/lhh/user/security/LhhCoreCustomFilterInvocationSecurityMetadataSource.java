package com.lhh.user.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lhh.user.core.service.ResourceDetails;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

public class LhhCoreCustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	private LhhCoreResourceCache resourceCache;
	public void setResourceCache(LhhCoreResourceCache resourceCache) {
		this.resourceCache = resourceCache;
	}

	public LhhCoreCustomFilterInvocationSecurityMetadataSource() {
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		List<String> resourceList = resourceCache.getAllResources();
		if (resourceList == null || resourceList.size() == 0) return null;
		Set<ConfigAttribute> set = new HashSet<ConfigAttribute>();
		for (String resourceKey : resourceList) {
			ResourceDetails resourceDetails = resourceCache.getResourceFromCache(resourceKey);
			if(resourceDetails==null) continue;
			set.addAll(resourceDetails.getConfigAttributeList());
		}
		return null;
	}

	public Collection<ConfigAttribute> getAttributes(Object arg0)
			throws IllegalArgumentException {
		List<String> resourceList = resourceCache.getAllResources();
		if (resourceList == null || resourceList.size() == 0) return null;
		String url = ((FilterInvocation) arg0).getRequestUrl();
		for (String resourceKey : resourceList) {
			if(antPathMatcher.match(resourceKey, url)) {
				ResourceDetails resourceDetails = resourceCache.getResourceFromCache(resourceKey);
				if(resourceDetails==null) continue;
				return resourceDetails.getConfigAttributeList();
			}
		}
		return null;
	}

	public Collection<ConfigAttribute> getAttributes(String url)
			throws IllegalArgumentException {
		List<String> resourceList = resourceCache.getAllResources();
		if (resourceList == null || resourceList.size() == 0) return null;
		for (String resourceKey : resourceList) {
			if(antPathMatcher.match(resourceKey, url)) {
				ResourceDetails resourceDetails = resourceCache.getResourceFromCache(resourceKey);
				if(resourceDetails==null) continue;
				return resourceDetails.getConfigAttributeList();
			}
		}
		return null;
	}

	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return true;
	}
}
