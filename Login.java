import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import java.util.regex.*;

public class Login 
{
	private static String USERDATA_FILE = "UserData.txt";
	private static ArrayList<String> users = new ArrayList<>();
	private static ArrayList<UserData> UsersList = new ArrayList<>();
	private static int CurrentUserData;
	private static UserData user;
	
	private static Scanner input = new Scanner(System.in);
	static Date date = new Date();
	static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //fix the format of date and time 
	
	public static void main (String [] args)
	{
		//login
		user = new UserData ("","","");
		boolean login = false;
		String username;
		while (login != true)
		{
			//define existed users or new users
			loadUserData();
			System.out.println("Please enter Username or new user enter 'new' to create: ");
			username = input.nextLine();
			
			if ("new".equalsIgnoreCase(username)) 
			{
				createNewUser(user,input);
				login = true;
			}
			else if (isValidUser(username))
			{
				System.out.println("Please enter your password: ");
				String password = input.nextLine();
				if (isValidPassword(username,password))
				{
					System.out.println("Login successful!");
					System.out.println();
					user.setUsername(username);
					login = true;
				}
				else
				{
					System.out.println("Invalid password!");
					System.out.println();
				}
			}
			else
			{
				System.out.println("Invalid username!");
				System.out.println("Please enter valid username!");
			}
		}
		
		//choosing function
		int option = 0;
		while (option != 6)
		{
			System.out.println("Please select a function");
			System.out.println("1. Add Booking");
			System.out.println("2. Update Booking");
			System.out.println("3. Cancel Booking");
			System.out.println("4. View Booking History");
			System.out.println("5. Edit User Profile");
			System.out.println("6. Quit");
			option = input.nextInt();
						
			switch (option)
			{
				case 1: 
					AddBooking(); 
					break;
				case 2: 
					UpdateBooking(); 
					break;
				case 3: 
					CancelBooking(); 
					break;
				case 4: 
					ViewBooking(); 
					break;
				case 5: 
					EditProfile(); 
					break;
				case 6: 
					System.out.println("Goodbye.Have a nice day!"); 
					break;		
				default: 
					System.out.println("Invalid option."); break;
				}
			}	
			input.close();
	}
	
	private static void AddBooking()
	{
		//Add booking
		Scanner input = new Scanner(System.in);
		System.out.println(format.format(date));
		String username = user.getUsername(); //get username
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //fix the format of date and time
		
		System.out.println("Please enter the date and time for booking(DD/MM/YYYY HH:MM): ");
		String DateChosen = input.nextLine();
		
		try //record
		{
			Date BookingDate = format.parse(DateChosen);
			BookingDetails booking = new BookingDetails(username, BookingDate);
            List<BookingDetails> Bookings= loadBookingsFromFile ("BookingDetails.txt");
            System.out.println("Your booking date will be " + format.format(BookingDate));
            Bookings.add(booking);
            recordBookingDetail(Bookings);
        } 
		catch (ParseException e) 
		{
            System.out.println("Invalid date format.");
        }
		
	}

	private static void UpdateBooking()
	{
		// Get the user's username
		 String username = user.getUsername();

		 // Load the booking data from the file
		 List<BookingDetails> bookings = loadBookingsFromFile("BookingDetails.txt");

		 // Filter bookings to show only the user's upcoming bookings
		 List<BookingDetails> userBookings = new ArrayList<>();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		 for (BookingDetails booking : bookings) 
		 {
			 if (username.equals(booking.getUsername())) 
			 {
				 userBookings.add(booking);
			 }
		 }

		 // Display the user's bookings
		 if (!userBookings.isEmpty()) 
		 {
			 System.out.println("Bookings for " + username + ":");
			 for (int i = 0; i < userBookings.size(); i++) 
			 {
				 BookingDetails booking = userBookings.get(i);
				 System.out.println((i + 1) + ": " + dateFormat.format(booking.getBookingDate()));
			 }

			 Scanner scanner = new Scanner(System.in);

			 while (true) 
			 {
				 // Ask the user to choose which booking to update
				 System.out.print("Enter the number of the booking to update (Enter 0 to update nothing): ");
				 int updateNumber = scanner.nextInt();

				 if (updateNumber > 0 && updateNumber <= userBookings.size()) 
				 {
					 // Calculate the index to update
					 int updateIndex = updateNumber - 1;
		 
					 BookingDetails updatedBooking = userBookings.get(updateIndex);

					 // Ask the user for the new date and time of the booking
					 System.out.print("Enter the new date and time for the booking (dd/MM/yyyy HH:mm): ");
					 scanner.nextLine(); // Consume newline left-over
					 String newDateTimeStr = scanner.nextLine();

					 try 
					 {
						 Date newbookingDate = dateFormat.parse(newDateTimeStr);
						 updatedBooking.setBookingDate(newbookingDate);
						 bookings.set(bookings.indexOf(updatedBooking), updatedBooking); // Update the booking in the list
						 saveBookingsToFile(bookings); // Save the updated bookings
						 System.out.println("Booking updated successfully.");
						 break; // Exit the loop once the booking is updated
					 } 
					 catch (ParseException e) 
					 {
						 System.out.println("Invalid date and time format. Please enter again!");
					 }
				 } 	
		 		else if (updateNumber == 0) 
		 		{
		 			System.out.println("No booking updated.");
		 			break; // Exit the loop if the user chooses not to update anything
		 		}
		 		else 
		 		{
		 			System.out.println("Invalid booking number. Please re-enter.");
		 		}
			 }
		 }
		 else 
		 {
			 System.out.println("You have no upcoming bookings to update.");
		 }
		 // Ask the user to press ENTER to return to the menu
		 System.out.println("Please press ENTER to return to the menu...");
		 input.nextLine();
		 input.nextLine();// Wait for ENTER key
	}
	
	private static void CancelBooking()
	{	
		//get user information
    	String username = user.getUsername();
    	
    	// take information from the file
    	List<BookingDetails> bookings = loadBookingsFromFile("BookingDetails.txt");
    	Scanner input = new Scanner(System.in);
       	List<BookingDetails> userBookings = new ArrayList<>();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
 
    	for (BookingDetails booking : bookings) 
    	{
        	if (username.equals(booking.getUsername())) 
        	{
            	userBookings.add(booking);
        	}
    	}
 	
    	if (!userBookings.isEmpty()) //to make sure the bookings is found
    	{
        	System.out.println("Bookings made by " + username + ":");
        	for (int i = 0; i < userBookings.size(); i++) {
            	BookingDetails booking = userBookings.get(i);
            	System.out.println((i + 1) + ": " + dateFormat.format(booking.getBookingDate()));
        	}
 	
        	while (true) 
        	{
            	System.out.print("Enter the number of the booking to cancel (Enter 0 to cancel nothing): ");
            	int cancel = input.nextInt();
 	
            	if (cancel > 0 && cancel <= userBookings.size()) 
            	{
                	int cancelIndex = cancel - 1;
                	BookingDetails CancelledBooking = userBookings.remove(cancelIndex);
                	System.out.println("Booking on " + dateFormat.format(CancelledBooking.getBookingDate()) + " cancelled.");
                	bookings.remove(CancelledBooking); 
                	saveBookingsToFile(bookings); 
                	break; 
            	} 	
            	else if (cancel == 0) 
            	{
                	System.out.println("No booking cancelled.");
                	break;
            	} else 
            	{
                System.out.println("Invalid Booking Number! Please enter a valid number.");
            	}
        	}
    	} else {
        	System.out.println("You have no any bookings to cancel.");
    	}
 
        	System.out.println("Please press ENTER to return to the menu...");
        	input.nextLine();
        	input.nextLine();

	}
	
	private static void ViewBooking()
	{
		// take information from file
		List<BookingDetails> Bookings = loadBookingsFromFile("BookingDetails.txt");

        String username = user.getUsername();
        
        for (BookingDetails booking: Bookings) 
        {
            if (username.equals(booking.getUsername())) 
            {
                Bookings.add(booking);
            }
        }

        if (!Bookings.isEmpty()) 
        {
            System.out.println("Booking for " + username + ":");
            for (BookingDetails booking: Bookings) 
            {
                String formatDate = format.format(booking.getBookingDate());
                System.out.println("Date/Time: " + formatDate);
            }
        } else 
        {
            System.out.println("Booking for " + username + " is not found.");
        }

        System.out.println("Please press enter to return to the menu...");
        input.nextLine();
        input.nextLine(); 
	}
	
	private static void EditProfile()
	{
		String newUsername;
		String newPassword;
		String newEmail;
        String username = user.getUsername();
        loadUserData();
        UsersList = loadUsersList();
        CurrentUserData = loadCurrentUserData(username);
        UserData users = UsersList.get(CurrentUserData);
        
        // choose the part of profile to edit
        System.out.println("Which profile information you would like to edit");
        System.out.println("1. Username");
        System.out.println("2. Password");
        System.out.println("3. Email");
        System.out.println("4. Back to menu");
        int option = 0;
        option = input.nextInt();
        Scanner input = new Scanner(System.in);
        
        if (option == 1) //edit username
        {
        	System.out.println("Please enter new Username: ");
        	newUsername = input.nextLine();
        	 if (!newUsername.equals(username)) 
        	 {
                 user.setUsername(newUsername);
                 System.out.println("Your username have edited successfully!");
        	 }
        	 else 
        	 {
        		 System.out.println("You entered the old Username.Please enter again!\n");
        	 }
        }	 
        	 
        else if (option == 2) // edit password
        {
        	System.out.println("Please enter new Password: ");
        	newPassword = input.nextLine();
        	if (!newPassword.equals("")) 
        	{
                users.setPassword(newPassword);
                System.out.println("Your password had changed successfully!");
        	}
        	else 
        	{
        		System.out.println("You entered the old Password.Please enter again!\n");
        	}
        }
        else if (option == 3) // edit email
        {
        	System.out.println("Please enter new email: ");
        	newEmail = input.nextLine();
        	
        	//fix the pattern of the email
        	String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
        	Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(newEmail);
            
            if (matcher.matches() || newEmail.equals("")) 
            {
                if (!newEmail.equals("")) 
                {
                	users.setEmail(newEmail);
                	System.out.println("Your email had updated successfully!");
                }
                else 
                {
                	System.out.print("You entered the old Email.Please enter again!\n");
                }
            }
            else
            {
            	System.out.println("You entered an invalid Email.Please enter agaain!");
            }
        }
        else if (option == 4) // back to menu
        {
        	System.out.println("Please press enter to return to the menu...");
            input.nextLine();
            input.nextLine();
        }

        else 
        {
        	System.out.println("Invalid option!Please enter again!");
        }
        writeUserData();
	}
	
	private static void createNewUser(UserData user, Scanner input)
	{
		// create new account
    	String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$"; // fix the format of email
        String newUsername, newPassword, newEmail;
        boolean valid = false;
        System.out.print("Enter new username: "); //input new username
	    newUsername = input.nextLine();
	    System.out.print("Enter new password: "); //input new password
	    newPassword = input.nextLine();
	    
	    do 
	    {    
	        System.out.print("Enter email address: "); //input new email
	        newEmail = input.nextLine();
	        if (isValidEmail(newEmail, emailPattern))
	        {
	        	valid = true;
	        }
	        else
	        {
	            System.out.println("Email format is invalid.");
	        }
	    }while(valid !=true);
        String newUserAdded = newUsername + "\t" + newPassword + "\t" + newEmail;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERDATA_FILE, true))) 
        {
            writer.write(newUserAdded);  // write information into file
            writer.newLine();
            users.add(newUserAdded);
            System.out.println("User created successfully.");
        } 
        catch (IOException e) 
        {
            System.err.println("Error creating a new user: " + e.getMessage());
        }
        user.setUsername(newUsername);
    }	
	
	private static void loadUserData() 
	{
		//read file
    	users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERDATA_FILE))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                users.add(line);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error reading user data file: " + e.getMessage());
        }
    }
	
	private static List<BookingDetails> loadBookingsFromFile(String filename) 
	{
	    List<BookingDetails> Bookings = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
	    {
	        String line;
	        while ((line = reader.readLine()) != null) 
	        {
	            String[] parts = line.split("\t");
	            if (parts.length == 3) 
	            {
	                String storedUsername = parts[0].trim();
	                String dates = parts[1].trim();

	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	                Date date = dateFormat.parse(dates);

	                Bookings.add(new BookingDetails(storedUsername, date));
	            }
	        }
	    } catch (IOException | ParseException e) {
	        System.err.println("Error Booking Detail: " + e.getMessage());
	    }

	    return Bookings;
	}
	private static boolean isValidUser(String username) 
	{
        for (String user : users) 
        {
            String[] parts = user.split("\t");
            if (parts.length == 3 && parts[0].equals(username)) 
            {
                return true;
            }
        }
        return false;
    }
	
	private static boolean isValidPassword(String username, String password) 
	{
        for (String user : users) 
        {
            String[] parts = user.split("\t");
            if (parts.length == 3 && parts[0].equals(username) && parts[1].equals(password)) 
            {
                return true;
            }
        }
        return false;
    }
        
	private static boolean isValidEmail(String email,String pattern) 
	{
		Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(email);
        return matcher.matches();
	}    
	
	private static ArrayList<UserData> loadUsersList() 
	{
		ArrayList<UserData> ul= new ArrayList<UserData>();
		for (int i=0;i<users.size();i++)
		{
			String[] userSplit=users.get(i).split("\t");
			UserData user = new UserData(userSplit[0],userSplit[1],userSplit[2]);
			ul.add(user);
		}
       return ul;
    }
       
	private static void saveBookingsToFile(List<BookingDetails> Bookings) 
	{
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("BookingDetails.txt"))) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        for (BookingDetails booking: Bookings) {
	            String enter = booking.getUsername() + "" + dateFormat.format(booking.getBookingDate());
	            writer.write(enter);
	            writer.newLine();
	        }
	        System.out.println("Booking Data is saved!");
	    } 
	    catch (IOException e) 
	    {
	        System.err.println("Error Booking Data: " + e.getMessage());
	    }
	}
	
	private static void recordBookingDetail(List<BookingDetails> Bookings) 
	{
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("BookingDetails.txt"))) 
	    {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        for (BookingDetails booking: Bookings) 
	        {
	            String enter = booking.getUsername() + "_" + dateFormat.format(booking.getBookingDate());
	            writer.write(enter);
	            writer.newLine();
	        }
	        System.out.println("Booking details saved to file.");
	    }
	    catch (IOException e) 
	    {
	        System.err.println("Error Booking Details: " + e.getMessage());
	    }
	}
	
	private static int loadCurrentUserData(String username) 
	{
		for (int i=0;i<UsersList.size();i++) 
		{
			if(UsersList.get(i).getUsername().equals(username))
			{
				return i;
			}
		}
       return -1; //throw Error
    }
	    
	private static void writeUserData()
	{
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERDATA_FILE, false))) {
    		for (int i=0;i<users.size();i++) {
    			if (i == CurrentUserData) {
    				writer.write(UsersList.get(i).getFileString());
    			}
    			else 
    			{
    				writer.write(users.get(i));
    			}
	            writer.newLine();
    		}
            System.out.println("User updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating a user: " + e.getMessage());
        }
    }
}			