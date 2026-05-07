package model;

public class Student extends User {

	private int studentId;   
	private String cms;         
    	private String name;
    	private String roomNo;
    	private String department;
    	private String contact;

    
    	public Student(int id, String username, String password, int studentId, String cms, String name,String roomNo, String department, String contact) {
        	
	super(id, username, password, "STUDENT");

        this.studentId = studentId;
        this.cms = cms;
        this.name = name;
        this.roomNo = roomNo;
        this.department = department;
        this.contact = contact;
    	}

    	// Simple constructor used when just storing student record...
    
	public Student(int studentId, String cms, String name, String roomNo, String department, String contact) {
        super();
        this.studentId  = studentId;
        this.cms = cms;
        this.name = name;
        this.roomNo = roomNo;
        this.department = department;
        this.contact = contact;
    	}

    	public Student() {}

    	// ---- Getters and Setters ----

    	public int getStudentId() { return studentId; }
    	public void setStudentId(int studentId) { this.studentId = studentId; }
	
    	public String getCms() { return cms; }
    	public void setCms(String cms) { this.cms = cms; }

    	public String getName() { return name; }
    	public void setName(String name) { this.name = name; }

    	public String getRoomNo() { return roomNo; }
    	public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    	public String getDepartment() { return department; }
    	public void setDepartment(String department) { this.department = department; }

    	public String getContact() { return contact; }
    	public void setContact(String contact) { this.contact = contact; }

    	// Override getDescription().......(Polymorphism)......

    	@Override
    	public String getDescription() {
        return "Student: " + name + " | CMS: " + cms + " | Room: " + roomNo;
    	}

}
