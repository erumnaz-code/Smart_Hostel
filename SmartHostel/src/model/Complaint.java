package model;

public class Complaint {

	private int id;
    	private int studentId;
    	private String cms;          
    	private String name;         
    	private String text;
    	private String status;       
    	private String createdAt;    

    	public Complaint(int id, int studentId, String text, String status, String createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.text = text;
        this.status = status;
        this.createdAt = createdAt;
   	}

    	public Complaint() {}

    	public int getId() { return id; }
    	public void setId(int id) { this.id = id; }

    	public int getStudentId() { return studentId; }
    	public void setStudentId(int studentId) { this.studentId = studentId; }

    	public String getCms() { return cms; }
    	public void setCms(String cms) { this.cms = cms; }

    	public String getName() { return name; }
    	public void setName(String name) { this.name = name; }

    	public String getText() { return text; }
    	public void setText(String text) { this.text = text; }

    	public String getStatus() { return status; }
    	public void setStatus(String status) { this.status = status; }

    	public String getCreatedAt() { return createdAt; }
    	public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
