package net.coderodde.web.db.fun.model;

import java.sql.Date;

public final class FunnyPerson {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Date created;
    
    public int getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Date getCreated() {
        return created;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }
}
