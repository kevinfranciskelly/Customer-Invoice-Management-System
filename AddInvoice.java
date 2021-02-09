//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import javax.swing.JTextField;
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
import errorHandling.validateMethods;
import errorHandling.InvalidInteger;
import errorHandling.InvalidDate;
import errorHandling.InvalidEmpty;

public class AddInvoice extends JFrame{
	//JComboBoxes
	private String[] arrProducts ;
	private JComboBox products ;
	
	private String[] arrCustomers ;
	private JComboBox customers ;
	
	private String[] arrDispatched = {"Yes", "No"} ;
	private JComboBox dispatched = new JComboBox(arrDispatched) ;
	
	//JTextFields
	private JTextField dateOrdered = new JTextField("yyyy-mm-dd") ;
	private JTextField quantity = new JTextField() ;
	
	//JButtons
	private JButton submitButton = new JButton("Submit Form") ;
	private JButton resetButton = new JButton("Reset Form") ;
	private JButton homeButton = new JButton("Home Page") ;
	
	//JLabel
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Enter Details for New Invoice</h1></html>") ;
	private JLabel product = new JLabel("<html><p style='font-size: 11px'>Product Name (Required)</p></html>") ;
	private JLabel customer = new JLabel("<html><p style='font-size: 11px'>Customer Name (Required)</p></html>") ;
	private JLabel QuanLabel = new JLabel("<html><p style='font-size: 11px'>Quantity (Required)</p></html>") ;
	private JLabel dateOrderedLabel = new JLabel("<html><p style='font-size: 11px'>Date Ordered (Required)</p></html>") ;
	private JLabel dispatchedLabel = new JLabel("<html><p style='font-size: 11px'>Dispatched? (Required)</p></html>") ;
	
	public AddInvoice () {
		super("Add an Invoice") ;
		
		submitButton.addActionListener(new ActionListener() //Inserting into SQL
				{
					public void actionPerformed(ActionEvent e) {
						final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
						Connection connection = null;
						Statement statement = null;
						ResultSet resultSet = null ;
						validateMethods vm  = new validateMethods();
						boolean check = true ;
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
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							statement = connection.createStatement() ;
							
							//Retrieve the Customer ID of the Customer Selected
							PreparedStatement pstat = connection.prepareStatement("SELECT CustID from Customers WHERE FullName= ?;");
							pstat.setString(1, theCust);
							resultSet = pstat.executeQuery() ;
							resultSet.first() ;
							custID = (int) resultSet.getObject(1) ;
							
							
							//Retrieve the Product ID of the Product Selected
							pstat = connection.prepareStatement("SELECT ProductID from Products WHERE ProductName= ? ;") ;
							pstat.setString(1, theProduct);
							resultSet = pstat.executeQuery() ;
							resultSet.first() ;
							productID = (int) resultSet.getObject(1) ;
							
							//Insert Statement
							pstat = connection.prepareStatement("INSERT INTO Invoices (CustID, ProductID, DateOrdered, Quantity, Dispatched) VALUES (?, ?, ?, ?, ?) ;");
							pstat.setInt(1, custID);
							pstat.setInt(2, productID);
							
							vm.validateDate(theDate); //Ensure the Date entered is valid before entering it into the statement
							
							pstat.setString(3, theDate);
							
							vm.isEmpty(theQuan);
							vm.validateInteger(theQuan); //Ensure the Quantity entered is valid before entering it into the statement
							
							pstat.setString(4, theQuan);
							pstat.setInt(5, dispatchVal);
							pstat.executeUpdate() ;
							
							
						}
						catch(SQLException sqlException) {
							sqlException.printStackTrace();
						}
						catch(InvalidDate id) {
							JOptionPane.showMessageDialog(null, id.getMessage());
							check = false ;
						}
						catch(InvalidInteger cv) {
							JOptionPane.showMessageDialog(null, cv.getMessage());
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
									JOptionPane.showMessageDialog(null, "Invoice Added");
									resetForm() ;
								}
								
							}
							catch ( Exception exception ){

								exception.printStackTrace();

							}
						}
						
					}
				});
		resetButton.addActionListener(new ActionListener() { ///Reset the form
			public void actionPerformed(ActionEvent e) {
				resetForm() ;
			}
		});
		
		homeButton.addActionListener(new ActionListener() //Return the user to the home page
		{
			public void actionPerformed(ActionEvent e) {
				HomePage hp = new HomePage() ;
				hp.setVisible(true);
				setVisible(false) ;
			}
				
		});
			getProductData() ;
			getCustomerData() ;
			products.setSelectedIndex(0);
			customers.setSelectedIndex(0);
			
			dispatched.setSelectedIndex(0);
			
			setLocation(500, 200);
			
			//Create a Panel for the Buttons
			JPanel buttonPanel = new JPanel() ;
			buttonPanel.add(submitButton) ;
			buttonPanel.add(resetButton) ;
			buttonPanel.add(homeButton) ;
			
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
	 * A method that gets the Product Names from the Products data, inserts it into an array and uses that array for the JComboBox
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
				resultSet.first() ; //Reset the count to the original position ;
				int index ;
				String temp ;
				count = 0 ;
				
				//Insert the first product name into the array, otherwise it will be skipped by the while loop
				temp = (String) resultSet.getObject(1) ;
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
			JOptionPane.showMessageDialog(null, "An Invoice cannot be created as there is currently no Products and/or Customers");
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
	 * A method that gets the Customer Names from the Customer data, inserts it into an array and uses that array for the JComboBox
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
				int index ;
				String temp ;
				count = 0 ;
				temp = (String) resultSet.getObject(1) ;
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
			JOptionPane.showMessageDialog(null, "An Invoice cannot be created as there is currently no Products and/or Customers");
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
	
	/**
	 * Set all fields to blank & reset the combo box fields
	 */
	public void resetForm() {
		products.setSelectedIndex(0);
		customers.setSelectedIndex(0);
		dateOrdered.setText("yyyy-mm-dd");
		quantity.setText("") ;
		dispatched.setSelectedIndex(0);
	}
	public static void main(String[] args) {
		AddInvoice ai = new AddInvoice() ;
	}
}

