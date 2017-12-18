package com.se.dao;

import com.se.bean.Login;
import com.se.bean.Register;
import com.se.bean.StockDetails;
import com.se.controller.LoginController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.data.conn.*;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;


@ManagedBean(name="loginDao",eager=true)
@RequestScoped
public class LoginDao {

	
	private List<SelectItem> availableManagers;
	private List<Register> managerList;
	
	public static boolean validateLgin(Login user) {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select balance,registration_id, first_name,username,role,approved from user where username = ? and password = ?";
			
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,user.getLoginId());
			preparedStatement.setString(2, user.getPassword());
			
			int userId = 0;
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", rs.getString("first_name"));
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", rs.getString("username"));
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_id", rs.getInt("registration_id"));
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentbalance", rs.getString("balance"));
				if((rs.getString("role")).equals("user"))
				{
					
					
					String query2 = "select manager_id from user_manager where user_id = ?";
					
					
					preparedStatement = (PreparedStatement) connection.prepareStatement(query2);
					
					preparedStatement.setInt(1, rs.getInt("registration_id"));
					
					
					ResultSet rs2 = preparedStatement.executeQuery();
					
					if(rs2.next()) {
					
						int managerId = rs2.getInt("manager_id");
						
						
						String query3 = "select * from user where registration_id = ?";
						
						
						preparedStatement = (PreparedStatement) connection.prepareStatement(query3);
						
						preparedStatement.setInt(1,managerId);
						
						
						ResultSet rs3 = preparedStatement.executeQuery();
						
						if(rs3.next()) {
					
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedmanager", rs3.getString("first_name") + " "+ rs3.getString("last_name"));
					
					
				}
						else {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedmanager", "NOT ASSIGNED");
								
						}
						
						connection.close();
						
						return true;
					}
					
				
				return true;
						
				}
				else if(((rs.getString("role")).equals("manager")) && (rs.getInt("approved") == 0))
					{connection.close();
					
					return true;
					}
			}
			
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return false;
	}
	
	
	
	public Register getUserDetails(String userName) {
		
		Register user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from user where username = ?";
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,userName);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
				
			if(rs.next()) {
			
				user = new Register();
				
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmailAddress(rs.getString("email_address"));
				user.setAddress(rs.getString("address"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setPassword(rs.getString("password"));
				user.setUserName(rs.getString("username"));
				user.setRole(rs.getString("role"));
				
			}
			
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	
public List<SelectItem> getAvailableManagers() {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from user where role = ? and approved = ?";
			
			availableManagers = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,"manager");
			preparedStatement.setInt(2,1);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				availableManagers.add(new SelectItem( rs.getString("registration_id") , rs.getString("first_name") + " "+ rs.getString("last_name") +" : " + rs.getString("username") ));
				
				
			}
			connection.close();
			
			return availableManagers;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
	
		
		
		
		return null;
	}



public List<SelectItem> getApprovedManagers() {
	
	
	Connection connection = null;
	PreparedStatement preparedStatement = null;
	
	try {
		connection = DataConnect.getConnection();
		
		String query = "select * from user where role = ? and approved = ?";
		
		availableManagers = new ArrayList<SelectItem>();
		preparedStatement = (PreparedStatement) connection.prepareStatement(query);
		
		preparedStatement.setString(1,"manager");
		preparedStatement.setInt(2,0);
		
		
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()) {
			availableManagers.add(new SelectItem( rs.getString("registration_id") , rs.getString("first_name") + " "+ rs.getString("last_name") +" : " + rs.getString("username") ));
			
			
		}
		connection.close();
		
		return availableManagers;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
		System.out.println(e.getMessage());
	}
	
	

	
	
	
	return null;
}
	




	public boolean approveManager(int selectedManagerId) {
		

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int user_id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString());
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "update user set approved = ? where registration_id = ?";
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setInt(1,0);
			preparedStatement.setInt(2,selectedManagerId);
			
			
			
			
				preparedStatement.executeUpdate();
				
				
				
				connection.close();
				return true;
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return false;
	}
	
	
	
	
	public List<Register> getManagerProfile(int selectedManagerId) {
		Register user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from user where registration_id = ?";
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setInt(1,selectedManagerId);
			managerList = new ArrayList<Register>();
			
			ResultSet rs = preparedStatement.executeQuery();
			
				
			while(rs.next()) {
			
				user = new Register();
				
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmailAddress(rs.getString("email_address"));
				user.setAddress(rs.getString("address"));
				user.setPhoneNumber(rs.getString("phone_number"));
				
				managerList.add(user);
			}
			
			return managerList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		return null;
		
		
	}
	
	
	
	
	
	
	
	
	public boolean selectManager(int selectedManagerId) {
		Register user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int user_id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString());
				
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from user_manager where user_id = ?";
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setInt(1,user_id);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			int managerId = 0;
			while(rs.next()) {
			
				user = new Register();
				
				managerId = rs.getInt("manager_id");
				
				}
				if(managerId > 0) {
					return false;
				}
				else {
					String query2 = "insert into user_manager(user_id, manager_id) values(?,?)";
					
					preparedStatement = (PreparedStatement) connection.prepareStatement(query2);
					
					preparedStatement.setInt(1,user_id);
					preparedStatement.setInt(2,selectedManagerId);
					
					
					
					preparedStatement.execute();
					
					
					
					String query3 = "select * from user where registration_id = ?";
					
					
					preparedStatement = (PreparedStatement) connection.prepareStatement(query3);
					
					preparedStatement.setInt(1,selectedManagerId);
					
					ResultSet rs3 = preparedStatement.executeQuery();
					
					if(rs3.next()) {
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedmanager", rs3.getString("first_name") + rs3.getString("last_name"));
				
					
					
					
					connection.close();
					
					return true;
				}
		} 		
	}
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		return false;
		
	
	}
	
	
}
