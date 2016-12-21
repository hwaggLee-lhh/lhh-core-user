package com.lhh.user.core.service.impl;

import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.Assert;

import com.lhh.user.core.service.ResourceDetails;

/**
 * ResourceDetails的实现类
 * resString 资源串， 如Url资源串 /admin/index.jsp, Method资源串 com.abc.service.userManager.save 等
 * resType 资源类型，如URL, METHOD 等
 * authorities 该资源所拥有的权限
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
public class MyResource implements ResourceDetails {
	
	private static final long serialVersionUID = 4640497640533200574L;

	public static final String RESOURCE_TYPE_URL = "URL";

	public static final String RESOURCE_TYPE_METHOD = "METHOD";

	public static final String RESOURCE_TYPE_TAG = "TAG";

	private String resString;

	private String resType;
	private List<ConfigAttribute> configAttributeList;

	public MyResource(String resString, String resType, List<ConfigAttribute> configAttributeList) {
		Assert.notNull(resString,"Cannot pass null or empty values to resource string");
		Assert.notNull(resType,"Cannot pass null or empty values to resource type");
		this.resString = resString;
		this.resType = resType;
		setConfigAttributeList(configAttributeList);
	}

	public boolean equals(Object rhs) {
		if (!(rhs instanceof MyResource))
			return false;
		MyResource resauth = (MyResource) rhs;
		if(!getResString().equals(resauth.getResString()))
			return false;
		if(!getResType().equals(resauth.getResType()))
			return false;
		if (resauth.getConfigAttributeList().size() != getConfigAttributeList().size())
			return false;
		for (int i = 0; i < getConfigAttributeList().size(); i++) {
			if (!getConfigAttributeList().get(i).equals(resauth.getConfigAttributeList().get(i)))
				return false;
		}
		return  true ;
	}

	public int hashCode() {
		int code = 168;
		if (getConfigAttributeList() != null) {
			for (int i = 0; i < getConfigAttributeList().size(); i++)
				code *= getConfigAttributeList().get(i).hashCode() % 7;
		}
		if (getResString() != null)
			code *= getResString().hashCode() % 7;
		return code;
	}

	public String getResString() {
		return resString;
	}

	public void setResString(String resString) {
		this.resString = resString;
	}

	public List<ConfigAttribute> getConfigAttributeList() {
		return configAttributeList;
	}

	public void setConfigAttributeList(
			List<ConfigAttribute> configAttributeList) {
		this.configAttributeList = configAttributeList;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

}
