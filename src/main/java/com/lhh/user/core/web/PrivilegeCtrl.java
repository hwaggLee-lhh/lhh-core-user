package com.lhh.user.core.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhh.base.TreeNodeModel;
import com.lhh.core.base.LhhCoreBaseCtrl;
import com.lhh.core.util.LhhCoreUtilsCtrl;
import com.lhh.user.core.model.Privilege;
import com.lhh.user.core.service.PrivilegeService;

/**
 * 权限信息
 * @author hwaggLee
 * @createDate 2016年12月20日
 */
@Controller
@RequestMapping("/privilege")
public class PrivilegeCtrl extends LhhCoreBaseCtrl {
	@Resource
	private PrivilegeService privilegeService;

    @RequestMapping("privilege_tree_list")
    public void treeList(HttpServletRequest req, HttpServletResponse res) {
    	List<Privilege> privilegeList = privilegeService.findList();
    	List<TreeNodeModel> nodeList = new ArrayList<TreeNodeModel>();
    	Map<String, List<TreeNodeModel>> childrenMap = new HashMap<String, List<TreeNodeModel>>();
    	if(privilegeList!=null && privilegeList.size()>0) {
    		for (Privilege privilege : privilegeList) {
    			String category = privilege.getCategory();
    			if(StringUtils.isBlank(category)) continue;
    			List<TreeNodeModel> children = childrenMap.get(category);
    			if(children==null) {
    				children = new ArrayList<TreeNodeModel>();
    				childrenMap.put(category, children);
        			TreeNodeModel treeNode = new TreeNodeModel(category);
        			treeNode.setChildren(children);
    				nodeList.add(treeNode);
    			}
    			TreeNodeModel treeNode = new TreeNodeModel();
    			treeNode.setText(privilege.getPrivilName());
    			if(treeNode.getAttributes()==null) {
    				treeNode.setAttributes(new HashMap<String, String>());
    			}
    			treeNode.getAttributes().put("idStr", privilege.getIdStr());
    			children.add(treeNode);
			}
    	}
    	LhhCoreUtilsCtrl.putJSON(JSONArray.fromObject(nodeList), res);
    }
    
    @RequestMapping("/privilege_list")
    public void list(HttpServletRequest req, HttpServletResponse res){
    	List<Privilege> privilegeList = privilegeService.findList();
    	Map<String,Object> map = new HashMap<String,Object>();
    	for (Privilege privilege : privilegeList) {
    		privilege.setRoleSet(null);
		}
    	map.put("list", privilegeList);
    	LhhCoreUtilsCtrl.putMapToJson(map, res);
    }
}
