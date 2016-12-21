package com.lhh.user.core.model;

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
 * 角色
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Entity
@Table(name="cmndd_roles")
public class Role extends LhhCoreBaseModel {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
	@Column(name="role_id")
    private String idStr; 
	
    @Column(name="role_name", nullable=false,unique=true)
    private String roleName; 
    
    @Column(name="description")
    private String description; 
    
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="cmndd_join_user_role",
    joinColumns={@JoinColumn(name="fk_role_id")},
    inverseJoinColumns={@JoinColumn(name="fk_user_id")})
    private Set<User> users;
    
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="cmndd_join_role_privilege",
    joinColumns={@JoinColumn(name="fk_role_id")},
    inverseJoinColumns={@JoinColumn(name="fk_privil_id")})
    private Set<Privilege> privileges;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}
    
}
