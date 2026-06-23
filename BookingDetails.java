
import java.util.Date;

public class BookingDetails 
{
    private String username;
    private Date BookingDate;

    public BookingDetails(String username, Date BookingDate) 
    {
        this.username = username;
        this.BookingDate = BookingDate;
    }

    public String getUsername() 
    {
        return username;
    }

    public Date getBookingDate() 
    {
        return BookingDate;
    }

    public void setBookingDate(Date BookingDate) 
    {
		this.BookingDate = BookingDate;
		
	}
    
    public String toString() {
        return "Username: " + username + ", Date: " + BookingDate;
    }

}
