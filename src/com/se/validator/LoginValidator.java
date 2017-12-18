package com.se.validator;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@ManagedBean
@SessionScoped
@FacesValidator("loginValidator")
public class LoginValidator implements Validator {

	
	public String loginId;
	public String password;
	
	
	
	
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getLoginId() {
		return loginId;
	}


	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


	public LoginValidator() {
		// TODO Auto-generated constructor stub
	}

	

	
	public void validateLoginId(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String loginId = (String) value;

		
		
		if (loginId == null) {
			FacesMessage msg = new FacesMessage(
					"Login Id cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		
	}

	
	
	public void validatePassword(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String password = (String) value;

		
		
		if (password == null) {
			FacesMessage msg = new FacesMessage(
					"Password cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);	
			throw new ValidatorException(msg);
		}
		
		
	}




	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		// TODO Auto-generated method stub
		
	}
	
	
}
