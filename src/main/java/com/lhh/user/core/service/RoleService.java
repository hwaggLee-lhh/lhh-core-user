package com.lhh.user.core.service;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.BaseService;
import com.lhh.user.core.model.Role;

public interface RoleService extends BaseService<Role> {
	LhhPage<Role> findPage(Role example, LhhPageParam pageParam);
	void savePrivilegeRel(String roleId, String privilegeIds);
	void saveUserRel(String roleId, String userIds);
}
