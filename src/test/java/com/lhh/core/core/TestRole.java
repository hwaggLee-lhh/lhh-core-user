package com.lhh.core.core;

import javax.annotation.Resource;

import org.junit.Test;

import com.lhh.core.BaseServiceTest;
import com.lhh.user.core.service.RoleService;

public class TestRole extends BaseServiceTest {
	@Resource
	private RoleService roleService;
	@Test
	public void test() {
		
	}
	@Test
	public void testRoleList() {
//		List<Role> roleList = roleService.findList();
//		if(roleList==null) return;
//		for (Role role : roleList) {
//			System.out.println(role.getRoleName());
//		}
	}
}
