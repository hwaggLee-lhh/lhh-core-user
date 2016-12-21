package com.lhh.user.core.service;

import java.util.List;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.BaseService;
import com.lhh.user.core.model.User;

public interface UserService extends BaseService<User> {
	LhhPage<User> findPage(User example, LhhPageParam pageParam);
	User findByUserName(String userName);
	void saveRoleRel(String userIdStr, String roleIds);
	List<User> findUserList(String enterName);
}
