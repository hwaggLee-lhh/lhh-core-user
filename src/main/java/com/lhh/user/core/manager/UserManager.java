package com.lhh.user.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.base.LhhCoreBaseManager;
import com.lhh.core.exception.LhhCoreException;
import com.lhh.user.core.model.User;

/**
 * 用户管理
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Repository
public class UserManager extends LhhCoreBaseManager<User> {

    public Class<User> getModelClass() {
        return User.class;
    }
    public User save(User user) {
		List<String> uniquePropertyNames = new ArrayList<String>();
		uniquePropertyNames.add("userName");
    	super.isUnique(true, user, uniquePropertyNames, new LhhCoreException("具有相同用户名的用户已经存在"));
    	super.save(user);
    	return user;
    }
    public User findByEmail(String email) {
        User example = new User();
        example.setEmail(email);
        DetachedCriteria dc = super.getDetachedCriteria(); 
		dc.add(Restrictions.eq("email", example.getEmail()));
        return (User) super.findUniqueResult(dc);
    }

    public User findByUserName(String userName) {
        User example = new User();//
        example.setUserName(userName);
        DetachedCriteria dc = super.getDetachedCriteria(); 
		dc.add(Restrictions.eq("userName", example.getUserName()));
        return (User) super.findUniqueResult(dc);
    }

    public List<User> findList() {
        return super.findList("userName");
    }

    public List<User> findList(User example) {
        return super.findList(getFindListCriteria(example));
    }

    public DetachedCriteria getFindListCriteria(User example) {
        DetachedCriteria dc = super.getDetachedCriteria();
        if (StringUtils.isNotEmpty(example.getUserName())) {
            dc.add(Restrictions.like("userName", example.getUserName(),MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(example.getEmail())) {
            dc.add(Restrictions.like("email", example.getEmail(),MatchMode.ANYWHERE));
        }
        if (StringUtils.isNotEmpty(example.getRealName())) {
            dc.add(Restrictions.like("realName", example.getRealName(),MatchMode.ANYWHERE));
        }
        dc.addOrder(Order.asc("userName"));
        return dc;
    }
    
    public User findById(String id) {
        return (User) super.findById(id);
    }
    
    public User findByUniqueUserName(String userName) {
        DetachedCriteria dc = super.getDetachedCriteria();
        dc.add(Restrictions.eq("userName", userName));
        
        List<User> list = super.findList(dc);
        if (list.size() == 0) {
            return null;
        }
        return (User)list.get(0);
    }

    public User findByUniqueEmail(String email) {
        DetachedCriteria dc = super.getDetachedCriteria();
        dc.add(Restrictions.eq("email", email));
        
        List<User> list = super.findList(dc);
        if (list.size() == 0) {
            return null;
        }
        
        return (User)list.get(0);
    }
    public LhhPage<User> findPage(User example, LhhPageParam pageParam) {
    	DetachedCriteria dc = super.getDetachedCriteria();
        if (example!=null && StringUtils.isNotEmpty(example.getUserName())) {
            dc.add(Restrictions.like("userName", example.getUserName(),MatchMode.ANYWHERE));
        }
        if (example!=null && StringUtils.isNotEmpty(example.getEmail())) {
            dc.add(Restrictions.like("email", example.getEmail(),MatchMode.ANYWHERE));
        }
        if (example!=null && StringUtils.isNotEmpty(example.getRealName())) {
            dc.add(Restrictions.like("realName", example.getRealName(),MatchMode.ANYWHERE));
        }
        if (example!=null && StringUtils.isNotEmpty(example.getUserStatus())) {
            dc.add(Restrictions.like("userStatus", example.getUserStatus(),MatchMode.ANYWHERE));
        }
        dc.addOrder(Order.asc("userName"));
        return super.findPage(dc, pageParam);
    }
    
    public void updateConfig(User user){
    	super.update(user);
    }
    
    /**
     * 
     * 查询账号
     */
    public List<User> findUserList(String enterName){
		DetachedCriteria dc = super.getDetachedCriteria();
		if(!StringUtils.isBlank(enterName)){
			dc.add(Restrictions.like("userName", enterName,MatchMode.ANYWHERE));
		}
		return super.findList(dc);
	}
}