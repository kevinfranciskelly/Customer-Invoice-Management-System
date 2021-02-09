//Student Name: Kevin Kelly
//Student ID: C00237615
//Course Code: CW_KCCYB_B
package errorHandling;

import java.text.SimpleDateFormat;
import java.util.Date;

public class validateMethods {
	InvalidPrice priceException ;
	InvalidEmail emailException ;
	InvalidPhoneNumber phoneException ;
	InvalidName nameException ;
	InvalidDate dateException ;
	InvalidEmpty emptyException ;
	InvalidInteger intException ;
	public validateMethods() {
		
	}
	
	//Validate Email Method
	/**
	 * 
	 * @param The text entered into the E-mail text field, to be tested for validity
	 * @throws InvalidEmail
	 */
	public void validateEmail(String theEmail) throws InvalidEmail { //A method that will determine if the e-mail is in the correct format
		int index ;
		index = theEmail.indexOf("@") ; //Check for an @ symbol
		if (index == -1) { //If there is no @ symbol present
			emailException = new InvalidEmail("Error: E-mail is not valid") ;
			throw emailException ;
		}
		else {
			if (index == 0) { //If there are no letters preceding the @ symbol
				emailException = new InvalidEmail("Error: E-mail is not valid") ;
				throw emailException ;
			}
		}
	}
	
	//Validate Phone Number
	/**
	 * 
	 * @param The text entered into the Phone Number text field, to be tested for validity
	 * @throws InvalidPhoneNumber
	 */
	public void validatePhone(String theNumber) throws InvalidPhoneNumber {
		int index ;
		boolean numCheck = true ; //A boolean that will turn false if the phone number contains letters
		for (index = 0 ; index < theNumber.length() ; index ++) {
			char value = theNumber.charAt(index) ;
			if ((value >= 48) && (value <= 57)) { //If the value is between the ASCII values of 0 and 9
				//Don't do anything
			}
			else {
				numCheck = false ;
				index = theNumber.length() ;
			}
		}//For Loop
		if (numCheck) {
			if (theNumber.length() > 11) { //If the phone number is too large
				phoneException = new InvalidPhoneNumber("Error: Phone number must be no bigger than 11 digits") ;
				throw phoneException ;
			}
		}
		else {
			phoneException = new InvalidPhoneNumber("Error: Phone number must be only digits") ;
			throw phoneException ;
		}
	}
	
	//Validate Name
	/**
	 * 
	 * @param The text entered into the Customer Name field, to be tested for validity
	 * @throws InvalidName
	 */
	public void validateName(String theName) throws InvalidName {
		int space ;
		space = theName.indexOf(" ") ;
		if (space == -1) { //If there are no spaces in the name
			nameException = new InvalidName("Error: Full Name must consist of a First and Last Name") ;
			throw nameException ;
		}
		String fName = theName.substring(0, space) ;
		String lName = theName.substring(space + 1) ;
		if (fName.equals("")) { //If there is no First Name
			nameException = new InvalidName("Error: Full Name must consist of a First and Last Name") ;
			throw nameException ;
		}
		else if (lName.equals("")){//If there is no Last Name 
			nameException = new InvalidName("Error: Full Name must consist of a First and Last Name") ;
			throw nameException ;
		}
	}
	
	//Validate Date
	/**
	 * 
	 * @param Text entered into the Date field, to be tested for validity
	 * @throws InvalidDate
	 */
	public void validateDate(String theDate) throws InvalidDate {
		dateException = new InvalidDate("Error: Date must be in the format yyyy-mm-dd") ;
		int firstdash = theDate.indexOf("-") ;
		int lastdash = theDate.lastIndexOf("-") ;
		if ((firstdash == -1) || (lastdash == -1)) { //If there is no dashes
			throw dateException ;
		}
		else if (firstdash == lastdash) { //If there is only one dash
			throw dateException ;
		}
		else {
			String year = theDate.substring(0, firstdash) ;
			String month = theDate.substring(firstdash + 1, lastdash) ;
			String day = theDate.substring(lastdash + 1) ;
			int index ;
			
			//Validate the year
			if ((year.length() < 4) || (year.length() > 4)) { //If the year is not presented as four digits
				throw dateException ;
			}
			else {
				for (index = 0 ; index < year.length() ; index ++) {
					if ((year.charAt(index) >= 48) && (year.charAt(index) <= 57)) {
						
					}
					else {
						throw dateException ;
					}
				}
			}
			
			//Validate the month
			if ((month.length() < 2) || (month.length() > 2)) { //If the month is not present as two digits
				throw dateException ;
			}
			else {
				for (index = 0 ; index < month.length() ; index ++) {
					if ((month.charAt(index) >= 48) && (month.charAt(index) <= 57)) {
						
					}
					else {
						throw dateException ;
					}
				}//For Loop
				int monthNum = Integer.parseInt(month) ;
				if ((monthNum < 01) || (monthNum > 12)) { //If the month is less than 1 or greater than 12
					throw dateException ;
				}
			}//Else for Month
			
			
			//Validate the Day
			if ((day.length() < 2) || (day.length() > 2)) { //If the day is not presented as two digits
				throw dateException ;
			}
			else {
				for (index = 0 ; index < day.length() ; index ++) {
					if ((day.charAt(index) >= 48) && (day.charAt(index) <= 57)) {
						
					}
					else {
						throw dateException ;
					}
			}
			int dayNum = Integer.parseInt(day) ;
			int monthNum = Integer.parseInt(month) ;
			switch(monthNum) {
				case 9://The months with 30 days in them are Sep, Apr, June and Nov
				case 4:
				case 5:
				case 11:
				if ((dayNum < 01) || (dayNum > 30)) {
					throw dateException ;
				}
				break ;
				
				case 2:
				int yearNum = Integer.parseInt(year) ;
				if (yearNum % 4 != 0) {//If the year is not a leap year
					if ((dayNum < 01) || (dayNum > 28)) {
						throw dateException ;
					}
				}
				else if (yearNum % 4 == 0){ //If it is a leap year
					if ((dayNum < 01) || (dayNum > 29)) {
						throw dateException ;
					}
				}
				break ;
				
				default: //Remaining months in the year have 31 days in them
				if ((dayNum < 01) || (dayNum > 31)) {
					throw dateException ;
				}
				break ;
				
			}
		}//Else Statement
		
	}
}
	//Validate Price
	/**
	 * 
	 * @param Text entered into the Price field, to be tested for validity
	 * @throws CustomValidate
	 */
	public void validatePrice(String theNum) throws InvalidPrice {
		priceException = new InvalidPrice("Error: A valid number must be entered") ;
		int dot = theNum.indexOf(".") ;
		if (dot != -1) { //If a decimal is present
			int index ;
			String firstHalf = theNum.substring(0, dot) ;
			String secondhalf = theNum.substring(dot + 1) ;
			for (index = 0 ; index < firstHalf.length() ; index ++) {
				if ((firstHalf.charAt(index) >= 48) && (firstHalf.charAt(index) <= 57)) {
					
				}
				else {
					throw priceException ;
				}
			}//For Loop
			if (secondhalf.length() > 2) { //If the decimal section of the number is greater than 2
				throw priceException ;
			}
			else {
				for (index = 0 ; index < secondhalf.length() ; index ++) {
					if ((secondhalf.charAt(index) >= 48) && (secondhalf.charAt(index) <= 57)) {
						
					}
					else {
						throw priceException ;
					}
				}//For Loop
			}
			
		}//If Statement
		else { //If a decimal is not present
			int index ;
			for (index = 0 ; index < theNum.length() ; index ++) {
				if ((theNum.charAt(index) >= 48) && (theNum.charAt(index) <= 57)) {
					
				}
				else {
					throw priceException ;
				}
			}//For Loop
		}
	}//End Method
	
	//Validate Integer
	/**
	 * 
	 * @param The number to be validated
	 * @throws InvalidInteger
	 */
	public void validateInteger(String theNum) throws InvalidInteger {
		intException = new InvalidInteger("Error: An invalid integer has been entered") ;
		for (int index = 0 ; index < theNum.length() ; index ++) {
			if ((theNum.charAt(index) >= 48) && (theNum.charAt(index) <= 57)) {
				
			}
			else {
				throw intException ;
			}
		}
	}
	
	/**
	 * 
	 * @param Text taken from a Text Field, to determine if it is empty
	 * @throws InvalidEmpty
	 */
	public void isEmpty(String entry) throws InvalidEmpty {
		emptyException = new InvalidEmpty("Error: One or More required fields have not been filled") ;
		int count = 0 ;
		int length = entry.length() ;
		
		for (int index = 0 ; index < length ; index ++) {
			if (entry.charAt(index) == ' ') {
				count ++ ; //Increment the count everytime a space is found
			}
		}//For Loop
		if (count == length) { //If the text field consists of only spaces, throw the exception
			throw emptyException ;
		}
	}
	

}
