package com.lhh.user.security;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lhh.user.core.service.ResourceDetails;

import org.springframework.dao.DataRetrievalFailureException;

/**
 * 缓存
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class LhhCoreEhCacheBasedResourceCache implements LhhCoreResourceCache {

	// ~ Static fields/initializers
	// =============================================

	private static final Log logger = LogFactory.getLog(LhhCoreEhCacheBasedResourceCache.class);

	// ~ Instance fields
	// ========================================================

	Cache cache;

	// ~ Methods
	// ================================================================

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public Cache getCache() {
		return this.cache;
	}

	/* (non-Javadoc)
	 * @see org.springside.components.acegi.cache.ResourceCache#getResourceFromCache(java.lang.String)
	 */
	public ResourceDetails getResourceFromCache(String resString) {
		Element element = null;

		try {
			element = cache.get(resString);
		} catch (CacheException cacheException) {
			throw new DataRetrievalFailureException("Cache failure: "
					+ cacheException.getMessage(), cacheException);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Cache hit: " + (element != null) + "; resString: "
					+ resString);
		}

		if (element == null) {
			return null;
		} else {
			return (ResourceDetails) element.getValue();
		}
	}

	/* (non-Javadoc)
	 * @see org.springside.components.acegi.cache.ResourceCache#putResourceInCache(org.springside.components.acegi.model.ResourceDetails)
	 */
	public void putResourceInCache(ResourceDetails resourceDetails) {
		Element element = new Element(resourceDetails.getResString(),
				resourceDetails);
		if (logger.isDebugEnabled()) {
			logger.debug("Cache put: " + element.getKey());
		}
		this.cache.put(element);
	}

	/* (non-Javadoc)
	 * @see org.springside.components.acegi.cache.ResourceCache#removeResourceFromCache(java.lang.String)
	 */
	public void removeResourceFromCache(String resString) {
		if (logger.isDebugEnabled()) {
			logger.debug("Cache remove: " + resString);
		}
		this.cache.remove(resString);
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllResources() {
		return this.cache.getKeys();
	}
	
	public void removeAllResources() {
		this.cache.removeAll();
	}

}