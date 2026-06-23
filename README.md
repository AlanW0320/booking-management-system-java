# Booking Management System (Java)

A simple console-based booking management system built with core OOP principles in Java. It supports user account creation/login and basic booking operations (create, update, view, cancel) with file-based persistence — suitable as a learning/example project for Object-Oriented Application Development.

## Features
- User login and new user registration
- Create a new booking (date/time input)
- Update an existing booking
- Cancel a booking
- View booking history for the current user
- Edit user profile (username, password, email)
- File-based persistence using `UserData.txt` and `BookingDetails.txt`

## Stack
- Language: Java (works with JDK 8+)
- Runtime: Command-line / console application
- No external libraries required

## Repository layout
Top-level files:
- `Login.java` — main program and application flow (login, menu and booking operations)
- `BookingDetails.java` — Booking model (username + booking Date)
- `UserData.java` — User model and helpers
- `UserData.txt` — sample user data (tab-separated)
- `BookingDetails.txt` — sample booking data

## Prerequisites
- Java JDK installed (version 8 or newer)
- A terminal / command prompt

## How to build and run
1. Clone the repository: git clone https://github.com/AlanW0320/booking-management-system-java.git cd booking-management-system-java
2. Compile: javac *.java
3. Run: java Login

## Data files and formats
- `UserData.txt` — expected format per line: `username<TAB>password<TAB>email`  
Example: alice password123 alice@example.com

- `BookingDetails.txt` — sample uses `username_date` format where username and date are separated by an underscore and the date is `yyyy-MM-dd HH:mm`. Example: Helen_2025-02-24 14:00

Note: The code contains inconsistent handling of booking file formats and date formats (see Known issues below). Review the Troubleshooting & Known issues section for details and fixes.

## Typical usage
1. Start the app (`java Login`).
2. At the prompt: enter an existing username or type `new` to create an account.
 - When creating a new account you will be asked for username, password, and a valid email.
3. After login you will see the menu:
 - 1: Add Booking — provide a date/time string (see formats below).
 - 2: Update Booking — lists your bookings and allows editing.
 - 3: Cancel Booking — lists and allows cancelling.
 - 4: View Booking History — display your bookings.
 - 5: Edit User Profile — change username, password, or email.
 - 6: Quit.

Date input used by the "Add Booking" flow expects: `DD/MM/YYYY HH:MM` (e.g. `24/02/2025 14:00`) but bookings are stored using `yyyy-MM-dd HH:mm`. Be careful when entering dates.

## Troubleshooting & Known issues
The repository works for basic flows but contains several inconsistencies and bugs. Below are the main issues and recommended fixes.

1. Booking file parsing mismatch
 - Problem: `recordBookingDetail` writes bookings as `username_yyyy-MM-dd HH:mm` (underscore-separated) while `loadBookingsFromFile` currently splits on tabs and expects 3 parts, so parsing fails.
 - Fix: Update `loadBookingsFromFile` to split on underscore (or change `recordBookingDetail` to use tabs). Example fix:
   ```java
   String[] parts = line.split("_", 2);
   if (parts.length == 2) {
       String storedUsername = parts[0].trim();
       String dates = parts[1].trim();
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
       Date date = dateFormat.parse(dates);
       Bookings.add(new BookingDetails(storedUsername, date));
   }
   ```

2. Inconsistent date formats
 - Problem: Different parts of the code use different date formats:
   - User input: `dd/MM/yyyy HH:mm` (AddBooking).
   - Storage/parsing: `yyyy-MM-dd HH:mm`.
 - Fix: Pick a single canonical format for storage (recommended `yyyy-MM-dd HH:mm`) and convert user input to that format before saving. Update all SimpleDateFormat usages consistently.

3. ViewBooking logic may duplicate or behave unexpectedly
 - Problem: `ViewBooking()` adds matching bookings back into the same list being iterated, causing duplication or errors.
 - Fix: Use a separate list to collect the user's bookings:
   ```java
   List<BookingDetails> allBookings = loadBookingsFromFile("BookingDetails.txt");
   List<BookingDetails> userBookings = new ArrayList<>();
   for (BookingDetails b : allBookings)
       if (username.equals(b.getUsername())) userBookings.add(b);
   // display userBookings
   ```

4. saveBookingsToFile missing delimiter
 - Problem: `saveBookingsToFile` concatenates username and date without a delimiter.
 - Fix: Add a delimiter (tab or underscore):
   ```java
   String enter = booking.getUsername() + "\t" + dateFormat.format(booking.getBookingDate());
   ```

5. First-run file-not-found
 - If `UserData.txt` or `BookingDetails.txt` are missing, create empty files before first run:
   ```
   touch UserData.txt BookingDetails.txt
   ```

## License
No license file is present in this repository. To make this project open-source, add a `LICENSE` file (for example MIT or Apache-2.0).
