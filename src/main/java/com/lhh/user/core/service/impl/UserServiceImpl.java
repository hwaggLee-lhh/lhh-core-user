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
import com.lhh.core.exception.LhhCoreException;
import com.lhh.format.lang.LhhUtilsStr;
import com.lhh.user.core.manager.RoleManager;
import com.lhh.user.core.manager.UserManager;
import com.lhh.user.core.model.Role;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;

@Component("userService")
public class UserServiceImpl extends LhhCoreBaseServiceImpl<User> implements UserService {
	@Resource
	private UserManager userManager;
	@Resource
	private RoleManager roleManager;
	public User findByUserName(String userName) {
		return userManager.findByUserName(userName);
	}
	public LhhPage<User> findPage(User example, LhhPageParam pageParam) {
		return userManager.findPage(example, pageParam);
	}
	public void saveRoleRel(String userIdStr, String roleIds) {
		User user = this.loadById(userIdStr);
		boolean hasSuperRole = false;
		List<String> roleIdList = LhhUtilsStr.str2List(roleIds);
		if(roleIdList==null || roleIdList.size()==0) {
			user.setRoleSet(null);
		} else {
			List<Role> roleList = roleManager.findList();
			Map<String, Role> roleMap = new HashMap<String, Role>();
			if(roleList!=null && roleList.size()>0) {
				for (Role role : roleList) {
					roleMap.put(role.getIdStr(), role);
				}
			}
			Set<Role> roleSet = new HashSet<Role>();
			for (String roleIdStr : roleIdList) {
				Role role = roleMap.get(roleIdStr);
				if(role==null) continue;
				if("1".equals(roleIdStr)) hasSuperRole = true;
				roleSet.add(role);
			}
			if(!hasSuperRole && "1".equals(userIdStr)) {
				throw new LhhCoreException("超级用户必须选择系统管理员角色。");
			}
			user.setRoleSet(roleSet);
		}
		userManager.update(user);
	}
	@Override
	protected BaseManager<User> getBaseManager() {
		// TODO Auto-generated method stub
		return userManager;
	}
	public List<User> findUserList(String enterName) {
		return userManager.findUserList(enterName);
	}
}
