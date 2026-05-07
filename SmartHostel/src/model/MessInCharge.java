package model;

public class MessInCharge extends User {

	private String assignedMess; 

    	public MessInCharge(int id, String username, String password) {
        	super(id, username, password, "MESS_INCHARGE");
        	this.assignedMess = "Main Mess";
    	}

    	public String getAssignedMess() { return assignedMess; }
    	public void   setAssignedMess(String assignedMess) { this.assignedMess = assignedMess; }

    	// Overriding getDescription()...(Polymorphism)......

    	@Override
    	public String getDescription() {
        	return "Mess Incharge: " + getUsername() + " | Mess: " + assignedMess;
    	}
}

