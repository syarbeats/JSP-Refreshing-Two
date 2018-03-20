package com.cdc.mitrais.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductBean implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String prodID;
	private String prodDesc;
	private String prodManuf;
	private float prodPrice;
	
	private List<String> prodIDList, prodDescList, prodManufList;
	private List<Float> prodPriceList;
	
	private int currentRow;
	private int rowCount;
	
	private Connection db =null;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductBean.class);
	
	
	public ProductBean(){
		this.setProdID("");
		this.setProdDesc("");
		this.setProdManuf("");
		this.setProdPrice(0.00f);
		
		 /* Initialize arrayLists to hold recordsets */
	    prodIDList = new ArrayList<String>();
	    prodDescList = new ArrayList<String>();
	    prodManufList = new ArrayList<String>();
	    prodPriceList = new ArrayList<Float>();

	    /* Initialize helper variables */
	    setCurrentRow(0);
	    setRowCount(0);

	    /* Get database connection */
	    dbConnect();
	}
	
	private void dbConnect() {
		if(db == null) {
			try {
				Class.forName("org.gjt.mm.mysql.Driver");
				db = DriverManager.getConnection("jdbc:mysql://localhost:3306/zeus?user=root&password=");
			} catch (ClassNotFoundException e) {
				logger.debug("Driver Not Found");
				logger.debug(e.getMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean populate() {
		
		if(this.prodIDList.isEmpty()) {
			
			try {
				Statement statement = db.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from product");
				
				this.prodIDList.clear();
				this.prodDescList.clear();
				this.prodManufList.clear();
				this.prodPriceList.clear();
				
				rowCount = 0;
		        while (resultSet.next()) {
		          prodIDList.add(resultSet.getString("id"));
		          prodDescList.add(resultSet.getString("description"));
		          prodManufList.add(resultSet.getString("manuf"));
		          prodPriceList.add((new Float(resultSet.getFloat("price"))));
		          rowCount++;
		        }
		        
			}catch(Exception e) {
				logger.debug("Error populating productBean:"+e.toString());
			}
		}
		
		return true;
	}
	
	public String getProdID() {
		return prodID;
	}
	
	public void setProdID(String prodID) {
		this.prodID = prodID;
	}
	
	public String getProdDesc() {
		return prodDesc;
	}
	
	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}
	
	public String getProdManuf() {
		return prodManuf;
	}
	
	public void setProdManuf(String prodManuf) {
		this.prodManuf = prodManuf;
	}
	
	public float getProdPrice() {
		return prodPrice;
	}
	
	public void setProdPrice(float prodPrice) {
		this.prodPrice = prodPrice;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	
	public void setStartRow(int _start) {
		if (_start < rowCount) {
			currentRow = _start;
		}
	}

	  /* Move to next row */
	public int nextRow() {

		  if (currentRow == rowCount) {
			  currentRow = 0; // Reset for next page request
			  return 0; // return 0 to indicate end of recordset
		  }

		  /* Populate bean properties with current row */
		  setProdID((String)prodIDList.get(currentRow));

		  setProdDesc((String)prodDescList.get(currentRow));

		  setProdManuf((String)prodManufList.get(currentRow));

		  Float price = (Float)prodPriceList.get(currentRow);
		  setProdPrice(price.floatValue());

		  currentRow++;

		  /* return currentRow*/
		  return currentRow;
	}
}
