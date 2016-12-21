package com.lhh.user.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lhh.core.base.LhhCoreBaseCtrl;
import com.lhh.core.util.LhhCoreUtilsCtrl;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;
import com.lhh.user.security.LhhCoreAuthenticationUtil;

/**
 * 主页面
 * @author hwaggLee
 * @createDate 2016年12月19日
 */
@Controller
@RequestMapping("/main")
public class MainCtrl extends LhhCoreBaseCtrl {

	@Resource
    private LhhCoreAuthenticationUtil authenticationUtil;
	@Resource
	private UserService userService;

    /**
     * 登录成功后跳转页面
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("view")
	public ModelAndView view(HttpServletRequest req, HttpServletResponse res) {
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "main");
        
    }
    
    /**
     * 安全退出跳转页面
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("noresetpwd")
    public ModelAndView noresetpwd(HttpServletRequest req, HttpServletResponse res) {
    	User user =  authenticationUtil.getCurrentUserPO();
    	user.setLastLoginTime(null);
    	user.setLastLoginIp("");
    	userService.update(user);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("REQUEST_MESSAGE", "已安全退出");
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "login" ,map);
    }

    /**
     * 安全退出跳转页面
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse res) {
    	User user =  authenticationUtil.getCurrentUserPO();
    	user.setLastLoginTime(null);
    	user.setLastLoginIp("");
    	userService.update(user);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("REQUEST_MESSAGE", "已安全退出");
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "login" ,map);
    }
    
    /**
     * 跳转登录页面（检查权限）
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("login")
    public ModelAndView gotoLoginPage(HttpServletRequest req, HttpServletResponse res) {
    	Object o = req.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    	Map<String, Object> map = new HashMap<String, Object>();
    	if(o instanceof AuthenticationException) {
    		AuthenticationException e = (AuthenticationException)o;
        	map.put("REQUEST_MESSAGE", e.getMessage());
    	}
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "login" ,map);
    }
    /**
     * session过期调整页面
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("session_expired")
    public ModelAndView session_expired(HttpServletRequest req, HttpServletResponse res) {
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "session-expired" ,null);
    }
    /**
     * 超时跳转页面
     * @param req
     * @param res
     * @return
     */
    @RequestMapping("time_out")
    public ModelAndView time_out(HttpServletRequest req, HttpServletResponse res) {
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView("", "login" ,null);
    }
}
