package com.se.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import com.se.controller.LoginController;
import com.se.controller.RegistrationController;
import javax.faces.model.SelectItem;

@ManagedBean
@RequestScoped
public class Login implements Serializable {
	
	@ManagedProperty("#{loginController}")
	private LoginController loginController;
	
Register user = new Register();
	
	
/**
	 * 
	 */
private static final long serialVersionUID = -7369491420258013611L;


/**
 * @return the loginController
 */
public LoginController getLoginController() {
	return loginController;
}

/**
 * @param loginController the loginController to set
 */
public void setLoginController(LoginController loginController) {
	this.loginController = loginController;
}



private String loginId;
private String password;
private String msg;
private int selectedManagerId;

private List<SelectItem> availableManagers;
private List<SelectItem> availableApprovedManagers;

public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}

/**
 * @return the loginId
 */
public String getLoginId() {
	return loginId;
}

/**
 * @param loginId the loginId to set
 */
public void setLoginId(String loginId) {
	this.loginId = loginId;
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

public Login() {
	// TODO Auto-generated constructor stub
}

public Login(String loginId, String password) {
	super();
	this.loginId = loginId;
	this.password = password;
}

public String login() {

	Login user = new Login(loginId, password);
	
	boolean isValid = LoginController.validateLogin(user);
	if(isValid == true && user.getLoginId().trim().equalsIgnoreCase("admin")) {
		ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
		return "adminsuccess.xhtml";
	}
	else 
	if (isValid == true) {
		
		Register userDetails = loginController.getUserDetails(loginId);
		if(userDetails.getRole().equals("user")) {		
			System.out.println(userDetails.getRole());
			return "user/success";
		}	else if(userDetails.getRole().equals("manager")) {
			System.out.println(userDetails.getRole());
			return "manager/success";
		}
	} else  {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Invalid Credentials or If you are manager then, not approved",""));
		System.out.println("inside this");
/*		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Incorrect Username and Passowrd",
						"Please enter correct username and Password"));
*/		return "index";
	}
	return "index";
}

@PostConstruct
public void init() {
    //initially populate stock list
    
	availableManagers = loginController.getAvailableManagers();
//	availableStockSymbols = stockController.getAvailableStockSymbols();
	availableApprovedManagers = loginController.getApprovedManagers();
}


public void logout() throws IOException {
	
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	externalContext.invalidateSession();
	externalContext.redirect("../index.xhtml");
	
}



public void updateProfile() throws IOException {

	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	
	System.out.println("*****updateProfile()**** entering");
	
	String userName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
	user = loginController.getUserDetails(userName);
	
	System.out.println(user);
	
	System.out.println("*****updateProfile()**** leaving");
	
	
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("register", user);
	//externalContext.sets
	
	externalContext.redirect("editProfile.xhtml");
	//return "editProfile";
}




public void viewManagerProfile() throws IOException {
	
ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
List<Register> managerList;
	System.out.println("*****viewManagerProfile()**** entering");
	
	 int selectedManagerId  = this.selectedManagerId;

	 managerList = loginController.getManagerProfile(selectedManagerId);
	
	
	System.out.println(managerList);
	
	System.out.println("*****viewManagerProfile()**** leaving");
	
	
	
	
	
	
//	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("register", user);
	//externalContext.sets
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("managerList", managerList);
	
	externalContext.redirect("managerProfile.xhtml");
	//return "editProfile";
	
	
}



public String selectManager() throws IOException {
	
//ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
boolean isSelected;
	System.out.println("*****selectManager()**** entering");
	
	 int selectedManagerId  = this.selectedManagerId;

	 isSelected = loginController.selectManager(selectedManagerId);
	if(isSelected == true) {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager Selecteion Done",""));
	}
	else {
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "You already have a manger.. please CALL 1800-MARKET",""));
	}
	
	
	System.out.println("*****selectManager()**** leaving");
	
	
	
	
	return "viewManagers";
	
//	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("register", user);
	//externalContext.sets
	//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("managerList", managerList);
	
	//externalContext.redirect("viewManagers.xhtml");
	//return "editProfile";
	
	
}




public String approveManger() {
	
	// installAllTrustingManager();

    //System.out.println("selectedItem: " + this.selectedSymbol);
    //System.out.println("selectedInterval: " + this.selectedInterval);
    int selectedManagerId  = this.selectedManagerId;

	
	
		boolean isApproved = 	loginController.approveManager(selectedManagerId);
			
		if(isApproved == true)
			 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Manager Approved Successfully",""));
		else
			 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong... Please try after sometime...",""));
		
		return "adminsuccess";
		
		
		}

/**
 * @return the selectedManagerId
 */
public int getSelectedManagerId() {
	return selectedManagerId;
}

/**
 * @param selectedManagerId the selectedManagerId to set
 */
public void setSelectedManagerId(int selectedManagerId) {
	this.selectedManagerId = selectedManagerId;
}

/**
 * @return the availableManagers
 */
public List<SelectItem> getAvailableManagers() {
	return availableManagers;
}

/**
 * @param availableManagers the availableManagers to set
 */
public void setAvailableManagers(List<SelectItem> availableManagers) {
	this.availableManagers = availableManagers;
}

/**
 * @return the availableApprovedManagers
 */
public List<SelectItem> getAvailableApprovedManagers() {
	return availableApprovedManagers;
}

/**
 * @param availableApprovedManagers the availableApprovedManagers to set
 */
public void setAvailableApprovedManagers(List<SelectItem> availableApprovedManagers) {
	this.availableApprovedManagers = availableApprovedManagers;
}



}