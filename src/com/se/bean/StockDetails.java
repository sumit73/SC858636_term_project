package com.se.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class StockDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stockName;
	private String productName;
	private int stockNumber;
	private String currentPrice;
	
	
	/**
	 * @return the stockName
	 */
	public String getStockName() {
		return stockName;
	}
	/**
	 * @param stockName the stockName to set
	 */
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the stockNumber
	 */
	public int getStockNumber() {
		return stockNumber;
	}
	/**
	 * @param stockNumber the stockNumber to set
	 */
	public void setStockNumber(int stockNumber) {
		this.stockNumber = stockNumber;
	}
	/**
	 * @return the currentPrice
	 */
	public String getCurrentPrice() {
		return currentPrice;
	}
	/**
	 * @param currentPrice the currentPrice to set
	 */
	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	
}
