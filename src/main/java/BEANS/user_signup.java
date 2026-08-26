package BEANS;

import java.sql.Date;

import java.sql.Statement;

public class user_signup {

	//change the int to string
    private int user_id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String user_type;
    private Date created_at;

    public user_signup() {
    }
    public int getUser_id() {
        return user_id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public boolean InsertMethod()
	{
		try {
			DB db = new DB();
			
			db.DBconnect();

			String q1 = "insert into users (USER_ID, NAME, EMAIL, PASSWORD, PHONE, ADDRESS, USER_TYPE, CREATED_AT) "
			          + "values (users_seq.NEXTVAL, '"+name+"','"+email+"','"+password+"','"+phone+"','"+address+"','USER',SYSDATE)";
		    Statement stmt=db.stmt;
		    int x=stmt.executeUpdate(q1);
		    if(x>0)
		    {
		    	return true;
		    }
		    else
		    {
		    	return false;
		    }
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
		
		
	
}