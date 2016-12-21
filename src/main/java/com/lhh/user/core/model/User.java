package com.lhh.user.core.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.lhh.core.base.LhhCoreBaseModel;

/**
 * 用户
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Entity
@Table(name="cmndd_user")
public class User extends LhhCoreBaseModel {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
    @Column(name="pk_id")
    private String idStr;
	
    @Column(name="USER_NAME", nullable=false,unique=true)
    private String userName;
    
    @Column(name="pwd", nullable=false)
    private String password;
    
    @Column(name="EMAIL")
    private String email;
    
    @Column(name="real_name")
    private String realName;

    @Column(name="current_login_ip")
    private String currentLoginIp;
    
    @Column(name="last_login_ip")
    private String lastLoginIp;

    @Column(name="current_login_time")
    private Date currentLoginTime;
    
    @Column(name="last_login_time")
    private Date lastLoginTime;
    /**0表示冻结，1表示正常 */
    @Column(name="user_status")
    private String userStatus = "1";

    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="cmndd_join_user_role",
    joinColumns={@JoinColumn(name="fk_user_id")},
    inverseJoinColumns={@JoinColumn(name="fk_role_id")})
    private Set<Role> roleSet;
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	public String getCurrentLoginIp() {
		return currentLoginIp;
	}
	public void setCurrentLoginIp(String currentLoginIp) {
		this.currentLoginIp = currentLoginIp;
	}
	public Date getCurrentLoginTime() {
		return currentLoginTime;
	}
	public void setCurrentLoginTime(Date currentLoginTime) {
		this.currentLoginTime = currentLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}
	
    
}