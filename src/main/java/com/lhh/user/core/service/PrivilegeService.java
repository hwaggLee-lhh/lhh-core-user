package com.lhh.user.core.service;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.BaseService;
import com.lhh.user.core.model.Privilege;

public interface PrivilegeService extends BaseService<Privilege> {

	LhhPage<Privilege> findPage(Privilege example, LhhPageParam pageParam);
}
