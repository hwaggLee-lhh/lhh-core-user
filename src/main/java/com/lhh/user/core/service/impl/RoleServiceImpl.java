package com.lhh.user.core.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.BaseManager;
import com.lhh.core.base.LhhCoreBaseServiceImpl;
import com.lhh.format.lang.LhhUtilsStr;
import com.lhh.user.core.manager.PrivilegeManager;
import com.lhh.user.core.manager.RoleManager;
import com.lhh.user.core.manager.UserManager;
import com.lhh.user.core.model.Privilege;
import com.lhh.user.core.model.Role;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.RoleService;

@Component("roleService")
public class RoleServiceImpl extends LhhCoreBaseServiceImpl<Role> implements RoleService {
	
	@Resource
	private RoleManager roleManager;
	@Resource
	private PrivilegeManager privilegeManager;
	@Resource
	private UserManager userManager;
	
	public LhhPage<Role> findPage(Role example, LhhPageParam pageParam) {
		return roleManager.findPage(example, pageParam)	;
	}

	public void savePrivilegeRel(String roleId, String privilegeIds) {
		Role role = super.loadById(roleId);
		List<String> idList = LhhUtilsStr.str2List(privilegeIds);
		if(idList==null || idList.size()==0) {
			role.setPrivileges(null);
		} else {
			List<Privilege> privilList = privilegeManager.findList();
			Map<String, Privilege> map = new HashMap<String, Privilege>();
			if(privilList!=null && privilList.size()>0) {
				for (Privilege privilege : privilList) {
					map.put(privilege.getIdStr(), privilege);
				}
			}
			
			Set<Privilege> privilSet = new HashSet<Privilege>();
			for (String idStr : idList) {
				Privilege privil = map.get(idStr);
				if(privil==null) continue; 
				privilSet.add(privil);
			}
			role.setPrivileges(privilSet);
		}
		roleManager.update(role);
	}
	public void saveUserRel(String roleId, String userIds) {

		Role role = super.loadById(roleId);
		List<String> idList = LhhUtilsStr.str2List(userIds);
		if(idList==null || idList.size()==0) {
			role.setUsers(null);
		} else {
			List<User> userList = userManager.findList();
			Map<String, User> map = new HashMap<String, User>();
			if(userList!=null && userList.size()>0) {
				for (User user : userList) {
					map.put(user.getIdStr(), user);
				}
			}
			
			Set<User> userSet = new HashSet<User>();
			for (String idStr : idList) {
				User user = map.get(idStr);
				if(user==null) continue; 
				userSet.add(user);
			}
			role.setUsers(userSet);
		}
		roleManager.update(role);
	}

	@Override
	protected BaseManager<Role> getBaseManager() {
		// TODO Auto-generated method stub
		return roleManager;
	}
}
