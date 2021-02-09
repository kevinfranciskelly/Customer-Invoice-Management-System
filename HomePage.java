//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import java.awt.BorderLayout;
import java.awt.event.ActionListener ;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.Icon ;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.lang.NullPointerException;

public class HomePage extends JFrame{
	//JButtons
	private JButton addProductButton = new JButton("Add a Product") ;
	private JButton addCustomerButton = new JButton("Add a Customer") ;
	private JButton addInvoicesButton = new JButton("Add an Invoice") ;
	private JButton viewProductButton = new JButton("View Products") ;
	private JButton viewCustomerButton = new JButton("View Customers") ;
	private JButton viewInvoicesButton = new JButton("View Invoices");
	
	//Image
	private Icon griffin = new ImageIcon(getClass().getResource("Griffin.png")) ;
	JLabel griffinLabel = new JLabel(griffin) ;
	
	//JLabel
	private JLabel text = new JLabel("<html><h1 style='color:blue'>Welcome to the Blue Griffin Games Management System</h1>"
									+ "<br/><h2 style='text-align:center'>Select an option from the list below</h2></html>") ;
	
	//Constructor
	public HomePage() {
		super("Home Page") ;
		
		setLocation(500, 200);
		
		//Adding Functionality to the Buttons
		addCustomerButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "You clicked on the Customer button");
				AddCustomer addC = new AddCustomer() ; //Brings up the add product page
				addC.setVisible(true);
				setVisible(false) ;
			}
		});
		
		addProductButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
			//	JOptionPane.showMessageDialog(null, "You clicked on the Product button");
				AddProduct addP = new AddProduct() ; //Brings up the add product page
				addP.setVisible(true);
				setVisible(false) ;
			}
		});
		
		addInvoicesButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				boolean check = true ;
				try {
					
					final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null ;
					connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
					statement = connection.createStatement() ;
					PreparedStatement pstat = connection.prepareStatement("SELECT ProductName from Products ;") ;
					resultSet = statement.executeQuery("SELECT ProductName from Products ;") ;
					resultSet.first() ;
					
					//Attempt to retrieve the very first value of the first entry in Products. If a null is returned throw the exception
					String temp  = (String) resultSet.getObject(1) ;
					
					pstat = connection.prepareStatement("SELECT FullName from Customers ;") ;
					resultSet = statement.executeQuery("SELECT FullName from Customers ;") ;
					resultSet.first() ;
					
					//Attempt to retrieve the very first value of the first entry in Products. If a null is returned throw the exception
					
					temp  = (String) resultSet.getObject(1) ;
				}
				catch (SQLException exception) { //There are no products or customers
					JOptionPane.showMessageDialog(null, "There is currently no products and/or customers to create invoices");
					check = false ;
				}
				if (check) {
					AddInvoice addI = new AddInvoice() ;
					addI.setVisible(true) ;
					setVisible(false) ;
				}
			}
		});
		
		viewProductButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				DisplayProducts dp = new DisplayProducts() ;
				dp.setVisible(true);
				setVisible(false) ;
			}
		});
		
		viewCustomerButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				DisplayCustomers dc = new DisplayCustomers() ;
				dc.setVisible(true);
				setVisible(false) ;
			}
		});
		
		viewInvoicesButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				DisplayInvoices di = new DisplayInvoices() ;
				di.setVisible(true);
				setVisible(false) ;
			}
		});
				
		
		//Create a Panel for the text
		JPanel textPanel = new JPanel() ;
		textPanel.add(text) ;
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.setLayout(new GridLayout(3,3, 70, 20));
	
		buttonPanel.add(addProductButton) ;		
		buttonPanel.add(viewProductButton) ;
		buttonPanel.add(addCustomerButton) ;
		buttonPanel.add(viewCustomerButton) ;
		buttonPanel.add(addInvoicesButton) ;
		buttonPanel.add(viewInvoicesButton) ;
		
		//Create a Panel for the Frame
		JPanel thePanel = new JPanel() ;
		thePanel.setPreferredSize(new Dimension(800, 650)) ;
		thePanel.setLayout(new BorderLayout());
		thePanel.add(griffinLabel, BorderLayout.PAGE_START) ;
		thePanel.add(textPanel, BorderLayout.CENTER) ;
		thePanel.add(buttonPanel, BorderLayout.PAGE_END) ;
		
		add(thePanel) ;
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/*public void checkProducts(boolean check) {
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT ProductName from Products ;") ;
			resultSet = statement.executeQuery("SELECT ProductName from Products ;") ;
			int count = 0 ;
			while (resultSet.next()) {
				count ++ ;
			}
			if (count == 0) { //If Java can advance to the next row (i.e there is at least one row), return true
				check = true ;
			}
			else {
				check = false ;
			}
			
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
	}

	public void checkCusts(boolean check) {
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT FullName from Customers ;") ;
			resultSet = pstat.executeQuery() ;
			int count = 0 ;
			while (resultSet.next()) {
				count ++ ;
			}
			if (count == 0) { //If Java can advance to the next row (i.e there is at least one row), return true
				check = true ;
			}
			else {
				check = false ;
			}
			
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
		
	}*/

	public static void main(String[] args) {
		HomePage home = new HomePage() ;
		
	}
}
