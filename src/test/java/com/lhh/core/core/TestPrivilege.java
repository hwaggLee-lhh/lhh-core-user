package com.lhh.core.core;

import java.util.List;

import javax.annotation.Resource;

import com.lhh.core.BaseServiceTest;
import com.lhh.user.core.model.Privilege;
import com.lhh.user.core.service.PrivilegeService;

import org.junit.Test;

public class TestPrivilege extends BaseServiceTest {
	@Resource
	private PrivilegeService privilegeService;
	@Test
	public void test() {
		
	}
	@Test
	public void testPrivilegeList() {
		List<Privilege> privilegeList = privilegeService.findList();
		if(privilegeList==null) return;
		for (Privilege privilege : privilegeList) {
			System.out.println(privilege.getPrivilName());
			System.out.println(privilege.getPattern());
		}
	}
}
