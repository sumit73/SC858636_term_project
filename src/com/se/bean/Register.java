package com.se.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.se.controller.LoginController;
import com.se.controller.RegistrationController;

@ManagedBean
@RequestScoped
public class Register implements Serializable {

	@ManagedProperty("#{registrationController}")
	private RegistrationController registrationController;
	
	private static final long serialVersionUID = -1149252938824280071L;
	
	/**
	 * @return the registrationController
	 */
	public RegistrationController getRegistrationController() {
		return registrationController;
	}
	/**
	 * @param registrationController the registrationController to set
	 */
	public void setRegistrationController(RegistrationController registrationController) {
		this.registrationController = registrationController;
	}


	
		
	public String firstName;
	public String lastName;
	public String address;
	public String phoneNumber;
	public String emailAddress;
	public String userName;
	public String password;
	public String role;
	public Float balance = 0F;
	public Float fee = 0F;
	
	
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
		/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	
	public Register() {
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phoneNumber
	 * @param emailAddress
	 * @param userName
	 * @param password
	 * @param role
	 * @param balance
	 * @param fee
	 */
	public Register(String firstName, String lastName, String address, String phoneNumber, String emailAddress,
			String userName, String password, String role, Float balance, Float fee) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.balance = balance;
		this.fee = fee;
	}
	public Register(String firstName, String lastName, String address, String phoneNumber, String emailAddress,
			String userName, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
		this.userName = userName;
		this.password = password;
	}
	
	
	
	public String register(){
		
		
		Register user = new Register(firstName, lastName, address, phoneNumber, emailAddress, userName, password,role,balance,fee);
		if(user.getRole().equals("user"))
			user.setBalance(100000.00F);
		
		boolean isRegistered = RegistrationController.newUSerRegistration(user);
		if(isRegistered == true)
			return "registered";
		else
			return "error";
	}
	
	
	
	public void updateProfileDetails() throws IOException {
		
		Register user = new Register(firstName, lastName, address, phoneNumber, emailAddress, userName,password);
		
		ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
		
		System.out.println("*****updateProfileDetails()**** entering");
		
		//System.out.println(user);
		
		boolean isUpdateSucess = RegistrationController.updateProfileDetails(user);
		
		System.out.println("*****updateProfileDetails()**** leaving");
		
		
		if(isUpdateSucess == true)
			externalContext.redirect("editProfileSuccess.xhtml");
		else
			externalContext.redirect("editProfileFailure.xhtml");

		
		
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Register [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", phoneNumber="
				+ phoneNumber + ", emailAddress=" + emailAddress + ", userName=" + userName + ", password=" + password
				+ ", role=" + role + "]";
	}
	/**
	 * @return the balance
	 */
	public Float getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Float balance) {
		this.balance = balance;
	}
	/**
	 * @return the fee
	 */
	public Float getFee() {
		return fee;
	}
	/**
	 * @param fee the fee to set
	 */
	public void setFee(Float fee) {
		this.fee = fee;
	}
	
	
	
	
	
	
}
