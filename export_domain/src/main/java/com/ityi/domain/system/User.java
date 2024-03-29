package com.ityi.domain.system;

import java.io.Serializable;

public class User implements Serializable {
    /** 
     */ 
    private String id;

    /** 
     */ 
    private String deptId;
	
	/** 
     */ 
    private String deptName;

    /** 
     */ 
    private String email;

    /** 
     * 用户名
     */ 
    private String userName;

    /** 
     * shiro MD5密码32位
     */ 
    private String password;

    /** 
     * 1启用0停用
     */ 
    private Integer state;


    /** 
     */ 
    private String managerId;

    /** 
     */ 
    private String joinDate;

    /** 
     */ 
    private Double salary;

    /** 
     */ 
    private String birthday;

    /** 
     */ 
    private String gender;

    /** 
     */ 
    private String station;

    /** 
     */ 
    private String telephone;

    /**
     * 0作为内部控制，租户企业不能使用
     *      0-saas管理员
     *      1-企业管理员
     *      2-管理所有下属部门和人员
     *      3-管理本部门
     *      4-普通员工
     */ 
    private Integer degree;

    /** 
     */ 
    private String remark;

    /** 
     */ 
    private Integer orderNo;

    private String companyId;
    private String companyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}