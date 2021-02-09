//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package gui;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton; 
import javax.swing.JScrollPane;
import java.awt.Dimension; 
import javax.swing.SwingConstants;
import javax.swing.table.* ;

import java.awt.event.*;

public class DisplayCustomers  extends JFrame {
	//Arrays
	private Object[] columnNames = {"CustID", "FullName", "AddressLine1", "AddressLine2", "City", "Country", "PostCode", "Email", "PhoneNumber", "DateJoined"} ;
	private Object[][] entries ;
	
	//JButton
	private JButton deleteButton = new JButton("Delete Selected Entry") ;
	private JButton editButton = new JButton("Edit Selected Entry") ;
	private JButton homeButton = new JButton("Home Page") ;
	
	//JLabel
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Customer Table</h1></html>", SwingConstants.CENTER) ;
	
	//JScrollPane
	private JScrollPane theScroll ;
	
	//JTable and Default Table Model
	protected JTable data ;
	protected DefaultTableModel dm ; //These are set to protected as they will be accessed by the Update GUIs
	
	
	//Constructor
	public DisplayCustomers () {
		super("Display Customers") ;
		
		createCustomerData() ;
		
		//A Listener That will delete the selected entry, otherwise will display a message if no row has been selected
		deleteButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				int theRow = data.getSelectedRow() ;
				if (theRow == -1) { //data.getSelectedRow() will return -1 if no row has been selected
					JOptionPane.showMessageDialog(null, "Error: Row has not been selected");
				}
				else {
					int confirmValue = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected entry?") ;
					String custID = (String) data.getValueAt(theRow, 0) ; //Retrieve the ID of the entry to be deleted, which is in the first column
					if (confirmValue == JOptionPane.NO_OPTION) { //If No is selected
						JOptionPane.showMessageDialog(null, "Deletion Cancelled");
					}
					else if (confirmValue == JOptionPane.CANCEL_OPTION) { //If Cancel is selected
						
					}
					else  if (confirmValue == JOptionPane.YES_OPTION){ //If Yes is selected
						
						//SQL Deletion Code
						final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
						Connection connection = null;
						Statement statement = null;
						
						try {
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							
							statement = connection.createStatement() ;
							PreparedStatement pstat = connection.prepareStatement("DELETE FROM Customers WHERE CustID = ?;") ;
							pstat.setString(1, custID);
							pstat.executeUpdate() ;
							JOptionPane.showMessageDialog(null, "Entry Deleted") ;
							//Delete the selected row and update the table
							dm.removeRow(theRow);
							data.repaint();
							data.revalidate();
						
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
						}//End of Finally Statement
						
					}//End of Else If Statement
					
				}//End of Else Statement
				
			}
		}); //End Listener
		
		//A Listener that will open the edit customer page for the selected entry, otherwise displays a message if no entry is selected
		editButton.addActionListener(new ActionListener()  
		{
			public void actionPerformed(ActionEvent e) {
				int theRow = data.getSelectedRow() ;
				if (theRow == -1) {
					JOptionPane.showMessageDialog(null, "Error: No Row has been selected");
				}
				else {
					String id = (String) data.getValueAt(theRow, 0) ;
					String name = (String) data.getValueAt(theRow, 1) ;
					String line1 = (String) data.getValueAt(theRow, 2) ;
					String line2 = (String) data.getValueAt(theRow, 3) ;
					String city = (String) data.getValueAt(theRow, 4) ;
					String country = (String) data.getValueAt(theRow, 5) ;
					String postCode = (String) data.getValueAt(theRow, 6) ;
					String email = (String) data.getValueAt(theRow, 7) ;
					String phone = (String) data.getValueAt(theRow, 8) ;
					
					UpdateCustomer uc = new UpdateCustomer(id) ; //Initialize a Update Customer Object, passing the Customer ID
					
					uc.inputName.setText(name);
					uc.inputAddr1.setText(line1);
					uc.inputAddr2.setText(line2);
					uc.inputCity.setText(city);
					uc.inputCountry.setText(country);
					uc.inputPost.setText(postCode) ;
					uc.inputEmail.setText(email);
					uc.inputNumber.setText(phone);
					uc.setVisible(true);
					setVisible(false) ;
				}
				
				
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
		
		theScroll = new JScrollPane(data) ; //A Scrollbar for the table
		theScroll.setPreferredSize(new Dimension(1800, 115));
		
		JPanel thePanel = new JPanel() ; //The Panel that will house everything
		thePanel.setPreferredSize(new Dimension(1800, 350));
		thePanel.add(title) ;
		
		JPanel buttonPanel = new JPanel() ; //This Panel will hold the buttons
		buttonPanel.add(editButton) ;
		buttonPanel.add(deleteButton) ;
		buttonPanel.add(homeButton) ;
		
		thePanel.add(theScroll);
		thePanel.add(buttonPanel) ;
		
		add(thePanel) ;
		setLocation(70, 200) ;
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	//Method that retrieves data and places it in a field
	/**
	 * A method that retrieves the entire Customer table, and places it in the JTable
	 */
	public void createCustomerData() {
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT * FROM Customers") ;
			resultSet = pstat.executeQuery() ;
			ResultSetMetaData metaData = resultSet.getMetaData() ;
			
			int rowCount = 0 ;
			while (resultSet.next()) {
				rowCount ++ ; //Count the number of rows 
			}
			resultSet.first() ; //Reset to the first row
			int count ;
			int row  = 0;
			int col = 0;
			
			entries = new String[rowCount][metaData.getColumnCount()] ;
			
			if (rowCount == 0) { //If the SQL query yielded no entries
				dm = new DefaultTableModel(entries, columnNames) ;
				data = new JTable(dm) ;
			}
			else {
				for (count = 1 ; count <= metaData.getColumnCount() ; count ++) { //This for Loop enters the first entry into the array
					entries[row][col] = resultSet.getString(count) ;
					if (col == 9) {
						//Do not increase the col value to 10, otherwise an array out of bounds error occurs
					}
					else {
						col ++ ;
					}
				}
				row ++ ;
				col = 0 ;
				
				while (resultSet.next()) { //Continue until you cannot enter the next row 
					for (count = 1 ; count <= metaData.getColumnCount() ; count ++) { 
						entries[row][col] = resultSet.getString(count) ;
						if (col == 9) {
							//Do not increase the col value to 10, otherwise an array out of bounds error occurs
						}
						else {
							col ++ ;
						}
					}//End of For Statement
					row ++ ; //Change to the next row
					col = 0 ; //Reset the column tracker
				}
				dm = new DefaultTableModel(entries, columnNames) ;
				data = new JTable(dm) ;
			}//Else Statement
				
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
	
	
	public static void main(String[] args) {
		DisplayCustomers dc = new DisplayCustomers() ;
	}

}
