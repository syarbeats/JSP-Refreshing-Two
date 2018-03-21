package com.cdc.mitrais.model;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CustomerBean implements java.io.Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* Member Variables */
	private String id, lname, fname, sex, married;
	private int age, children;
	private boolean spouse, smoker;

	/* Helper Variables */
	private Connection db = null;
	private String status;

	private int currentRow;
	private int rowCount;
	
	List<String> lNameList, fNameList, sexList, marriedList, smokerList, spouseList;
	List<Integer> custIdList, ageList, childrenList;
	
	/* Error collection */
	Hashtable<String, String> errors = new Hashtable<String, String>();

	/* Constants */
	public static final int FIELD_NAME = 0;
	public static final int FIELD_VALUE = 1;

	private static final Logger logger = LoggerFactory.getLogger(CustomerBean.class);

	/* Constructor */
	public CustomerBean() {
		/* Initialize properties */
		setLname("");
		setFname("");
		setSex("");
		setAge(0);
		setChildren(0);
		setSpouse(false);
		setSmoker(false);
		setStatus("");
		setMarried("");

		setId("");  // Not really a property, so no accessor method

		lNameList = new ArrayList<String>();
		fNameList = new ArrayList<String>();
		sexList   = new ArrayList<String>();
		marriedList = new ArrayList<String>();
		smokerList = new ArrayList<String>();
		spouseList = new ArrayList<String>();
		custIdList = new ArrayList<Integer>();
		ageList = new ArrayList<Integer>();
		childrenList = new ArrayList<Integer>();
		
		 setCurrentRow(0);
		 setRowCount(0);
		
		/* Get database connection */
		dbConnect();
	}

	public void setStartRow(int start) {
		if (start < rowCount) {
			currentRow = start;
		}
	}

	
	public boolean populate() {

		if(this.custIdList.isEmpty()) {

			try {
				Statement statement = db.createStatement();
				ResultSet resultSet = statement.executeQuery("select * from customer");

				this.custIdList.clear();
				this.lNameList.clear();
				this.fNameList.clear();
				this.sexList.clear();
				this.marriedList.clear();
				this.smokerList.clear();
				this.ageList.clear();
				this.childrenList.clear();
				
				
				rowCount = 0;
				
				while (resultSet.next()) {
					custIdList.add(resultSet.getInt("id"));
					lNameList.add(resultSet.getString("lname"));
					fNameList.add(resultSet.getString("fname"));
					sexList.add(resultSet.getString("sex"));
					marriedList.add(resultSet.getString("married"));
					smokerList.add(resultSet.getString("smoker"));
					childrenList.add(resultSet.getInt("children"));
					ageList.add(resultSet.getInt("age"));
					//this.spouseList.add(resultSet.getString("spouse"));
					rowCount++;
					logger.debug("FirstName:"+resultSet.getString("fname")+" LastName:"+resultSet.getString("lname")+" "+" Sex:"+resultSet.getString("sex")+" Married:"+resultSet.getString("married"));
				}

			}catch(Exception e) {
				logger.debug("Error populating Customer Bean:"+e.toString());
			}
		}

		return true;
	}
	
	public int nextRow() {

		  if (currentRow == rowCount) {
			  currentRow = 0; // Reset for next page request
			  return 0; // return 0 to indicate end of recordset
		  }

		  this.setAge(this.ageList.get(currentRow));
		  this.setChildren(this.childrenList.get(currentRow));
		  this.setFname(this.fNameList.get(currentRow));
		  this.setLname(this.lNameList.get(currentRow));
		  this.setSex(this.sexList.get(currentRow));
		  this.setSmoker((this.smokerList.get(currentRow) == "Y" ? true : false ));
		  this.setMarried(this.marriedList.get(currentRow));
		 
		  logger.debug("Customer Firstname:"+this.getFname());
		  
		  currentRow++;

		  /* return currentRow*/
		  return currentRow;
	}
	
	/* Get Database Connection */
	private void dbConnect() {
		if (db == null) {
			try {
				Class.forName("org.gjt.mm.mysql.Driver");
				db = DriverManager.getConnection("jdbc:mysql://localhost:3306/zeus?user=root&password=");
			}
			catch (Exception e) {
				logger.debug("Error Connecting to zeus DB: " + e.toString());
			}
		}
	}

	/* Accessor Methods */

	/* Last Name */
	public void setLname(String _lname) {
		lname = _lname;
	}
	public String getLname() {
		return lname;
	}

	/* First Name */
	public void setFname(String _fname) {
		fname = _fname;
	}
	public String getFname() {
		return fname;
	}

	/* Sex */
	public void setSex(String _sex) {
		sex = _sex;
	}
	public String getSex() {
		return sex;
	}

	/* Age */
	public void setAge(int _age) {
		age = _age;
	}
	public int getAge() {
		return age;
	}

	/* Number of Children */
	public void setChildren(int _children) {
		children = _children;
	}
	public int getChildren() {
		return children;
	}

	/* Spouse ? */
	public void setSpouse(boolean _spouse) {
		spouse = _spouse;
	}
	public boolean getSpouse() {
		return spouse;
	}

	/* Smoker ? */
	public void setSmoker(boolean _smoker) {
		smoker = _smoker;
	}
	public boolean getSmoker() {
		return smoker;
	}

	/* Status ("Customer saved...") */
	public void setStatus(String _msg) {
		status = _msg;
	}
	public String getStatus() {
		return "<br><center><font color=red>" + status + "</font></center>";
	}

	public void loadCustomer(String _id) {
		try {
			String sql = "select * from customer where id='" + _id + "'";
			Statement s = db.createStatement();
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				setLname(rs.getString("lname"));
				setFname(rs.getString("fname"));
				setSex(rs.getString("sex"));
				setAge(rs.getInt("age"));
				setChildren(rs.getInt("children"));
				setSpouse((rs.getString("married") == "Y") ? true : false);
				setSmoker((rs.getString("smoker") == "Y") ? true : false);
				setId(_id);
			}
			else {
				setStatus("Customer Does Not Exist.");
			}
		}
		catch (SQLException e) {
			System.out.println("Error loading customer: " + _id + " : " + e.toString());
		}
	}

	public boolean validateString(String _input) {
		char[] chars = _input.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			if(Character.isDigit(chars[i]))
				return false;
		}
		return true;
	}

	public boolean validateAge(int _age) {
		if (age < 1 || age > 100) {
			return false;
		}
		else {
			return true;
		}
	}

	public boolean validate() {
		errors.clear(); // Reset the errors hashtable

		if (!validateString(lname))
			errors.put("lname", "Last name must be all letters.");
		if (!validateString(fname))
			errors.put("fname", "First name must be all letters.");

		if (!validateAge(age))
			errors.put("age", "Age must be a numeric value between 1 and 100.");

		return (errors.isEmpty()) ? true : false;
	}

	public String getErrors() {

		StringBuffer errTable = new StringBuffer();
		if (!errors.isEmpty())
			errTable.append("<br><center><table border='1'>");

		Enumeration<String> errs = errors.elements();
		while (errs.hasMoreElements()) {
			errTable.append("<tr><td><font color=red>");
			errTable.append(errs.nextElement());
			errTable.append("</font></td></tr>");
		}

		if (!errors.isEmpty())
			errTable.append("</table></center>");

		return errTable.toString();
	}

	public String getField(String _field, int _part) {

		String err = null;
		String pre = "<font color=red>*";
		String post = "</font>";

		if (_part == FIELD_NAME) {
			if (_field.equals("lname")) {
				err = (String) errors.get("lname");
				if (err != null) {
					return pre + "Last Name: " + post;
				}
				else {
					return "Last Name: ";
				}
			}
			if (_field.equals("fname")) {
				err = (String) errors.get("fname");
				if (err != null) {
					return pre + "First Name: " + post;
				}
				else {
					return "First Name: ";
				}
			}
			if (_field.equals("sex")) {
				err = (String) errors.get("sex");
				if (err != null) {
					return pre + "Sex: " + post;
				}
				else {
					return "Sex: ";
				}
			}

			if (_field.equals("age")) return "Age: ";
			if (_field.equals("children")) return "Children: ";
			if (_field.equals("spouse")) return "Spouse ? ";
			if (_field.equals("smoker")) return "Smoker ? ";
		}

		if (_part == FIELD_VALUE) {
			if (_field.equals("lname")) return getLname();
			if (_field.equals("fname")) return getFname();
			if (_field.equals("sex")) return getSex();
			if (_field.equals("age")) return (Integer.toString(getAge()));
			if (_field.equals("children")) return (Integer.toString(getChildren()));
			if (_field.equals("spouse")) return ((getSpouse()) ? "true" : "false");
			if (_field.equals("smoker")) return ((getSmoker()) ? "true" : "false");
		}

		return "";
	}

	public void submit() {
		try {
			//StringBuffer sql = new StringBuffer(256);
			String sql;
			/*sql.append("UPDATE customer SET ");
		      sql.append("lname='").append(lname).append("', ");
		      sql.append("fname='").append(fname).append("', ");
		      sql.append("age=").append(age).append(", ");
		      sql.append("sex='").append(sex).append("', ");
		      sql.append("married='").append(spouse).append("', ");
		      sql.append("children=").append(children).append(", ");
		      sql.append("smoker='").append(smoker).append("' ");
		      sql.append("where id=").append(id).append("");*/

			sql= "INSERT INTO customer (lname,fname,age,sex,married,children,smoker) " + "VALUES ('"+lname+"','"+fname+"',"+age+",'"+sex+"','"+spouse+"',"+children+",'"+smoker+"')";

			//logger.debug("id:"+id);
			logger.debug("SQL is: "+sql);

			Statement s = db.createStatement();
			s.executeUpdate(sql);
			//s.executeUpdate(sql.toString());
		}
		catch (SQLException e) {
			System.out.println("Error saving customer: " + getId() + " : " + e.toString());
		}
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

}