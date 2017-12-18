package com.se.dao;

import com.se.bean.Register;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.data.conn.*;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

@ManagedBean(name="registrationDao",eager=true)
@RequestScoped
public class RegistrationDao {

	public static boolean registerNewUser(Register user) {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "insert into user(first_name, last_name , address , phone_number ,email_address , username ,password,role,balance,fee ) values(?,?,?,?,?,?,?,?,?,?)";
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,user.getFirstName());
			preparedStatement.setString(2,user.getLastName());
			preparedStatement.setString(3,user.getAddress());
			preparedStatement.setString(4,user.getPhoneNumber());
			preparedStatement.setString(5,user.getEmailAddress());
			preparedStatement.setString(6,user.getUserName());
			preparedStatement.setString(7,user.getPassword());
			preparedStatement.setString(8,user.getRole());
			preparedStatement.setFloat(9,user.getBalance());
			preparedStatement.setFloat(10,user.getFee());
			
			
			
			preparedStatement.execute();
			
			connection.close();
			
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return false;
	}
	
	
public static boolean isUserAlreadyExist(String userName) {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from user where username = ?";
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,userName);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
				return true;
			}
			
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return false;
	}


public static boolean updateProfileDetails(Register user) {
	// TODO Auto-generated method stub
	

	Connection connection = null;
	PreparedStatement preparedStatement = null;
	
	try {
		connection = DataConnect.getConnection();
		String userName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
		
		String query = "update user set first_name = ?, last_name =?, address =?, phone_number =?,email_address = ?, username = ?,password= ? where username = ?";
		
		preparedStatement = (PreparedStatement) connection.prepareStatement(query);
		
		preparedStatement.setString(1,user.getFirstName());
		preparedStatement.setString(2,user.getLastName());
		preparedStatement.setString(3,user.getAddress());
		preparedStatement.setString(4,user.getPhoneNumber());
		preparedStatement.setString(5,user.getEmailAddress());
		preparedStatement.setString(6,user.getUserName());
		preparedStatement.setString(7,user.getPassword());
		preparedStatement.setString(8,userName);
		
		
		preparedStatement.executeUpdate();
		
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", user.getFirstName());
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", user.getUserName());
		
		connection.close();
		
		return true;
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		System.out.println(e.getMessage());
	}
	
	
	
	
	return false;
}
	
}
