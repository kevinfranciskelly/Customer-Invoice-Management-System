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

public class DisplayProducts  extends JFrame {
	//Arrays
	private Object[] columnNames = {"ProductID", "ProductName", "Category", "Platform", "Age Rating", "Description", "ReleaseDate", "QuantityinStock", "Price"} ;
	private Object[][] entries ;
	
	//JButton
	private JButton deleteButton = new JButton("Delete Selected Entry") ;
	private JButton editButton = new JButton("Edit Selected Entry") ;
	private JButton homeButton = new JButton("Home Page") ;
	
	//JLabel
	private JLabel title = new JLabel("<html><h1 style='color:blue'>Product Table</h1></html>", SwingConstants.CENTER) ;
	
	//JScrollPane
	private JScrollPane theScroll ;
	
	//JTable and Default Table Model
	protected JTable data ;
	protected DefaultTableModel dm ; //These are set to protected as they will be accessed by the Update GUIs
	
	
	//Constructor
	public DisplayProducts () {
		super("Display Products") ;
		
		createProductTable() ;
		
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
			
					if (confirmValue == JOptionPane.NO_OPTION) { //If No is selected
						JOptionPane.showMessageDialog(null, "Deletion Cancelled");
					}
					else if (confirmValue == JOptionPane.CANCEL_OPTION) { //If Cancel is selected
						
					}
					else  if (confirmValue == JOptionPane.YES_OPTION){ //If Yes is selected
						
						//SQL Deletion Code
						String productID = (String) data.getValueAt(theRow, 0) ;
						final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
						Connection connection = null;
						Statement statement = null;
						
						try {
							connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
							
							statement = connection.createStatement() ;
							PreparedStatement pstat = connection.prepareStatement("DELETE FROM Products WHERE ProductID = ?;") ;
							pstat.setString(1, productID);
							pstat.executeUpdate() ;
							JOptionPane.showMessageDialog(null, "Entry Deleted") ;
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
		
		//A Listener that will open the edit product page for the selected entry, otherwise displays a message if no entry is selected
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
					String category = (String) data.getValueAt(theRow, 2) ;
					String platform = (String) data.getValueAt(theRow, 3) ;
					String ageRating = (String) data.getValueAt(theRow, 4) ;
					String description = (String) data.getValueAt(theRow, 5) ;
					String releaseDate = (String) data.getValueAt(theRow, 6) ;
					String stockLevel = (String) data.getValueAt(theRow, 7) ;
					String price = (String) data.getValueAt(theRow, 8) ;
					
					UpdateProducts dp = new UpdateProducts(id) ;
					
					dp.inputName.setText(name);
					dp.inputCategory.setText(category);
					dp.inputPlat.setText(platform);
					dp.inputDesc.setText(description);
					dp.inputDate.setText(releaseDate);
					dp.inputQuantity.setText(stockLevel);
					dp.inputPrice.setText(price);
					
					int index ;
					for (index = 0 ; index < dp.ratings.length ; index ++) {
						if (ageRating.equals(dp.ratings[index])) {
							dp.inputAge.setSelectedIndex(index);
							index = dp.ratings.length ;
						}
					}
					
					dp.setVisible(true);
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
		pack();
		setLocation(70, 200) ;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	//Method that retrieves data and places it in a field
	/**
	 * A method that retrieves the entire Products Table, and inserts it into the JTable
	 */
	public void createProductTable() {
		final String DATABASE_URL = "jdbc:mysql://localhost/CIMS";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null ;
		
		try {
			connection = DriverManager.getConnection(DATABASE_URL, "root", "rosie2") ;
			
			statement = connection.createStatement() ;
			PreparedStatement pstat = connection.prepareStatement("SELECT * FROM Products") ;
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
			if (rowCount == 0) { //Create an empty table if no entries are found
				dm = new DefaultTableModel(entries, columnNames) ;
				data = new JTable(dm) ;
			}
			else {
				for (count = 1 ; count <= metaData.getColumnCount() ; count ++) { //This for Loop enters the first entry into the array
					entries[row][col] = resultSet.getString(count) ;
					if (col == 9) {
						
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
							
						}
						else {
							col ++ ;
						}
					}
					row ++ ;
					col = 0 ;
				}
				dm = new DefaultTableModel(entries, columnNames) ;
				data = new JTable(dm) ;
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
	}//End Method
	
	
	public static void main(String[] args) {
		DisplayProducts dp = new DisplayProducts() ;
	}

}

