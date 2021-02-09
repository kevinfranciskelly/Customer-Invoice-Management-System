//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import javax.swing.JTextField;

import errorHandling.InvalidEmail;
import errorHandling.InvalidPhoneNumber;
import errorHandling.InvalidName;
import errorHandling.validateMethods;
import errorHandling.InvalidEmpty ;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCustomer extends JFrame{
	//JTextFields
	private JTextField inputName = new JTextField() ;
	private JTextField inputAddr1 = new JTextField() ;
	private JTextField inputAddr2 = new JTextField() ; 
	private JTextField inputCity = new JTextField() ;
	private JTextField inputCountry = new JTextField() ;
	private JTextField inputPost = new JTextField() ;
	private JTextField inputNumber = new JTextField() ;
	private JTextField inputEmail = new JTextField() ;
	
	//JLabels
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Enter Details for New Customer</h1></html>") ;
	private JLabel labelName = new JLabel("<html><p style='font-size: 11px'>Full Name (Required):</p></html>") ;
	private JLabel labelAddr1 = new JLabel("<html><p style='font-size: 11px'>Address Line 1 (Required):</p></html>") ;
	private JLabel labelAddr2 = new JLabel("<html><p style='font-size: 11px'>Address Line 2 (Required):</p></html>") ;
	private JLabel labelCity = new JLabel("<html><p style='font-size: 11px'>City (Required):</p></html>") ;
	private JLabel labelCountry = new JLabel("<html><p style='font-size: 11px'>Country (Required):</p></html>") ;
	private JLabel labelPost = new JLabel("<html><p style='font-size: 11px'>Postcode:</p></html>") ;
	private JLabel labelNumber = new JLabel("<html><p style='font-size: 11px'>Phone Number:</p></html>") ;
	private JLabel labelEmail = new JLabel("<html><p style='font-size: 11px'>E-mail (Required):</p></html>") ;
	
	//JButtons
	private JButton submitButton = new JButton("Submit Form") ;
	private JButton resetButton = new JButton("Reset Form") ;
	private JButton homeButton = new JButton("Home Page") ;
	
	//Constructor
	public AddCustomer() {
		super("Add a Customer") ;
		
		setLocation(500, 200);
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(submitButton) ;
		buttonPanel.add(resetButton) ;
		buttonPanel.add(homeButton) ;
		
		//Adding Functionality to the Buttons
		homeButton.addActionListener(new ActionListener() //Return to the Home Page
		{
			public void actionPerformed(ActionEvent e) {
				HomePage homePag = new HomePage() ; //Brings up the add product page
				homePag.setVisible(true);
				setVisible(false) ;
			}
		});
		submitButton.addActionListener(new ActionListener() //Inserting into SQL
		{
			public void actionPerformed(ActionEvent e) {
				//Variables from Forms
				boolean check = true ; //A boolean that will remain true if no catches occur
				String name = inputName.getText() ;
				String address1 = inputAddr1.getText() ;
				String address2 = inputAddr2.getText() ;
				String city = inputCity.getText() ;
				String country = inputCountry.getText() ;
				String post = inputPost.getText() ;
				String number = inputNumber.getText() ;
				String email = inputEmail.getText() ;
				
				//Get today's date, and format it to "yyyy-mm-dd" format so it will be accepted by SQL
				Date theDate = new Date();
				String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(theDate);
				
				final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
				Connection connection = null;
				Statement statement = null;
				
				try {
					connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
					validateMethods vm = new validateMethods() ;
					
					statement = connection.createStatement() ;
					PreparedStatement pstat = connection.prepareStatement("INSERT INTO Customers (FullName, AddressLine1, AddressLine2, City, Country, PostCode, Email, PhoneNumber, DateJoined) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)") ;
					
					vm.validateName(name); //Verify the name before adding it to the statement
					
					pstat.setString(1, name);
					
					vm.isEmpty(address1); //Ensure the address line is filled before adding it to the statement
					
					pstat.setString(2, address1);
					
					vm.isEmpty(address2); //Ensure the address line is filled before adding it to the statement
					
					pstat.setString(3, address2);
					
					vm.isEmpty(city); //Ensure the city line is filled before adding it to the statement
					
					pstat.setString(4, city);
					
					vm.isEmpty(country); //Ensure the country line is filled before adding it to the statement
					
					pstat.setString(5, country);
					
					pstat.setString(6, post);
					
					vm.validateEmail(email); //Validate the email before adding it to the statement
					
					pstat.setString(7, email);
					
					if (number.equals("")) {
						number = "0" ;
					}
					else {
						vm.validatePhone(number); //Validate the phone number before adding it to the statement
					}
					
					pstat.setString(8, number);
					pstat.setString(9, modifiedDate);
					pstat.executeUpdate() ;
				}
				catch(SQLException sqlException) { //Catch for SQL Errors
					sqlException.printStackTrace(); 
				}
				catch(InvalidEmail ie) { //Catch for Invalid Email
					JOptionPane.showMessageDialog(null, ie.getMessage());
					check = false ;
				}
				catch(InvalidPhoneNumber ip) { //Catch for Invalid Phone Number
					JOptionPane.showMessageDialog(null, ip.getMessage());
					check = false ;
				}
				catch(InvalidName in) { //Catch for Invalid Name
					JOptionPane.showMessageDialog(null, in.getMessage());
					check = false ;
				}
				catch(InvalidEmpty ie) {
					JOptionPane.showMessageDialog(null, ie.getMessage());
					check = false ;
				}
				finally {
					try {
						statement.close();
						connection.close() ;
						if (check) {
							JOptionPane.showMessageDialog(null, "Customer Added");
							resetForm() ; //Reset the form
						}
						
					}
					catch ( Exception exception ){

						exception.printStackTrace();

					}
				}
				
			}
		});
		
		resetButton.addActionListener(new ActionListener() { //Set all fields to blank
			public void actionPerformed(ActionEvent e) {
				resetForm() ;
			}
		});
		
		homeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				HomePage hp = new HomePage() ;
				hp.setVisible(true);
				setVisible(false) ;
			}
				
		});
		
		//Create the Panel for the Input Fields and their Labels
		JPanel formLayout = new JPanel() ;
		formLayout.setLayout(new GridLayout(8,2, 150, 20));
		
		inputName.setPreferredSize(new Dimension(230, 28));
		
		formLayout.add(labelName) ;
		formLayout.add(inputName) ;
		formLayout.add(labelAddr1) ;
		formLayout.add(inputAddr1) ;
		formLayout.add(labelAddr2) ;
		formLayout.add(inputAddr2) ;
		formLayout.add(labelCity) ;
		formLayout.add(inputCity) ;
		formLayout.add(labelCountry) ;
		formLayout.add(inputCountry) ;
		formLayout.add(labelPost) ;
		formLayout.add(inputPost) ;
		formLayout.add(labelNumber) ;
		formLayout.add(inputNumber) ;
		formLayout.add(labelEmail) ;
		formLayout.add(inputEmail) ;
		
		
		//Create a Panel for the Title
		JPanel titlePanel = new JPanel() ;
		titlePanel.add(title) ;
		
		//Create a Panel for the Frame
		JPanel thePanel = new JPanel() ;
		thePanel.setPreferredSize(new Dimension(750, 600));
		thePanel.add(titlePanel) ;
		thePanel.add(formLayout) ;
		thePanel.add(buttonPanel) ;
		
		
		add(thePanel) ;

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * A method that will set all entries back to blank
	 */
	public void resetForm() { //A method that will reset the form
		inputName.setText("");
		inputAddr1.setText("");
		inputAddr2.setText("");
		inputCity.setText("");
		inputCountry.setText("");
		inputPost.setText("");
		inputNumber.setText("");
		inputEmail.setText("");
	}
	
	public static void main(String[] args) {
		AddCustomer addCust = new AddCustomer() ;
	}
}
