package com.jada.xml.databaseUpgrade;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import org.apache.log4j.Logger;

import com.ibm.wsdl.util.IOUtils;

public class DatabaseUpgradeProcedure {
    Logger logger = Logger.getLogger(DatabaseUpgradeProcedure.class);
    
    static public void createDefaultShippingRegion(Connection connection) throws SQLException {
		String sql = "insert  " +
					 "into    shipping_region " +
					 "        (published, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, shipping_region_name, site_id, system_record) " +
					 "values  ('Y', 'admin', now(), 'admin', now(), 'default', ?, 'Y')";
		PreparedStatement insertStatement = connection.prepareStatement(sql);
    	
		Statement select = connection.createStatement();
		sql = "select  site_id " +
			  "from    site " + 
			  "where   site_id != '_system' " +
			  "and     system_record = 'Y'";
		ResultSet result = select.executeQuery(sql);
		while (result.next()) {
			String siteId = result.getString("site_id");
			
			sql = "select  count(*) " +
				  "from    shipping_region " +
				  "where   site_id = '" + siteId + "' " +
				  "and     system_record = 'Y'";
			Statement countStatement = connection.createStatement();
			ResultSet shippingRegionResult = countStatement.executeQuery(sql);
			shippingRegionResult.first();
			int count = shippingRegionResult.getInt(1);
			if (count == 0) {
				insertStatement.setString(1, siteId);
				insertStatement.executeUpdate();
			}
		}
    }
    
	static public void createCustomerClass(Connection connection) throws SQLException {
		String sql = "insert  " +
					 "into    customer_class " +
					 "        (cust_class_name, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, site_id, system_record) " +
					 "values  ('Regular', 'admin', now(), 'admin', now(), ?, 'Y')";
		PreparedStatement insertStatement = connection.prepareStatement(sql);
		
		Statement select = connection.createStatement();
		sql = "select  site_id " +
			  "from    site " + 
			  "where   site_id != '_system' " +
			  "and     system_record = 'Y'";
		ResultSet result = select.executeQuery(sql);
		while (result.next()) {
			String siteId = result.getString("site_id");
			
			sql = "select  count(*) " +
				  "from    customer_class " +
				  "where   site_id = '" + siteId + "'";
			Statement countStatement = connection.createStatement();
			ResultSet customerClassResult = countStatement.executeQuery(sql);
			customerClassResult.first();
			int count = customerClassResult.getInt(1);
			if (count == 0) {
				insertStatement.setString(1, siteId);
				insertStatement.executeUpdate();
			}
		}
	}
	
	static public void dropReport(Connection connection) throws SQLException {
		String sql = "drop table Report";
		try {
			connection.createStatement().executeUpdate(sql);
		}
		catch (Exception e) {
		}
	}
	
	static public void loadReportOrders(Connection connection) throws SQLException, IOException {
		loadReport(connection, "Orders", "Orders by sub-sites");
	}
	
	static public void loadReport(Connection connection, String reportName, String reportDesc) throws SQLException, IOException {
		InputStream stream = connection.getClass().getResourceAsStream("/com/jada/xml/report/" + reportName + ".xml");
		String reportString = IOUtils.getStringFromReader(new InputStreamReader(stream));

		String sql = "insert  " +
		 "into    report " +
		 "        (report_name, report_desc, report_text, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, site_id, system_record) " +
		 "values  (?,?,?,?,?,?,?,?,?)";
		PreparedStatement insertStatement = connection.prepareStatement(sql);
		insertStatement.setString(1, reportName);
		insertStatement.setString(2, reportDesc);
		insertStatement.setString(3, reportString);
		insertStatement.setString(4, "admin");
		insertStatement.setDate(5, new Date(System.currentTimeMillis()));
		insertStatement.setString(6, "admin");
		insertStatement.setDate(7, new Date(System.currentTimeMillis()));
		insertStatement.setString(8, "default");
		insertStatement.setString(9, "Y");
		insertStatement.execute();
	}
	
    static public void copyReports(Connection connection) throws SQLException {
		String sql = "insert  " +
					 "into    report " +
					 "        (report_name, report_desc, report_text, last_run_by, last_run_datetime, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, site_id, system_record) " +
					 "values  (?, ?, ?, null, null, 'admin', now(), 'admin', now(), ?, 'Y')";
		PreparedStatement insertStatement = connection.prepareStatement(sql);
    	
		Statement select = connection.createStatement();
		sql = "select  site_id " +
			  "from    site " + 
			  "where   site_id != '_system' " +
			  "and     system_record != 'Y'";
		ResultSet result = select.executeQuery(sql);
		while (result.next()) {
			String siteId = result.getString("site_id");
			
			sql = "select  * " +
				  "from    report " + 
				  "where   site_id = 'default' " +
				  "and     system_record = 'Y'";
			ResultSet reportSet = connection.createStatement().executeQuery(sql);
			while (reportSet.next()) {
				String reportName = reportSet.getString("report_name");
				sql = "select  count(*) " +
					  "from    report " +
					  "where   site_id = '" + siteId + "' " +
					  "and     report_name = ?";
				PreparedStatement countStatement = connection.prepareStatement(sql);
				countStatement.setString(1, reportName);
				ResultSet reportResult = countStatement.executeQuery();
				reportResult.first();
				int count = reportResult.getInt(1);
				if (count == 0) {
					insertStatement.setString(1, reportSet.getString("report_name"));
					insertStatement.setString(2, reportSet.getString("report_desc"));
					insertStatement.setString(3, reportSet.getString("report_text"));
					insertStatement.setString(4, siteId);
					insertStatement.executeUpdate();
				}
			}
			reportSet.close();
		}
    }
    
    static public void copyImportExport(Connection connection) throws SQLException {
		String sql = "insert  " +
					 "into    ie_profile_header " +
					 "        (ie_profile_header_name, ie_profile_type, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, site_id, system_record) " +
					 "values  (?, ?, 'admin', now(), 'admin', now(), ?, 'Y')";
		PreparedStatement insertHeaderStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    	
		sql = "insert  " +
			 "into    ie_profile_detail " +
			 "        (ie_profile_field_name, ie_profile_field_value, ie_profile_group_index, ie_profile_group_name, ie_profile_position, rec_create_by, rec_create_datetime, rec_update_by, rec_update_datetime, seq_num, ie_profile_header_id) " +
			 "values  (?, ?, ?, ?, ?, 'admin', now(), 'admin', now(), ?, ?)";
		PreparedStatement insertDetailStatement = connection.prepareStatement(sql);

		Statement select = connection.createStatement();
		sql = "select  site_id " +
			  "from    site " + 
			  "where   site_id != '_system' " +
			  "and     system_record != 'Y'";
		ResultSet result = select.executeQuery(sql);
		while (result.next()) {
			String siteId = result.getString("site_id");
			
			sql = "select  * " +
				  "from    ie_profile_header " + 
				  "where   site_id = 'default' " +
				  "and     system_record = 'Y'";
			ResultSet ieProfileSet = connection.createStatement().executeQuery(sql);
			while (ieProfileSet.next()) {
				String ieProfileHeaderName = ieProfileSet.getString("ie_profile_header_name");
				String ieProfileType = ieProfileSet.getString("ie_profile_type");
				sql = "select  count(*) " +
					  "from    ie_profile_header " +
					  "where   site_id = '" + siteId + "' " +
					  "and     ie_profile_header_name = ? " +
					  "and     ie_profile_type = ?";
				PreparedStatement countStatement = connection.prepareStatement(sql);
				countStatement.setString(1, ieProfileHeaderName);
				countStatement.setString(2, ieProfileType);
				ResultSet ieProfileResult = countStatement.executeQuery();
				ieProfileResult.first();
				int count = ieProfileResult.getInt(1);
				if (count == 0) {
					insertHeaderStatement.setString(1, ieProfileSet.getString("ie_profile_header_name"));
					insertHeaderStatement.setString(2, ieProfileSet.getString("ie_profile_type"));
					insertHeaderStatement.setString(3, siteId);
					insertHeaderStatement.executeUpdate();
					
					ResultSet genKeySet = insertHeaderStatement.getGeneratedKeys();
					genKeySet.next();
					String ieProfileHeaderId = genKeySet.getString(1);
					sql = "select  * " +
						  "from    ie_profile_detail " + 
						  "where   ie_profile_header_id = '" + ieProfileSet.getShort("ie_profile_header_id") + "'";
					ResultSet ieProfileDetailSet = connection.createStatement().executeQuery(sql);
					while (ieProfileDetailSet.next()) {
						insertDetailStatement.setString(1, ieProfileDetailSet.getString("ie_profile_field_name"));
						insertDetailStatement.setString(2, ieProfileDetailSet.getString("ie_profile_field_value"));
						insertDetailStatement.setString(3, ieProfileDetailSet.getString("ie_profile_group_index"));
						insertDetailStatement.setString(4, ieProfileDetailSet.getString("ie_profile_group_name"));
						insertDetailStatement.setString(5, ieProfileDetailSet.getString("ie_profile_position"));
						insertDetailStatement.setString(6, ieProfileDetailSet.getString("seq_num"));
						insertDetailStatement.setString(7, ieProfileHeaderId);
						insertDetailStatement.executeUpdate();
					}
				}
			}
			ieProfileSet.close();
		}
    }
}
