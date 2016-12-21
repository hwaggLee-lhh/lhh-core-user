package com.lhh.core.core;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.lhh.base.LhhPage;
import com.lhh.base.LhhPageParam;
import com.lhh.core.BaseServiceTest;
import com.lhh.core.exception.LhhCoreException;
import com.lhh.user.core.model.User;
import com.lhh.user.core.service.UserService;

public class TestUser extends BaseServiceTest {
    /**日志对象*/
    private final Logger log = Logger.getLogger(getClass());
	@Resource
	private UserService userService;
	@Test
	public void test() {
		
	}
//	@Test
	public void testGetUserPage() {
		LhhPageParam pageParam = new LhhPageParam();
		pageParam.setStart(20);
		pageParam.setLimit(20);
		try {
			LhhPage<User> userPage = userService.findPage(null, pageParam);
			if(userPage!=null) {
				List<User> userList = userPage.getDatas();
				if(userList==null || userList.size()==0) {
					System.out.println("null"+userList);
				} else {
					for (User user : userList) {
						System.out.println(user.getUserName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("OK");
	}
//	@Test
	public void testGetUserList() {
		try {
			List<User> userList = userService.findList(); 
			if(userList==null || userList.size()==0) {
				System.out.println("null"+userList);
				return;
			} else {
				for (User user : userList) {
					System.out.println(user.getUserName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
	public void testGetUserById() {
		try {

			String userId = "928182f4384bdac201384bdde93a0001";
			User user = userService.findById(userId);
			if(user!=null) System.out.println(user.getUserName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
//	@Test
	public void testSaveUser() {
		try {
			User user = new User();
			user.setUserName("testabc");
			user.setPassword("aaaa");
			userService.save(user);
		} catch (LhhCoreException e) {
			// TODO: handle exception
			log.error(e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
