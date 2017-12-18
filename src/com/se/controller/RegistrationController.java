package com.se.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.se.bean.Register;
import com.se.dao.RegistrationDao;


@ManagedBean(name="registrationController",eager=true)
@RequestScoped
public class RegistrationController {

	@ManagedProperty("#{registrationDao}")
	private RegistrationDao registrationDao;
	
	
	
	/**
	 * @return the registrationDao
	 */
	public RegistrationDao getRegistrationDao() {
		return registrationDao;
	}

	/**
	 * @param registrationDao the registrationDao to set
	 */
	public void setRegistrationDao(RegistrationDao registrationDao) {
		this.registrationDao = registrationDao;
	}

	
	
	public static boolean newUSerRegistration(Register user) {
		
		
		System.out.println(user);
		
		boolean isRegistered = RegistrationDao.registerNewUser(user);
		
		
		return isRegistered;
	}
	
	public static boolean isUserNameAvailable(String userName) {
		
		boolean isUserNameAvailable = RegistrationDao.isUserAlreadyExist(userName);
		
		return isUserNameAvailable;
	}
	
	
	public static boolean updateProfileDetails(Register user) {
		
		boolean isUpdateSucess = RegistrationDao.updateProfileDetails(user);
		
		return isUpdateSucess;
	}
	
}
