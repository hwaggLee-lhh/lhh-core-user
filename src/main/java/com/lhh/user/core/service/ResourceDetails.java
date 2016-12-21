package com.lhh.user.core.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;

/**
 * 提供资源信息
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
public interface ResourceDetails extends Serializable {

	/**
	 * 资源串
	 */
	public String getResString();

	/**
	 * 资源类型,如URL,FUNCTION
	 */
	public String getResType();

	/**
	 * 返回属于该resource的authorities
	 */
	List<ConfigAttribute> getConfigAttributeList();
}
