package com.lhh.user.core.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.base.TreeNodeModel;
import com.lhh.core.base.LhhCoreBaseCtrl;
import com.lhh.core.exception.LhhCoreException;
import com.lhh.core.util.LhhCoreUtilsCtrl;
import com.lhh.core.util.LhhCoreUtilsCtrl.EditorUtils;
import com.lhh.format.lang.LhhUtilsStr;
import com.lhh.security.encryption.LhhUtilsPassword;
import com.lhh.user.core.model.Role;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.RoleService;
import com.lhh.user.core.service.UserService;
import com.lhh.user.security.LhhCoreAuthenticationUtil;
import com.lhh.user.security.LhhCoreSecurityCacheManager;

/**
 * 用户信息
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
@Controller
@RequestMapping("/user")
public class UserCtrl extends LhhCoreBaseCtrl {
	@Resource
	private UserService userService;

	@Resource
	private RoleService roleService;
	
	@Resource
	private LhhCoreSecurityCacheManager securityCacheManager;
	
	@Resource
	private LhhCoreAuthenticationUtil authenticationUtil;

    @RequestMapping("list_view")
    public ModelAndView list_view(HttpServletRequest req, HttpServletResponse res) {
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView(PAGE_PATH, "user");
    }

    @RequestMapping("list_do_page")
    public void list_do_page(HttpServletRequest req, HttpServletResponse res, User example) {
        LhhPageParam pageParam = LhhCoreUtilsCtrl.getPageParamZore(req);
        
        LhhPage<User> page = userService.findPage(example, pageParam);
//        List userList = page.getDatas();
//        为了产生json不出错 凡是有双向关联都要去掉关联
//        for (Iterator iter = userList.iterator(); iter.hasNext();) {
//            User element = (User) iter.next();
//        }
        //CtrlUtils.putJSONList(super.jsonConvert, userList, null, res);
        LhhCoreUtilsCtrl.CorePageToJson.putJSONPage(jsonConvert, page, null, res);
    }
    @RequestMapping("save_do")
    public void save_do(HttpServletRequest req, HttpServletResponse res) {
         String msg = "新增成功";
         boolean isSuccess = false;
         try {
        	 User user = new User();
        	 LhhCoreUtilsCtrl.EditorUtils.convertObj(req, user);
         	 user.setPassword(LhhUtilsPassword.hash(user.getPassword()));
        	 userService.save(user);
        	 securityCacheManager.reInit();
        	 isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
        LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
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
       	 	securityCacheManager.reInit();
       	 	isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }

    @RequestMapping("delete_do")
    public void delete_do(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "删除成功";
        boolean isSuccess = false;
        try {
        	String ids = req.getParameter("ids");
        	List<String> idList = LhhUtilsStr.str2List(ids); 
            User user = authenticationUtil.getCurrentUserPO();
        	for (String string : idList) {
        		if("1".equals(string)) throw new LhhCoreException("系统管理员不能删除");
				if(string.equals(user.getIdStr())) throw new LhhCoreException("不能删除自身账号");
			}
       	 	userService.deleteByIdList(idList);
       	 	securityCacheManager.reInit();
       	 	isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("reset_user")
    public void reset_user(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "解冻成功";
        boolean isSuccess = false;
        try {
        	String userId = req.getParameter("userId");
        	User user = userService.findById(userId);
        	user.setUserStatus("1");
       	 	userService.update(user);
       	    securityCacheManager.reInit();
       	 	isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("freeze_user")
    public void freeze_user(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "冻结成功";
        boolean isSuccess = false;
        try {
        	String userId = req.getParameter("userId");
            User currentUser = authenticationUtil.getCurrentUserPO();
        	if(userId.equals(currentUser.getIdStr())) throw new LhhCoreException("不能冻结自身账号");
        	User user = userService.findById(userId);
        	user.setUserStatus("0");
       	 	userService.update(user);
       	 	securityCacheManager.reInit();
       	 	isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("reset_pwd")
    public void reset_pwd(HttpServletRequest req, HttpServletResponse res) {
        String msg = "重置成功";
        boolean isSuccess = false;
        try {
        	String userId = req.getParameter("idStr");
            String pwd1 = req.getParameter("pwd1");
            String pwd2 = req.getParameter("pwd2");
            if (StringUtils.isEmpty(pwd1) || StringUtils.isEmpty(pwd2)) {
                isSuccess = false;
                msg = "密码不能为空";
            } else if(!pwd1.equals(pwd2)) {

                isSuccess = false;
                msg = "两次输入的密码不一致";
            } else {
            	User user = userService.findById(userId);
            	user.setPassword(LhhUtilsPassword.hash(pwd1));
           	 	userService.update(user);
           	 	securityCacheManager.reInit();
           	 	isSuccess = true;
           	    req.getSession().setAttribute("isFirstLogin", false);
            }
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
       LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("get_role_id_list")
    public void getRoleIdList(HttpServletRequest req, HttpServletResponse res) {

    	String userId = req.getParameter("userId");
    	User user = userService.loadById(userId);
    	List<String> roleIdList = new ArrayList<String>();
    	if(user!=null) {
    		Set<Role> roleSet = user.getRoleSet();
    		for (Role role : roleSet) {
    			roleIdList.add(role.getIdStr());
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(roleIdList), res);
    }
    @RequestMapping("save_role_rel")
    public void saveRoleRel(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "保存成功";
        boolean isSuccess = false;
    	String userId = req.getParameter("userId");
    	String roleIds = req.getParameter("roleIds");
    	try {
    		userService.saveRoleRel(userId, roleIds);
    		securityCacheManager.reInit();
    		isSuccess = true;
		} catch (LhhCoreException e) {
			msg = e.getMessage();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg = e.getMessage();
		}
        LhhCoreUtilsCtrl.CoreObjectToJson.putJSONResult(isSuccess, msg, res);
    }
    @RequestMapping("user_tree_list")
    public void treeList(HttpServletRequest req, HttpServletResponse res) {
    	List<User> userList = userService.findList();
    	List<TreeNodeModel> nodeList = new ArrayList<TreeNodeModel>();
    	if(userList!=null && userList.size()>0) {
    		for (User user : userList) {
    			TreeNodeModel treeNode = new TreeNodeModel();
    			treeNode.setText(user.getUserName());
    			if(treeNode.getAttributes()==null) {
    				treeNode.setAttributes(new HashMap<String, String>());
    			}
    			treeNode.getAttributes().put("idStr", user.getIdStr());
    			nodeList.add(treeNode);
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(nodeList), res);
    }
}