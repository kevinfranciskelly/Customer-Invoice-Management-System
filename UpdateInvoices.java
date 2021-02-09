//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import javax.swing.JTextField;

import errorHandling.validateMethods;
import errorHandling.InvalidInteger;
import errorHandling.InvalidDate;
import errorHandling.InvalidEmpty;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.Icon ;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComboBox;

public class UpdateInvoices extends JFrame{
	//JComboBoxes
	protected String[] arrProducts ;
	protected JComboBox products ;
	
	protected String[] arrCustomers ;
	protected JComboBox customers ;
	
	protected String[] arrDispatched = {"Yes", "No"} ;
	protected JComboBox dispatched = new JComboBox(arrDispatched) ;
	
	//JTextFields
	protected JTextField dateOrdered = new JTextField("yyyy-mm-dd") ;
	protected JTextField quantity = new JTextField() ;
	
	//JButtons
	private JButton submitButton = new JButton("Save Changes") ;
	private JButton backButton = new JButton("Go Back") ;
	
	//JLabel
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Edit Invoice Details</h1></html>") ;
	private JLabel product = new JLabel("<html><p style='font-size: 11px'>Product Name</p></html>") ;
	private JLabel customer = new JLabel("<html><p style='font-size: 11px'>Customer Name</p></html>") ;
	private JLabel QuanLabel = new JLabel("<html><p style='font-size: 11px'>Quantity</p></html>") ;
	private JLabel dateOrderedLabel = new JLabel("<html><p style='font-size: 11px'>Date Ordered</p></html>") ;
	private JLabel dispatchedLabel = new JLabel("<html><p style='font-size: 11px'>Dispatched?</p></html>") ;
	
	public UpdateInvoices (String theID) { //The parameter is the Invoice ID of the entry to be edited
		super("Update Invoice") ;
		
		submitButton.addActionListener(new ActionListener() //Inserting into SQL
				{
					public void actionPerformed(ActionEvent e) {
						boolean check = true ;
						final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
						Connection connection = null;
						Statement statement = null;
						ResultSet resultSet = null ;
						String theCust = (String) customers.getSelectedItem() ;
						String theProduct = (String) products.getSelectedItem() ;
						String theDate = dateOrdered.getText() ;
						String theQuan = quantity.getText(); 
						String dispatch = (String) dispatched.getSelectedItem() ;
						int dispatchVal = 0 ;
						if (dispatch.equals("Yes")) {
							dispatchVal = 1 ;
						}
						int productID ;
						int custID  ;
						
						try {
							validateMethods vm = new validateMethods() ;
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							statement = connection.createStatement() ;
							PreparedStatement pstat = connection.prepareStatement("SELECT CustID from Customers WHERE FullName= ?;");
							pstat.setString(1, theCust);
							resultSet = pstat.executeQuery() ;
							resultSet.first() ;
							custID = (int) resultSet.getObject(1) ;
							
							pstat = connection.prepareStatement("SELECT ProductID from Products WHERE ProductName= ? ;") ;
							pstat.setString(1, theProduct);
							resultSet = pstat.executeQuery() ;
							resultSet.first() ;
							productID = (int) resultSet.getObject(1) ;
							
							pstat = connection.prepareStatement("UPDATE Invoices SET CustID = ?, ProductID = ?, DateOrdered = ?, Quantity = ?, Dispatched = ? WHERE InvoiceID = ?;");
							pstat.setInt(1, custID);
							pstat.setInt(2, productID);
							
							vm.validateDate(theDate); //Validate the Date before entering it into the statement
							pstat.setString(3, theDate);
							
							vm.isEmpty(theQuan);
							vm.validateInteger(theQuan); //Validate the Quantity before entering it into the statement
							pstat.setString(4, theQuan);
							pstat.setInt(5, dispatchVal);
							pstat.setString(6, theID) ;
							pstat.executeUpdate() ;
							
							
						}
						catch(SQLException sqlException) {
							sqlException.printStackTrace();
						}
						catch(InvalidDate id) {
							JOptionPane.showMessageDialog(null, id.getMessage());
							check = false ;
						}
						catch(InvalidInteger ii) {
							JOptionPane.showMessageDialog(null, ii.getMessage());
							check = false ;
						}
						catch (InvalidEmpty ie) {
							JOptionPane.showMessageDialog(null, ie.getMessage());
							check = false ;
						}
						finally {
							try {
								statement.close();
								connection.close() ;
								if (check) {
									JOptionPane.showMessageDialog(null, "Entry Edited");
									DisplayInvoices di = new DisplayInvoices() ;
									di.setVisible(true);
									setVisible(false) ;
								}
								
							}
							catch ( Exception exception ){

								exception.printStackTrace();

							}
						}
						
					}
				});
		
		getProductData() ;
		products.setSelectedIndex(0);
		
		getCustomerData() ;
		customers.setSelectedIndex(0);
		
		dispatched.setSelectedIndex(0);
		
		setLocation(500, 200);
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(submitButton) ;
		buttonPanel.add(backButton) ;
		
		//Form Layout
		JPanel formLayout = new JPanel() ;
		
		formLayout.setLayout(new GridLayout(6,2, 150, 20));
		dateOrdered.setPreferredSize(new Dimension(230, 28));
		
		formLayout.add(product) ;
		formLayout.add(products) ;
		formLayout.add(customer) ;
		formLayout.add(customers) ;
		formLayout.add(dateOrderedLabel) ;
		formLayout.add(dateOrdered) ;
		formLayout.add(QuanLabel) ;
		formLayout.add(quantity) ;
		formLayout.add(dispatchedLabel) ;
		formLayout.add(dispatched) ;
		
		
		JPanel thePanel = new JPanel() ;
		thePanel.setPreferredSize(new Dimension(750, 400));
		thePanel.add(title) ;
		thePanel.add(formLayout) ;
		thePanel.add(buttonPanel) ;
		
		add(thePanel) ;

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//A method that gets the data Products data, inserts it into an array and uses that array for the JComboBox
	/**
	 * A method that retrieves all of the product names from the Products table, and inserts the names into the JComboBox
	 */
	public void getProductData() {
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT ProductName from Products ;") ;
			resultSet = statement.executeQuery("SELECT ProductName from Products ;") ;
			ResultSetMetaData metaData = resultSet.getMetaData();
			
			int count = 0 ;
			while (resultSet.next()) {
				count ++ ;
			}
			arrProducts = new String[count] ;
			resultSet.first() ; //Reset the pointer to the original position ;
			
			String temp ;
			
			count = 0 ;
			temp = (String) resultSet.getObject(1) ; //Retrieve the first name
			arrProducts[count] = temp ;
			count ++ ;
			
			while (resultSet.next()) {
				temp = (String) resultSet.getObject(1) ;
				arrProducts[count] = temp ;
				count ++ ;
			}
			products = new JComboBox(arrProducts) ;
			
		}
		catch(SQLException sqlException) {
			sqlException.printStackTrace();
		}
		finally {
			try {
				statement.close();
				connection.close() ;
			}
			catch ( Exception exception ){

				exception.printStackTrace();

			}
		}
	}//End Method
	
	//A method that gets the customer data and inserts it into an array
	/**
	 * A method that retrieves all of the customer names from the Customer table, and inserts the names into the JComboBox
	 */
	public void getCustomerData() { 
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT FullName from Customers ;") ;
			resultSet = statement.executeQuery("SELECT FullName from Customers ;") ;
			ResultSetMetaData metaData = resultSet.getMetaData();
			
			int count = 0 ;
			while (resultSet.next()) {
				count ++ ;
			}
			arrCustomers = new String[count] ;
			resultSet.first() ; //Reset the count to the original position ;

			String temp ;
			count = 0 ;
			temp = (String) resultSet.getObject(1) ; //Retrieve the first name
			arrCustomers[count] = temp ;
			count ++ ;
			
			while (resultSet.next()) {
				temp = (String) resultSet.getObject(1) ;
				arrCustomers[count] = temp ;
				count ++ ;
			}
			customers = new JComboBox(arrCustomers) ;
			
		}
		catch(SQLException sqlException) {
			sqlException.printStackTrace();
		}
		finally {
			try {
				statement.close();
				connection.close() ;
			}
			catch ( Exception exception ){

				exception.printStackTrace();

			}
		}
			
	}//End Method
	
}

