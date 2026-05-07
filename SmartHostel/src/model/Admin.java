package model;

public class Admin extends User {

	private boolean canManageAll = true;   //Admin can manage everything

    	public Admin(int id, String username, String password) {
        	super(id, username, password, "ADMIN"); 
    	}

   	public boolean isCanManageAll() { 
	return canManageAll; }

//      *****Overriding getDescription() from Class User (i.e Polymorphism)****

    	@Override
   	public String getDescription() {
        return "Admin: " + getUsername() + " | Full Access";
	}

}
