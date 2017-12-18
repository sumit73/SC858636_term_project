package com.se.bean;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.data.conn.DataConnect;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.controller.LoginController;
import com.se.controller.StockController;



@ManagedBean
@RequestScoped
public class Stock {

    private static final long serialVersionUID = 1L;
    static final String API_KEY = "54DFGLR2VHRBD7T5";

    
    private List<StockDetails> myStockWatchList;
    private List<Order> orderList;
    
    @ManagedProperty("#{stockController}")
	private StockController stockController;
    
    
    private int selectedStockId;
    
    private String symbol;
    private double price;
    private int qty;
    private double amt;
    private String table1Markup;
    private String table2Markup;

    private String selectedSymbol;
    private List<SelectItem> availableSymbols;
    private List<SelectItem> availableStockSymbols;
    
    private String purchaseSymbol;
    private double purchasePrice;
    
    
    
    private String getRequestParameter(String name) {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter(name);
    }

    @PostConstruct
    public void init() {
        //initially populate stock list
        
    	availableSymbols = stockController.getAvailableStocks();
    	availableStockSymbols = stockController.getAvailableStockSymbols();
    	
        //initially populate intervals for stock api
        availableIntervals = new ArrayList<SelectItem>();
        availableIntervals.add(new SelectItem("1min", "1min"));
        availableIntervals.add(new SelectItem("5min", "5min"));
        availableIntervals.add(new SelectItem("15min", "15min"));
        availableIntervals.add(new SelectItem("30min", "30min"));
        availableIntervals.add(new SelectItem("60min", "60min"));
    }

    private String selectedInterval;
    private List<SelectItem> availableIntervals;

    public String getSelectedInterval() {
        return selectedInterval;
    }

    public void setSelectedInterval(String selectedInterval) {
        this.selectedInterval = selectedInterval;
    }

    public List<SelectItem> getAvailableIntervals() {
        return availableIntervals;
    }

    public void setAvailableIntervals(List<SelectItem> availableIntervals) {
        this.availableIntervals = availableIntervals;
    }

    public String getSelectedSymbol() {
        return selectedSymbol;
    }

    public void setSelectedSymbol(String selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public List<SelectItem> getAvailableSymbols() {
        return availableSymbols;
    }

    public void setAvailableSymbols(List<SelectItem> availableSymbols) {
        this.availableSymbols = availableSymbols;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public String getTable1Markup() {
        return table1Markup;
    }

    public void setTable1Markup(String table1Markup) {
        this.table1Markup = table1Markup;
    }

    public String getTable2Markup() {
        return table2Markup;
    }

    public void setTable2Markup(String table2Markup) {
        this.table2Markup = table2Markup;
    }

    public String createDbRecord(String symbol, double price, int qty, double amt) {
        try {
            //System.out.println("symbol: " + this.symbol + ", price: " + this.price + "\n");
            //System.out.println("qty: " + this.qty + ", amt: " + this.amt + "\n");

            Connection conn = DataConnect.getConnection();
            Statement statement = conn.createStatement();
            
            //get userid
            Integer uid = Integer.parseInt((String) FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap().get("uid"));
            
            System.out.println(uid);
            System.out.println("symbol:" + symbol);
            System.out.println("price:" + price);
            System.out.println("qty:" + qty);
            System.out.println("amt:" + amt);
            statement.executeUpdate("INSERT INTO `purchase` (`id`, `uid`, `stock_symbol`, `qty`, `price`, `amt`) "
                    + "VALUES (NULL,'" + uid + "','" + symbol + "','" + qty + "','" + price + "','" + amt +"')");
            
            statement.close();
            conn.close();
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "purchase";
    }

    public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }

    public void timeseries() throws MalformedURLException, IOException {

        installAllTrustingManager();

        //System.out.println("selectedItem: " + this.selectedSymbol);
        //System.out.println("selectedInterval: " + this.selectedInterval);
        String symbol = this.selectedSymbol;
        String interval = this.selectedInterval;
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;

        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = new URL(url).openStream();

        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        for (String key : mainJsonObj.keySet()) {/*
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
            } else {
                this.table2Markup = null; // reset table 2 markup before repopulating
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
                this.table2Markup += "<table class='table table-hover'>";
                this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
                this.table2Markup += "<tbody>";
                int i = 0;
                for (String subKey : dataJsonObj.keySet()) {
                    JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                    this.table2Markup
                            += "<tr>"
                            + "<td>" + subKey + "</td>"
                            + "<td>" + subJsonObj.getString("1. open") + "</td>"
                            + "<td>" + subJsonObj.getString("2. high") + "</td>"
                            + "<td>" + subJsonObj.getString("3. low") + "</td>"
                            + "<td>" + subJsonObj.getString("4. close") + "</td>"
                            + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                    if (i == 0) {
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        this.table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/purchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    }
                    this.table2Markup += "</tr>";
                    i++;
                }
                this.table2Markup += "</tbody></table>";
            }
        */}
        return;
    }

    public void purchaseStock() {
        System.out.println("Calling function purchaseStock()");
        System.out.println("stockSymbol: " + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockSymbol"));
        System.out.println("stockPrice" + FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stockPrice"));
        return;
    }

	/**
	 * @return the stockController
	 */
	public StockController getStockController() {
		return stockController;
	}

	/**
	 * @param stockController the stockController to set
	 */
	public void setStockController(StockController stockController) {
		this.stockController = stockController;
	}
	
	
	
	/**
	 * @return the selectedStockId
	 */
	public int getSelectedStockId() {
		return selectedStockId;
	}

	/**
	 * @param selectedStockId the selectedStockId to set
	 */
	public void setSelectedStockId(int selectedStockId) {
		this.selectedStockId = selectedStockId;
	}

	/**
	 * @return the apiKey
	 */
	public static String getApiKey() {
		return API_KEY;
	}
	
	
	
public void addToWatchList() {
		
	 installAllTrustingManager();

     //System.out.println("selectedItem: " + this.selectedSymbol);
     //System.out.println("selectedInterval: " + this.selectedInterval);
     int selectedStockId = this.selectedStockId;
 
	
	
		boolean isInserted = 	stockController.addToWatchList(selectedStockId);
			
		if(isInserted == true) {
			ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
			try {
				externalContext.redirect("stockAddedToWatchListSuccess.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(isInserted == false) {
			ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
			try {
				externalContext.redirect("user/stockAddedToWatchListFailure.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		}

	
public void myWatchList() throws IOException {
	
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	System.out.println("*****myWatchList()**** entering");
	String userName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
	myStockWatchList = stockController.getMyWatchList(userName);
	System.out.println(myStockWatchList);
	System.out.println("*****myWatchList()**** leaving");
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("myStockWatchList", myStockWatchList);
	externalContext.redirect("stockWatchList.xhtml");
	
}



public void myOrders() throws IOException {
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	System.out.println("*****myOrders()**** entering");
	int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
	orderList = stockController.getMyOrders(user_id);
	System.out.println(myStockWatchList);
	System.out.println("*****myOrders()**** leaving");
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("orders", orderList);
	externalContext.redirect("myOrders.xhtml");

}


public void managerOrders() throws IOException {
	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	System.out.println("*****myOrders()**** entering");
	int user_id = (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id");
	orderList = stockController.getMyOrders(user_id);
	System.out.println(myStockWatchList);
	System.out.println("*****myOrders()**** leaving");
	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("orders", orderList);
	externalContext.redirect("myOrders.xhtml");

}






public void viewHistory() throws IOException {
	
	 installAllTrustingManager();
	
	 
	 String symbol = this.selectedSymbol;
     String interval = this.selectedInterval;
     String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;
     this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
     InputStream inputStream = new URL(url).openStream();

     // convert the json string back to object
     JsonReader jsonReader = Json.createReader(inputStream);
     JsonObject mainJsonObj = jsonReader.readObject();
     for (String key : mainJsonObj.keySet()) {
         if (key.equals("Meta Data")) {
             this.table1Markup = null; // reset table 1 markup before repopulating
             JsonObject jsob = (JsonObject) mainJsonObj.get(key);
             this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
             this.table1Markup += "<table>";
             this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
             this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
             this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
             this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
             this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
             this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
             this.table1Markup += "</table>";
         } else {
             this.table2Markup = null; // reset table 2 markup before repopulating
             JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
             this.table2Markup += "<table class='table table-hover'>";
             this.table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
             this.table2Markup += "<tbody>";
             int i = 0;
             for (String subKey : dataJsonObj.keySet()) {
                 JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                 this.table2Markup
                         += "<tr>"
                         + "<td>" + subKey + "</td>"
                         + "<td>" + subJsonObj.getString("1. open") + "</td>"
                         + "<td>" + subJsonObj.getString("2. high") + "</td>"
                         + "<td>" + subJsonObj.getString("3. low") + "</td>"
                         + "<td>" + subJsonObj.getString("4. close") + "</td>"
                         + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                 if (i == 0) {
                    // String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                     this.table2Markup += "<td><a class='btn btn-success' href='" + "purchase.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                 }
                 this.table2Markup += "</tr>";
                 i++;
             }
             this.table2Markup += "</tbody></table>";
         }
     }
     return;
  
		
/*	ExternalContext externalContext =  FacesContext.getCurrentInstance().getExternalContext();
	System.out.println("*****viewHistory()**** entering");
	System.out.println("*****viewHistory()**** leaving");
	externalContext.redirect("stocksHistory.xhtml");
*/	
}

/**
 * @return the availableStockSymbols
 */
public List<SelectItem> getAvailableStockSymbols() {
	return availableStockSymbols;
}

/**
 * @param availableStockSymbols the availableStockSymbols to set
 */
public void setAvailableStockSymbols(List<SelectItem> availableStockSymbols) {
	this.availableStockSymbols = availableStockSymbols;
}


public String purchase(String symbol, double price, int qty, double amt) {
   
	boolean isOrderInserted = stockController.purchase(symbol, price, qty, amt);
	if(isOrderInserted == true)
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Successfully purchased stock",""));
	else
		 FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO, "Something went wrong... Please try after sometime...",""));
	
	return "purchase";
}

/**
 * @return the purchaseSymbol
 */
public String getPurchaseSymbol() {
	if (getRequestParameter("symbol") != null) {
		purchaseSymbol = getRequestParameter("symbol");
    }return purchaseSymbol;
}

/**
 * @param purchaseSymbol the purchaseSymbol to set
 */
public void setPurchaseSymbol(String purchaseSymbol) {
	
	this.purchaseSymbol = purchaseSymbol;
}

/**
 * @return the purchasePrice
 */
public double getPurchasePrice() {
	 if (getRequestParameter("price") != null) {
		 purchasePrice = Double.parseDouble(getRequestParameter("price"));
         System.out.println("price: " + price);
     }return purchasePrice;
}

/**
 * @param purchasePrice the purchasePrice to set
 */
public void setPurchasePrice(double purchasePrice) {
	this.purchasePrice = purchasePrice;
}

}