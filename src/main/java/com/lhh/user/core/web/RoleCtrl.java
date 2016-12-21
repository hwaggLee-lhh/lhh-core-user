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
import com.lhh.user.core.model.Privilege;
import com.lhh.user.core.model.Role;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.RoleService;
import com.lhh.user.security.LhhCoreSecurityCacheManager;

/**
 * 角色信息
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
@Controller
@RequestMapping("/role")
public class RoleCtrl extends LhhCoreBaseCtrl {
	@Resource
	private RoleService roleService;
	@Resource
	private LhhCoreSecurityCacheManager securityCacheManager;
	
    @RequestMapping("list_view")
    public ModelAndView list_view(HttpServletRequest req, HttpServletResponse res) {
        return LhhCoreUtilsCtrl.CoreModelAndView.getModelAndView(PAGE_PATH, "role" ,null);
    }
    @RequestMapping("list_do_page")
    public void list_do_page(HttpServletRequest req, HttpServletResponse res, Role example) {
        LhhPageParam pageParam = LhhCoreUtilsCtrl.getPageParamZore(req);
        
        LhhPage<Role> page = roleService.findPage(example, pageParam);
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
        	 Role role = new Role();
             EditorUtils.convertObj(req, role);
             roleService.save(role);
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
       	 	Role role = new Role();
            EditorUtils.convertObj(req, role);
        	if( StringUtils.isBlank(role.getIdStr())) {
        		throw new LhhCoreException("用户不存在");
        	}
        	roleService.updateByParam(role);
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
        	roleService.deleteByIdList(idList);
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

    @RequestMapping("role_tree_list")
    public void roleTreeList(HttpServletRequest req, HttpServletResponse res) {
    	List<Role> roleList = roleService.findList();
    	List<TreeNodeModel> nodeList = new ArrayList<TreeNodeModel>();
    	if(roleList!=null && roleList.size()>0) {
    		for (Role role : roleList) {
    			TreeNodeModel treeNode = new TreeNodeModel();
    			treeNode.setText(role.getRoleName());
    			if(treeNode.getAttributes()==null) {
    				treeNode.setAttributes(new HashMap<String, String>());
    			}
    			treeNode.getAttributes().put("idStr", role.getIdStr());
    			nodeList.add(treeNode);
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(nodeList), res);
    }
    @RequestMapping("get_privilege_id_list")
    public void getPrivilegeIdList(HttpServletRequest req, HttpServletResponse res) {

    	String roleId = req.getParameter("roleId");
    	Role role = roleService.loadById(roleId);
    	List<String> privilegeIdList = new ArrayList<String>();
    	if(role!=null) {
    		Set<Privilege> roleSet = role.getPrivileges();
    		for (Privilege priv : roleSet) {
    			privilegeIdList.add(priv.getIdStr());
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(privilegeIdList), res);
    }
    @RequestMapping("save_privilege_rel")
    public void savePrivilegeRel(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "保存成功";
        boolean isSuccess = false;
    	String roleId = req.getParameter("roleId");
    	String privilegeIds = req.getParameter("privilegeIds");
    	try {
    		roleService.savePrivilegeRel(roleId, privilegeIds);
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

    @RequestMapping("get_user_id_list")
    public void getUserIdList(HttpServletRequest req, HttpServletResponse res) {

    	String roleId = req.getParameter("roleId");
    	Role role = roleService.loadById(roleId);
    	List<String> idList = new ArrayList<String>();
    	if(role!=null) {
    		Set<User> roleSet = role.getUsers();
    		for (User user : roleSet) {
    			idList.add(user.getIdStr());
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(idList), res);
    }
    @RequestMapping("save_user_rel")
    public void saveUserRel(HttpServletRequest req, HttpServletResponse res) {
    	String msg = "保存成功";
        boolean isSuccess = false;
    	String roleId = req.getParameter("roleId");
    	String userIds = req.getParameter("userIds");
    	try {
    		roleService.saveUserRel(roleId, userIds);
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
}
