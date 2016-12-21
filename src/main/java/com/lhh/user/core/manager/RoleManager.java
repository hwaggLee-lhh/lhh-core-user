package com.lhh.user.core.manager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.LhhCoreBaseManager;
import com.lhh.user.core.model.Role;

/**
 * 角色管理
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Repository
public class RoleManager extends LhhCoreBaseManager<Role> {

	@Override
	public Class<Role> getModelClass() {
		return Role.class;
	}
	
	public LhhPage<Role> findPage(Role example, LhhPageParam pageParam) {
    	DetachedCriteria dc = super.getDetachedCriteria();
        if (example!=null && StringUtils.isNotEmpty(example.getRoleName())) {
            dc.add(Restrictions.like("roleName", example.getRoleName(),MatchMode.ANYWHERE));
        }
        dc.addOrder(Order.asc("roleName"));
        return super.findPage(dc, pageParam);
    }
}
