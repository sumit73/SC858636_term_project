package com.se.dao;

import com.se.bean.Login;
import com.se.bean.Register;
import com.se.bean.StockDetails;
import com.se.controller.LoginController;
import com.se.bean.Order;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.data.conn.*;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.dao.StockDao;

@ManagedBean(name="stockDao",eager=true)
@RequestScoped
public class StockDao implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SelectItem> availableSymbols;
	private List<StockDetails> stocksInWatchList;
	private List<Order> ordersList;
	
	
	
	public List<SelectItem> getAvailableStocks() {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from stocks_available";
			
			availableSymbols = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
		
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				availableSymbols.add(new SelectItem( rs.getString("stock_id") , rs.getString("stock_symbol") ));
			
				
			}
			connection.close();
			
			return availableSymbols;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
	
		
		
		
		return null;
	}
	
	
	
	
public List<SelectItem> getAvailableStocksSymbols() {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from stocks_available";
			
			availableSymbols = new ArrayList<SelectItem>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
		
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				availableSymbols.add(new SelectItem( rs.getString("stock_symbol") , rs.getString("stock_symbol") ));
			
				
			}
			connection.close();
			
			return availableSymbols;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
	
		
		
		
		return null;
	}
	
	
	
	public boolean addToWatchList(int stock_no) {
		
		

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int user_id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString());
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "insert into watch_list (stock_no, user_id ) values(?,?)";
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setInt(1,stock_no);
			preparedStatement.setInt(2,user_id);
			
			
			
			
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
	
	
	
	public List<StockDetails> getMyWatchList(String userName) {
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		int user_id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString());
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from watch_list where user_id = ?";
			
			stocksInWatchList = new ArrayList<StockDetails>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setInt(1,user_id);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			
			
			
			//preparedStatement.execute();
			int count =0;
			
			while(rs.next()) {
				count++;
				int stock_no = rs.getInt("stock_no");
						
				
				String query2 = "select * from stocks_available where stock_id = ?";
				
				//stocksInWatchList = new ArrayList<SelectItem>();
				preparedStatement2 = (PreparedStatement) connection.prepareStatement(query2);
				preparedStatement2.setInt(1,stock_no);
				
				
				ResultSet rs2 = preparedStatement2.executeQuery();
				String stockSymbol= "";
				String stockName = "";
				String currentPrice ="";
				
				while(rs2.next()) {
					
					
					
					stockSymbol = rs2.getString("stock_symbol");
					stockName = rs2.getString("stock_name");
					
					

					System.out.println("Calling AlphaVantage API...");
					Client client= ClientBuilder.newClient();

					// Core settings are here, put what ever API parameter you want to use
					WebTarget target= client.target("https://www.alphavantage.co/query")
					   .queryParam("function", "TIME_SERIES_INTRADAY")
					   .queryParam("symbol", stockSymbol)
					   .queryParam("interval", "15min")
					   .queryParam("outputsize", "1")
					   .queryParam("apikey", "54DFGLR2VHRBD7T5");
					// Actually calling API here, Use HTTP GET method
					// data is the response JSON string
					String data = target.request(MediaType.APPLICATION_JSON).get(String.class);
					
					try {
						// Use Jackson to read the JSON into a tree like structure
						ObjectMapper mapper = new ObjectMapper();
						JsonNode root = mapper.readTree(data);
						
						// Make sure the JSON is an object, as said in their documents
						assert root.isObject();
						// Read the "Meta Data" property of JSON object
						JsonNode metadata = root.get("Meta Data");
						assert metadata.isObject();
						// Read "2. Symbol" property of "Meta Data" property
						if (metadata.get("2. Symbol").isValueNode()) {
							System.out.println(metadata.get("2. Symbol").asText());
						}
						// Print "4. Time Zone" property of "Meta Data" property of root JSON object
						System.out.println(root.at("/Meta Data/4. Time Zone").asText());
						// Read "Weekly Time Series" property of root JSON object
						Iterator<String> dates = root.get("Time Series (15min)").fieldNames();
						while(dates.hasNext()) {
							// Read the first date's open price
							currentPrice = root.at("/Time Series (15min)/" + dates.next() + "/1. open").asText();
							System.out.println(Double.parseDouble(currentPrice));
							// remove break if you wan't to print all the open prices.
							break;
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
					
					
					
				}
				
				StockDetails sd = new StockDetails();
				sd.setStockNumber(count);
				sd.setStockName(stockSymbol);
				sd.setProductName(stockName);
				sd.setCurrentPrice(currentPrice);
				
				stocksInWatchList.add(sd);                                                                           
				
			}
			connection.close();
			
			return stocksInWatchList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return null;
	}
	
	
	
	
	public boolean purchase(String symbol, double price, int qty, double amt) {
		
		

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int user_id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id").toString());
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "insert into ordertbl (stock_symbol, price, qty, amount, txntype, userid ) values(?,?,?,?,?,?)";
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			
			preparedStatement.setString(1,symbol);
			preparedStatement.setFloat(2,(float)price);
			preparedStatement.setInt(3,qty);
			preparedStatement.setFloat(4,(float)amt);
			preparedStatement.setString(5,"BUY");
			preparedStatement.setInt(6,user_id);
			
			
			
			
			preparedStatement.execute();
			
			
			String balanceQuery = "select balance from user where registration_id = ?";
			
			
			preparedStatement = (PreparedStatement) connection.prepareStatement(balanceQuery);
			
			preparedStatement.setInt(1,user_id);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()) {
			
			float balance = rs.getFloat("balance");
				balance = balance - (float)amt;
				
				
				String balanceUpdate = "update user set balance = ? where registration_id = ?";
				
				preparedStatement = (PreparedStatement) connection.prepareStatement(balanceUpdate);
				
				preparedStatement.setFloat(1,balance);
				preparedStatement.setInt(2,user_id);


				preparedStatement.executeUpdate();
				
				
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentbalance", balance);
				
				connection.close();
			
			}
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	public List<Order> getMyOrders(int user_id) {
		
		




		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		//user_id = (int)(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
		
		try {
			connection = DataConnect.getConnection();
			
			String query = "select * from ordertbl where userid = ?";
			
			ordersList = new ArrayList<Order>();
			preparedStatement = (PreparedStatement) connection.prepareStatement(query);
			preparedStatement.setInt(1,user_id);
			
			
			ResultSet rs = preparedStatement.executeQuery();
			
			
			
			Order order = null;
			while(rs.next()) {
						
					order = new Order();
					order.setOrderId(rs.getInt("order_id"));
					order.setStockSymbol(rs.getString("stock_symbol"));
					order.setPrice(rs.getFloat("price"));
					order.setQuantity(rs.getInt("qty"));
					order.setAmount(rs.getFloat("amount"));
					order.setTransactionType(rs.getString("txntype"));
					ordersList.add(order);  
					}
				
			connection.close();
			return ordersList;                                                                   
				
			}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
		
		
		
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

