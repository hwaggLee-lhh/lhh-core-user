package com.lhh.user.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.lhh.user.core.service.AuthenticationService;


/**
 * 通过数据库方式获取User 和 Resource 实例
 * @author hwaggLee
 * @createDate 2016年12月15日
 */
public class AuthenticationServiceImpl extends JdbcDaoSupport implements AuthenticationService{
	
	//获取用户名，密码，状态(name,passwd,status)
	private String loadUsersQuery;
	
	//获取相应用户名下的所有资源(role.name)
	private String authoritiesByUsernameQuery;
	
	//获取所有资源的资源串和资源类型(res_string, res_type)
	private String loadResourcesQuery;
	
	//获取相应资源下的所有权限(role.name)
	private String authoritiesByResourceQuery;
	
	private String rolePrefix = "";


	/* 
	 * 获取所有资源实例
	 * (non-Javadoc)
	 * @see org.springside.components.acegi.service.AuthenticationService#getResources()
	 */
	public List<MyResource> getResources() {
		List<MyResource> resources = new LoadResourcesMapping(getDataSource()).execute();
		List<MyResource> authResources = new ArrayList<MyResource>();
		for (MyResource resc : resources) {
			List<String> auths = new AuthoritiesByResourcMapping(getDataSource()).execute(resc.getResString());
			List<ConfigAttribute> configAttributeList = new ArrayList<ConfigAttribute>();
			if(auths!=null && auths.size()>0) {
				for (int i = 0; i < auths.size(); i++) {
					String auth = (String)auths.get(i);
					configAttributeList.add(new SecurityConfig(auth));
				}
			} 
			authResources.add(new MyResource(resc.getResString(), resc.getResType(), configAttributeList));
		}
		return authResources;
	}

	/* 
	 * 获取所有用户实例
	 * (non-Javadoc)
	 * @see org.springside.components.acegi.service.AuthenticationService#getUsers()
	 */
	public List<User> getUsers() {
		List<UserDetails> users = new LoadUsersMapping(getDataSource()).execute();
		List<User> authUsers = new ArrayList<User>();
		for (UserDetails user : users) {
			List<GrantedAuthority> auths = new AuthoritiesByUsernameMapping(getDataSource()).execute(user.getUsername());
			
			authUsers.add(new MyUser(
					user.getUsername(), 
					user.getPassword(), 
					user.isEnabled(), 
					true, 
					true, 
					true, 
					auths));
		}
		return authUsers;
	}
	private class MyUser extends User {
		private static final long serialVersionUID = 1L;
		private MyUser(String username, String password, boolean enabled, boolean accountNonExpired,
	            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
			super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		}
		//重载消除凭证方法，空实现
		public void eraseCredentials() {
			
		}
	}
	
	/**
     * Query to look up users.
     */
    protected class LoadUsersMapping extends MappingSqlQuery<UserDetails> {
        protected LoadUsersMapping(DataSource ds) {
            super(ds, loadUsersQuery);
            compile();
        }

        protected UserDetails mapRow(ResultSet rs, int rownum)
            throws SQLException {
            String username = rs.getString(1);
            String password = rs.getString(2);
            boolean enabled = rs.getBoolean(3);
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("HOLDER"));
            UserDetails user = new User(username, password, enabled, true, true, true,authorities);
            return user;
        }
    }
    
    /**
     * Query object to look up a user's authorities.
     */
    protected class AuthoritiesByUsernameMapping extends MappingSqlQuery<GrantedAuthority> {
        protected AuthoritiesByUsernameMapping(DataSource ds) {
            super(ds, authoritiesByUsernameQuery);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        protected GrantedAuthority mapRow(ResultSet rs, int rownum)
            throws SQLException {
            String roleName = rolePrefix + rs.getString(1);
            GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
            return authority;
        }
    }
	
    /**
     * Query to look up resources.
     */
    protected class LoadResourcesMapping extends MappingSqlQuery<MyResource> {
        protected LoadResourcesMapping(DataSource ds) {
            super(ds, loadResourcesQuery);
            compile();
        }

        protected MyResource mapRow(ResultSet rs, int rownum)
            throws SQLException {
            String resString = rs.getString(1);
            String resType = rs.getString(2);
            MyResource resource = new MyResource(resString, resType, null);
            return resource;
        }
    }
    
    /**
     * Query object to look up a resource's authorities.
     */
    protected class AuthoritiesByResourcMapping extends MappingSqlQuery<String> {
        protected AuthoritiesByResourcMapping(DataSource ds) {
            super(ds, authoritiesByResourceQuery);
            declareParameter(new SqlParameter(Types.VARCHAR));
            compile();
        }

        protected String mapRow(ResultSet rs, int rownum)
            throws SQLException {
            String roleName = rolePrefix + rs.getString(1);
            return roleName;
        }
    }

	public void setLoadUsersQuery(String loadUsersQuery) {
		this.loadUsersQuery = loadUsersQuery;
	}

	public void setAuthoritiesByUsernameQuery(String authoritiesByUsernameQuery) {
		this.authoritiesByUsernameQuery = authoritiesByUsernameQuery;
	}
	
	public void setAuthoritiesByResourceQuery(String authoritiesByResourceQuery) {
		this.authoritiesByResourceQuery = authoritiesByResourceQuery;
	}

	public void setLoadResourcesQuery(String loadResourcesQuery) {
		this.loadResourcesQuery = loadResourcesQuery;
	}

	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}
	
}
