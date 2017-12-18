package com.se.controller;


import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.se.bean.Login;
import com.se.bean.Register;
import com.se.dao.LoginDao;
import com.se.dao.RegistrationDao;

@ManagedBean(name="loginController",eager=true)
@RequestScoped
public class LoginController {

	@ManagedProperty("#{loginDao}")
	private LoginDao loginDao;
	
	
	/**
	 * @return the loginDao
	 */
	public LoginDao getLoginDao() {
		return loginDao;
	}



	/**
	 * @param loginDao the loginDao to set
	 */
	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}



	
	public static boolean validateLogin(Login user) {
		
		boolean isValid = LoginDao.validateLgin(user);
		
		
		return isValid;
	}
	
	
	
	public Register getUserDetails(String usesrName) {
		
		Register user = loginDao.getUserDetails(usesrName);
		return  user;
	}
	
	
	public boolean approveManager(int selectedManagerId) {
		boolean isApproved = loginDao.approveManager(selectedManagerId);
		return isApproved;
	}
	
	
	
public List<SelectItem> getAvailableManagers() {
		
		List<SelectItem> availableManagers = loginDao.getAvailableManagers();
			
			return availableManagers;
		}

public List<SelectItem> getApprovedManagers() {
	
	List<SelectItem> availableManagers = loginDao.getApprovedManagers();
		
		return availableManagers;
	}


public List<Register> getManagerProfile(int selectedManagerId) {
	List<Register> managerList  = loginDao.getManagerProfile(selectedManagerId);
	return managerList;
}

public boolean selectManager(int selectedManagerId) {
	boolean isSelected  = loginDao.selectManager(selectedManagerId);
	return isSelected;
}

	
}
