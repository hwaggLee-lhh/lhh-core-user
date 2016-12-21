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
 * 权限
 * @author hwaggLee
 * @createDate 2016年12月16日
 */
@Entity
@Table(name="cmndd_privilege")
public class Privilege extends LhhCoreBaseModel {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator="paymentableGenerator")
    @GenericGenerator(name="paymentableGenerator",strategy="uuid")
	@Column(name="privil_id")
    private String idStr;
    
    @Column(name="privil_name", nullable=false,unique=true)
    private String privilName;
    
	@Column(name="category", nullable=false)
    private String category;
	
	@Column(name="pattern", nullable=false,unique=true)
    private String pattern;
    
	@Column(name="type", nullable=false)
    private String type;
    
    @Cascade(CascadeType.SAVE_UPDATE)
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="cmndd_join_role_privilege",
    joinColumns={@JoinColumn(name="fk_privil_id")},
    inverseJoinColumns={@JoinColumn(name="fk_role_id")})
    private Set<Role> roleSet;
    
    public String getPattern() {
        return pattern;
    }
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    public String getIdStr() {
        return idStr;
    }
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
    public String getPrivilName() {
        return privilName;
    }
    public void setPrivilName(String privilName) {
        this.privilName = privilName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}
    
}