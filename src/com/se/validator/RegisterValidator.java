package com.se.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import com.se.controller.RegistrationController;

@ManagedBean
@SessionScoped
@FacesValidator("registerValidator")
public class RegisterValidator implements Validator {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\." +
			"[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*" +
			"(\\.[A-Za-z]{2,})$";
	
	private static final String PHONE_NUMBER_PATTERN = "[0-9 ]{10,10}+";
	
	
	Pattern emailPattern, phoneNumberPattern;
	Matcher matcher;
	
	public RegisterValidator() {
		// TODO Auto-generated constructor stub
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		phoneNumberPattern = Pattern.compile(PHONE_NUMBER_PATTERN);
	}
	
	public void validateFirstName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String firstName = (String) value;

		
		
		if (firstName == null) {
			FacesMessage msg = new FacesMessage(
					"First Name cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		
	}


	public void validateLastName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String lastName = (String) value;

		
		
		if (lastName == null) {
			FacesMessage msg = new FacesMessage(
					"Last Name cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		
	}


	public void validateAddress(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String address = (String) value;

		
		
		if (address == null) {
			FacesMessage msg = new FacesMessage(
					"Address cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		
	}


	public void validatePhoneNumber(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String phoneNumber = (String) value;

		
		
		if (phoneNumber == null) {
			FacesMessage msg = new FacesMessage(
					"Phone Number cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		matcher = phoneNumberPattern.matcher(phoneNumber.toString());
		
		if(!matcher.matches()) {
			FacesMessage msg = new FacesMessage(
					"Enter Valid Phone Number");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
			
		
	}


	public void validateEmail(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String emailAddress = (String) value;

		
		
		if (emailAddress == null) {
			FacesMessage msg = new FacesMessage(
					"Email Address cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		
		matcher = emailPattern.matcher(emailAddress.toString());
		
		if(!matcher.matches()) {
			FacesMessage msg = new FacesMessage(
					"Enter Valid Email Address");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
				
		
		
		
	}


	public void validateUserName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		
		String userName = (String) value;

		
		
		if (userName == null) {
			FacesMessage msg = new FacesMessage(
					"User Name cannot be empty");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
		
		boolean isUserAlreadyTaken = RegistrationController.isUserNameAvailable(userName);
		
		if(isUserAlreadyTaken == true) {
			FacesMessage msg = new FacesMessage(
					"User Name already taken. Please choose another username");
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
