package com.lhh.user.core.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.BaseManager;
import com.lhh.core.base.LhhCoreBaseServiceImpl;
import com.lhh.user.core.manager.PrivilegeManager;
import com.lhh.user.core.model.Privilege;
import com.lhh.user.core.service.PrivilegeService;

@Component("privilegeService")
public class PrivilegeServiceImpl extends LhhCoreBaseServiceImpl<Privilege> implements PrivilegeService {
	
	@Resource
	private PrivilegeManager privilegeManager;
	
	public LhhPage<Privilege> findPage(Privilege example, LhhPageParam pageParam) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected BaseManager<Privilege> getBaseManager() {
		return privilegeManager;
	}

}
