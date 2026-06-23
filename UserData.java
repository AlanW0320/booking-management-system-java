
public class UserData 
{
	private String username;
    private String password;
    private String email;	   

    public UserData (String username, String password, String email) 
    {	    	
        this.username = username;
        this.password = password;
        this.email = email;	       
    }

    // Getter and Setter
    
    public String getUsername() 
    {
    	return username;
    }

    public String getPassword() 
    {
        return password;
    }
   
    public String getEmail() 
    {
        return email;
    }
    
    public void setUsername(String username) 
    {
        this.username = username;
    }
    
    public void setPassword(String password) 
    {
        this.password = password;
    }
    
    public void setEmail(String email) 
    {
        this.email = email;
    }
    
    public String getFileString() 
    {
        return this.username + "\t"+this.password+"\t"+ this.email;
    }
}
