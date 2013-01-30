package org.swp.emo.client;



import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class Registration {
    Long id;
    
   
    @Size(min = 4, max = 16)
    String username;
    

    String email;
    
    @Size(min=5 ,max = 20)
    String password1;
    
    @Size(min=5 ,max = 20)
    String password2;
    public Registration() {
        
    }
    public Registration(Long id) {
            this.id = id;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}


}

