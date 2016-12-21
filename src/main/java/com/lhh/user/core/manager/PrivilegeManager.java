package com.lhh.user.core.manager;

import org.springframework.stereotype.Repository;

import com.lhh.core.base.LhhCoreBaseManager;
import com.lhh.user.core.model.Privilege;
/**
 * 权限管理类
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Repository
public class PrivilegeManager extends LhhCoreBaseManager<Privilege> {

	@Override
	public Class<Privilege> getModelClass() {
		return Privilege.class;
	}

}
