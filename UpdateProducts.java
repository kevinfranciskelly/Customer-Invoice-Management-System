//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
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
import javax.swing.JComboBox;

import errorHandling.* ;

public class UpdateProducts extends JFrame{
	//JTextFields
	protected JTextField inputName = new JTextField() ;
	protected JTextField inputCategory = new JTextField() ;
	protected JTextField inputPlat = new JTextField() ;
	protected JTextField inputDesc = new JTextField() ;
	protected JTextField inputPrice = new JTextField() ;
	protected JTextField inputQuantity = new JTextField() ;
	protected JTextField inputDate = new JTextField("yyyy/mm/dd") ;
	
	
	//JLables
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Edit Product Details</h1></html>") ;
	private JLabel labelName = new JLabel("<html><p style='font-size: 11px'>Product Name:</p></html>") ;
	private JLabel labelCategory = new JLabel("<html><p style='font-size: 11px'>Category:</p></html>") ;
	private JLabel labelPlat = new JLabel("<html><p style='font-size: 11px'>Platform:</p></html>") ;
	private JLabel labelDesc = new JLabel("<html><p style='font-size: 11px'>Description:</p></html>") ;
	private JLabel labelPrice = new JLabel("<html><p style='font-size: 11px'>Product Price:</p></html>") ;
	private JLabel labelQuantity = new JLabel("<html><p style='font-size: 11px'>Quantity in Stock:</p></html>") ;
	private JLabel labelDate = new JLabel("<html><p style='font-size: 11px'>Release Date:</p></html>") ;
	private JLabel labelAge = new JLabel("<html><p style='font-size: 11px'>Age Rating:</p></html>") ;
	
	//JComboBox
	protected String ratings[] = {"3+", "7+", "12+", "16+", "18+"} ; //Array of Age Ratings for Games
	protected JComboBox inputAge = new JComboBox(ratings) ; //Protected as it will be accessed by another class
	
	//JButtons
	private JButton saveButton = new JButton("Save Changes") ;
	private JButton backButton = new JButton("Go Back") ;
	
	public UpdateProducts (String theID) {
		super("Edit Products") ;
		
		setLocation(500, 200);
		
		//Adding Functionality to the Buttons
		
		saveButton.addActionListener(new ActionListener() //Inserting into SQL
				{
					public void actionPerformed(ActionEvent e) {
						//Variables from Forms
						boolean check = true ;
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
							validateMethods vm = new validateMethods() ;
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							statement = connection.createStatement() ;
							PreparedStatement pstat = connection.prepareStatement("UPDATE Products SET ProductName= ?, Category= ?, Platform= ?, AgeRating = ?, Description= ?, ReleaseDate = ?, QuantityInStock = ?, Price = ? WHERE ProductID = ?") ;
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
							
							pstat.setString(9, theID);
							
							pstat.executeUpdate() ;
						}
						catch(SQLException sqlException) {
							sqlException.printStackTrace();
						}
						catch(InvalidDate id) {
							JOptionPane.showMessageDialog(null, id.getMessage());
							check = false ;
						}
						catch (InvalidPrice cv) {
							JOptionPane.showMessageDialog(null, cv.getMessage());
							check = false ;
						} 
						catch (InvalidInteger ii) {
							JOptionPane.showMessageDialog(null, ii.getMessage());
						}
						catch (InvalidEmpty ie) {
							JOptionPane.showMessageDialog(null, ie.getMessage());
						}
						finally {
							try {
								statement.close();
								connection.close() ;
								if (check) {
									JOptionPane.showMessageDialog(null, "Entry Edited");
								}
								DisplayProducts dp = new DisplayProducts() ;
								dp.setVisible(true);
								setVisible(false) ;
							}
							catch ( Exception exception ){

								exception.printStackTrace();

							}
						}
						
					}
				});
		
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayProducts dp = new DisplayProducts() ;
				dp.setVisible(true);
				setVisible(false) ;
				
			}
		});
		
		
		inputAge.setSelectedIndex(0);
		
		//Create a Panel for the Buttons
		JPanel buttonPanel = new JPanel() ;
		buttonPanel.add(saveButton) ;
		buttonPanel.add(backButton) ;
		
		
		//Create the Panel for the Input Fields and their Labels.
		JPanel formLayout = new JPanel() ;

		formLayout.setLayout(new GridLayout(10,2, 150, 20));
		
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
		thePanel.add(titlePanel) ;
		thePanel.add(formLayout) ;
		thePanel.add(buttonPanel) ;
		
		add(thePanel) ;

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		
	}
	
}

