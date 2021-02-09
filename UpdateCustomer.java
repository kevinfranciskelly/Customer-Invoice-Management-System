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
import java.text.SimpleDateFormat;
import java.util.Date;
import errorHandling.InvalidEmail;
import errorHandling.InvalidPhoneNumber;
import errorHandling.InvalidName;
import errorHandling.validateMethods;
import errorHandling.InvalidEmpty ;

public class UpdateCustomer extends JFrame{
	//JTextFields
	protected JTextField inputName = new JTextField() ;
	protected  JTextField inputAddr1 = new JTextField() ;
	protected  JTextField inputAddr2 = new JTextField() ; 
	protected  JTextField inputCity = new JTextField() ;
	protected JTextField inputCountry = new JTextField() ;
	protected JTextField inputPost = new JTextField() ;
	protected JTextField inputNumber = new JTextField() ;
	protected JTextField inputEmail = new JTextField() ;
	
	//JLabels
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Edit Customer Details</h1></html>") ;
	private JLabel labelName = new JLabel("<html><p style='font-size: 11px'>Full Name:</p></html>") ;
	private JLabel labelAddr1 = new JLabel("<html><p style='font-size: 11px'>Address Line 1:</p></html>") ;
	private JLabel labelAddr2 = new JLabel("<html><p style='font-size: 11px'>Address Line 2:</p></html>") ;
	private JLabel labelCity = new JLabel("<html><p style='font-size: 11px'>City:</p></html>") ;
	private JLabel labelCountry = new JLabel("<html><p style='font-size: 11px'>Country:</p></html>") ;
	private JLabel labelPost = new JLabel("<html><p style='font-size: 11px'>Postcode:</p></html>") ;
	private JLabel labelNumber = new JLabel("<html><p style='font-size: 11px'>Phone Number:</p></html>") ;
	private JLabel labelEmail = new JLabel("<html><p style='font-size: 11px'>E-mail:</p></html>") ;
	
	//JButtons
	private JButton saveButton = new JButton("Save Changes") ;
	private JButton backButton = new JButton("Go Back") ;
	
	//Constructor
	public UpdateCustomer(String theID) { //UpdateCustomer has a parameter to retrieve the selected row's customer ID
		super("Update a Customer") ;
		
		setLocation(500, 200);
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(saveButton) ;
		buttonPanel.add(backButton) ;
		
		//Adding Functionality to the Buttons
		saveButton.addActionListener(new ActionListener() //Inserting into SQL
		{
			public void actionPerformed(ActionEvent e) {
				//Variables from Forms
				boolean check = true ;
				String name = inputName.getText() ;
				String address1 = inputAddr1.getText() ;
				String address2 = inputAddr2.getText() ;
				String city = inputCity.getText() ;
				String country = inputCountry.getText() ;
				String post = inputPost.getText() ;
				String number = inputNumber.getText() ;
				String email = inputEmail.getText() ;
				Date theDate = new Date();
				String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(theDate);
				
				final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
				Connection connection = null;
				Statement statement = null;
				
				try {
					connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
					validateMethods vm = new validateMethods() ;
					
					statement = connection.createStatement() ;
					PreparedStatement pstat = connection.prepareStatement("UPDATE Customers SET FullName= ?, AddressLine1= ?, AddressLine2= ?, City = ?, Country = ?, PostCode= ?, Email= ?, PhoneNumber =? WHERE CustID= ?") ;
					
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
					
					vm.validatePhone(number); //Validate the phone number before adding it to the statement
					
					pstat.setString(8, number);
					
					pstat.setString(9, theID);
					pstat.executeUpdate() ;
				}
				catch(SQLException sqlException) {
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
							JOptionPane.showMessageDialog(null, "Entry edited");
							DisplayCustomers dc = new DisplayCustomers() ;
							dc.setVisible(true);
							setVisible(false) ;
						}
					}
					catch ( Exception exception ){

						exception.printStackTrace();

					}
				}
				
			}
		});
		
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayCustomers dc = new DisplayCustomers() ;
				dc.setVisible(true);
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
		thePanel.add(formLayout) ;
		thePanel.add(titlePanel) ;
		thePanel.add(formLayout) ;
		thePanel.add(buttonPanel) ;
		
		
		add(thePanel) ;

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
}

