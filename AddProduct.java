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
import javax.swing.JComboBox;

import errorHandling.InvalidPrice;
import errorHandling.InvalidDate;
import errorHandling.InvalidEmpty;
import errorHandling.InvalidInteger;
import errorHandling.validateMethods;

public class AddProduct extends JFrame{
	//JTextFields
	private JTextField inputName = new JTextField() ;
	private JTextField inputCategory = new JTextField() ;
	private JTextField inputPlat = new JTextField() ;
	private JTextField inputDesc = new JTextField() ;
	private JTextField inputPrice = new JTextField() ;
	private JTextField inputQuantity = new JTextField() ;
	private JTextField inputDate = new JTextField("yyyy-mm-dd") ;
	
	
	//JLables
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Enter Details for New Product</h1></html>") ;
	private JLabel labelName = new JLabel("<html><p style='font-size: 11px'>Product Name (Required):</p></html>") ;
	private JLabel labelCategory = new JLabel("<html><p style='font-size: 11px'>Category (Required):</p></html>") ;
	private JLabel labelPlat = new JLabel("<html><p style='font-size: 11px'>Platform (Required):</p></html>") ;
	private JLabel labelDesc = new JLabel("<html><p style='font-size: 11px'>Description (Required):</p></html>") ;
	private JLabel labelPrice = new JLabel("<html><p style='font-size: 11px'>Product Price (Required):</p></html>") ;
	private JLabel labelQuantity = new JLabel("<html><p style='font-size: 11px'>Quantity in Stock (Required):</p></html>") ;
	private JLabel labelDate = new JLabel("<html><p style='font-size: 11px'>Release Date (Required):</p></html>") ;
	private JLabel labelAge = new JLabel("<html><p style='font-size: 11px'>Age Rating (Required):</p></html>") ;
	
	//JComboBox
	private String ratings[] = {"3+", "7+", "12+", "16+", "18+"} ; //Array of Age Ratings for Games
	private JComboBox inputAge = new JComboBox(ratings) ;
	
	//JButtons
	private JButton submitButton = new JButton("Submit Form") ;
	private JButton resetButton = new JButton("Reset Form") ;
	private JButton homeButton = new JButton("Home Page") ;
	
	public AddProduct () {
		super("Product Page") ;
		
		setLocation(500, 200);
		
		//Adding Functionality to the Buttons
		
		submitButton.addActionListener(new ActionListener() //Inserting into SQL
				{
					public void actionPerformed(ActionEvent e) {
						boolean check = true ;
						//Variables from Forms
						String name = inputName.getText() ;
						String category = inputCategory.getText() ;
						String platform = inputPlat.getText() ;
						String description = inputDesc.getText() ;
						String ageRating = inputAge.getSelectedItem().toString() ;
						String price = inputPrice.getText() ;
						String quantity = inputQuantity.getText() ;
						String date = inputDate.getText() ;
						
						final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
						Connection connection = null;
						Statement statement = null;
						
						try {
							validateMethods vm  = new validateMethods();
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							statement = connection.createStatement() ;
							PreparedStatement pstat = connection.prepareStatement("INSERT INTO Products (ProductName, Category, Platform, AgeRating, Description, ReleaseDate, QuantityInStock, Price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)") ;
							
							vm.isEmpty(name); //Ensure the product name field is filled before adding it to the statement
							
							pstat.setString(1, name);
							
							vm.isEmpty(category); //Ensure the category field is filled before adding it to the statement
							
							pstat.setString(2, category);
							
							vm.isEmpty(platform); //Ensure the platform field is filled before adding it to the statement
							
							vm.isEmpty(platform); //Check if the Platform is empty before adding it to the statement
							pstat.setString(3, platform);
							pstat.setString(4, ageRating);
							
							vm.isEmpty(description); //Check if the description is empty before adding it to the statement
							
							pstat.setString(5, description);
							
							vm.validateDate(date); //Validate the Date before entering it into the statement
							
							pstat.setString(6, date);
							
							vm.isEmpty(quantity);
							vm.validateInteger(quantity); //Ensure the quantity is a valid number before adding it to the statement
							
							pstat.setString(7, quantity);
							
							vm.isEmpty(price);
							vm.validatePrice(price); //Ensure the price is a valid number before adding it to the statement
							
							pstat.setString(8, price);
							
							pstat.executeUpdate() ;
						}
						catch(SQLException sqlException) {
							sqlException.printStackTrace();
						}
						catch(InvalidDate id) {
							JOptionPane.showMessageDialog(null, id.getMessage());
							check = false ;
						}
						catch (InvalidPrice ip) {
							JOptionPane.showMessageDialog(null, ip.getMessage());
							check = false ;
						}
						catch (InvalidInteger ii) {
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
									JOptionPane.showMessageDialog(null, "Product Added");
									resetForm() ;
								}	
							}
							catch ( Exception exception ){

								exception.printStackTrace();

							}
						}
						
					}
				});
		homeButton.addActionListener(new ActionListener() //Return the user to the product page
			{
				public void actionPerformed(ActionEvent e) {
					HomePage homePag = new HomePage() ; //Brings up the add product page
					homePag.setVisible(true);
					setVisible(false) ;
				}
			});
		
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetForm() ;
			}
		});
		
		
		inputAge.setSelectedIndex(0); //Set the Drop Down Box to the first value
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(submitButton) ;
		buttonPanel.add(resetButton) ;
		buttonPanel.add(homeButton) ;
		
		
		//Create the Panel for the Input Fields and their Labels.
		JPanel formLayout = new JPanel() ;
		
		/*BoxLayout theLayout3 = new BoxLayout(formLayout, BoxLayout.Y_AXIS) ;
		formLayout.setLayout(theLayout3); 
		formLayout.add(namePanel) ;
		formLayout.add(catPanel) ;*/
		formLayout.setLayout(new GridLayout(8,2, 150, 20));
		
		inputName.setPreferredSize(new Dimension(230, 28)); //Setting a size for one field affects all for some reason
		
		formLayout.add(labelName) ;
		formLayout.add(inputName) ;
		formLayout.add(labelCategory) ;
		formLayout.add(inputCategory) ;
		formLayout.add(labelPlat) ;
		formLayout.add(inputPlat) ;
		formLayout.add(labelDesc) ;
		formLayout.add(inputDesc) ;
		formLayout.add(labelAge) ;
		formLayout.add(inputAge) ;
		formLayout.add(labelPrice) ;
		formLayout.add(inputPrice) ;
		formLayout.add(labelQuantity) ;
		formLayout.add(inputQuantity) ;
		formLayout.add(labelDate) ;
		formLayout.add(inputDate) ;
		
		//Create a Panel for the Title
		JPanel titlePanel = new JPanel() ;
		titlePanel.add(title) ;
		
		//Create a Panel for the Frame
		JPanel thePanel = new JPanel() ;
		thePanel.setPreferredSize(new Dimension(750, 600));
		//BoxLayout boxLayout = new BoxLayout(thePanel, BoxLayout.Y_AXIS);
		//thePanel.setLayout(boxLayout);
		thePanel.add(titlePanel) ;
		thePanel.add(formLayout) ;
		thePanel.add(buttonPanel) ;
		
		add(thePanel) ;

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
	}
	/**
	 * A method that sets all fields to blank and the Combo Box to the first value
	 */
	public void resetForm() {
		inputName.setText("");
		inputCategory.setText("");
		inputPlat.setText("");
		inputDesc.setText("");
		inputPrice.setText("");
		inputQuantity.setText("");
		inputDate.setText("yyyy-mm-dd");
		inputAge.setSelectedIndex(0);
	}
	
	public static void main(String[] args) {
		AddProduct ap = new AddProduct() ;
	}
	
}
