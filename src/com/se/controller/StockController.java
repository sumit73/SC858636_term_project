package com.se.controller;


import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.se.bean.Login;
import com.se.bean.Order;
import com.se.bean.Register;
import com.se.bean.Stock;
import com.se.bean.StockDetails;
import com.se.dao.LoginDao;
import com.se.dao.RegistrationDao;
import com.se.dao.StockDao;

@ManagedBean(name="stockController",eager=true)
@RequestScoped
public class StockController {

	@ManagedProperty("#{stockDao}")
	private StockDao stockDao;
	
	


	



	/**
	 * @return the stockDao
	 */
	public StockDao getStockDao() {
		return stockDao;
	}




	/**
	 * @param stockDao the stockDao to set
	 */
	public void setStockDao(StockDao stockDao) {
		this.stockDao = stockDao;
	}
	
	
	
	public List<SelectItem> getAvailableStocks() {
		
		List<SelectItem> availableStocks = stockDao.getAvailableStocks();
			
			return availableStocks;
		}

	public List<SelectItem> getAvailableStockSymbols() {
		
		List<SelectItem> availableStocks = stockDao.getAvailableStocksSymbols();
			
			return availableStocks;
		}




public boolean addToWatchList(int stock_no) {
	
	boolean isInserted = stockDao.addToWatchList(stock_no);	
	
	return true;
}
	

public List<StockDetails> getMyWatchList(String userName) {
	
	
	List<StockDetails> myWatchList = stockDao.getMyWatchList(userName);
	return myWatchList;
}


public boolean purchase(String symbol, double price, int qty, double amt) {
	boolean isOrderInserted = stockDao.purchase(symbol, price, qty, amt);
	return isOrderInserted;
	
		}



public List<Order> getMyOrders(int user_id){
	List<Order> orders = stockDao.getMyOrders(user_id);
	return orders;
}

}
