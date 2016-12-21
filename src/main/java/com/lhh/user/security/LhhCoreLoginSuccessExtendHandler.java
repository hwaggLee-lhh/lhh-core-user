package com.lhh.user.security;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.lhh.connection.url.LhhUtilsLocalIp;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;
import com.lhh.user.util.LhhCoreUtilsSession;
/**
 * 继承SavedRequestAwareAuthenticationSuccessHandler
 * 在认证成功之前添加逻辑
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
public class LhhCoreLoginSuccessExtendHandler extends SavedRequestAwareAuthenticationSuccessHandler{
    /**日志对象*/
    private final Logger loggle = Logger.getLogger(getClass());
	@Resource
	private UserService userService;
	
	/**
	 * 重新登录验证成功方法，需要在登录成功是做特性处理
	 * @param request
	 * @param response
	 * @param authentication
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		saveLoginInfo(request, authentication);
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	/**
	 * 登录验证成功后更新用户登录最新时间等信息
	 * @param request
	 * @param authentication
	 */
	protected void saveLoginInfo(HttpServletRequest request, Authentication authentication) {
		String userName = authentication.getName();
		User user = userService.findByUserName(userName);
		request.getSession().setAttribute("isFirstLogin", StringUtils.isBlank(user.getLastLoginIp()));
    	user.setLastLoginTime(user.getCurrentLoginTime());
    	user.setLastLoginIp(user.getCurrentLoginIp());
    	user.setCurrentLoginTime(new Date());
    	user.setCurrentLoginIp(LhhUtilsLocalIp.getRemoteHost(request));
		loggle.info("lhh-core user :"+user.getUserName()+" is logged on. ip:"+user.getCurrentLoginIp());
    	userService.update(user);
    	LhhCoreUtilsSession.addUserSession(request.getSession(), user, true);
	}
}
