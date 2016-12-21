package com.lhh.user.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.lhh.user.LhhUser;
import com.lhh.user.core.model.User;

/**
 * session存储
 * @author hwaggLee
 * @createDate 2016年12月2日
 */
public class LhhCoreUtilsSession {
	public static final String sessionUserid = LhhUser.sessionUserid;
	public static final String sessionUserName = LhhUser.sessionUsername;
	public static final String sessionUser = LhhUser.sessionUser;
	
	/**
	 * 使用指定的session名称将用户信息存入session
	 * @param session:存入的session
	 * @param user：用户信息
	 * @param isall：true=将用户所有的信息存入session，false：只存入userid，username
	 */
	public static void addUserSession(HttpSession session,User user,boolean isall){
		if( session == null  )return;
		if( user == null || StringUtils.isBlank(user.getIdStr()) )return ;
		session.setAttribute(sessionUserid, user.getIdStr());
		if( StringUtils.isNotBlank(user.getUserName())){
			session.setAttribute(sessionUserName, user.getUserName());
		}
		
		if(isall){
			session.setAttribute(sessionUser, user);
		}
	}
	
	
	/**
	 * 单独存储session（用户信息建议使用addUserSession，有系统设定默认用户session的key，这样能一一对应）
	 * @param session:存入的session
	 * @param key
	 * @param value
	 */
	public static void addSession(HttpSession session,String key,Object value){
		session.setAttribute(key, value);
	}
	
	/**
	 * 存容器内容
	 * @param sc
	 * @param key
	 * @param value
	 */
	public static void addServletContext(ServletContext sc,String key,Object value){
		sc.setAttribute(key, value);
	}
	
	/**
	 * 读取当前session登陆用户的id
	 * @param session
	 * @return
	 */
	public static String getUserId(HttpSession session){
		Object ob = session.getAttribute(sessionUserid);
		if( ob == null ) return null;
		return ob.toString();
	}
	
}
