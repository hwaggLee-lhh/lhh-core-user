package com.lhh.user.core.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lhh.core.base.LhhCoreBaseCtrl;
import com.lhh.core.exception.LhhCoreException;
import com.lhh.core.util.LhhCoreUtilsCtrl;
import com.lhh.core.util.LhhCoreUtilsCtrl.EditorUtils;
import com.lhh.security.encryption.LhhUtilsPassword;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;
import com.lhh.user.security.LhhCoreAuthenticationUtil;
import com.lhh.user.security.LhhCoreSecurityCacheManager;

/**
 * 商户信息（用户的另一个纬度）
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
@Controller
@RequestMapping("/account")
public class AccountCtrl extends LhhCoreBaseCtrl {

	@Resource
	private UserService userService;
	@Resource
    private LhhCoreAuthenticationUtil authenticationUtil;
	@Resource
	private LhhCoreSecurityCacheManager securityCacheManager;
	
    public static final Logger log = Logger.getLogger(AccountCtrl.class);

    @RequestMapping("list_view")
    public ModelAndView list_view(HttpServletRequest req, HttpServletResponse res) {
    	User user = authenticationUtil.getCurrentUserPO();
    	if(user==null) {
            return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView(PAGE_PATH, "account_nonuser");
    	} else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", user);
            return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView(PAGE_PATH, "account" ,map);
    	}
    }

    @RequestMapping("update_do")
    public void update_do(HttpServletRequest req, HttpServletResponse res) {
        String msg = "修改成功";
        boolean isSuccess = false;
        try {
       	 	User user = new User();
            EditorUtils.convertObj(req, user);
        	if( StringUtils.isBlank(user.getIdStr())) {
        		throw new LhhCoreException("用户不存在");
        	}
       	 	userService.updateByParam(user);
       	 	isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("reset_pwd")
    public void resetPwd(HttpServletRequest req, HttpServletResponse res) {
        String msg = "修改成功";
        boolean isSuccess = false;
    	String userId = req.getParameter("idStr");
    	String currentPwd = req.getParameter("currentPwd");
        String pwd1 = req.getParameter("newPwd1");
        String pwd2 = req.getParameter("newPwd2");
    	try {
        	if( StringUtils.isBlank(userId)) {
        		msg = "用户Id不能为空";
        	} else if (StringUtils.isEmpty(pwd1) || StringUtils.isEmpty(pwd2)) {
                msg = "密码不能为空";
            } else if(!pwd1.equals(pwd2)) {
                msg = "两次输入的密码不一致";
            } else if(currentPwd.equals(pwd1)) {
            	msg = "新密码不能与当前密码一致";
            }else {
            	User user = userService.findById(userId);
            	currentPwd = LhhUtilsPassword.hash(currentPwd);
            	if(currentPwd.equals(user.getPassword())) {
            		user.setPassword(LhhUtilsPassword.hash(pwd1));
               	 	userService.update(user);
               	 	securityCacheManager.reInit();
               	 	req.getSession().setAttribute("isFirstLogin", false);
               	 	isSuccess = true;
            	} else {
            		msg = "当前密码有误，修改失败";
            	}
            }
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			log.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
}
