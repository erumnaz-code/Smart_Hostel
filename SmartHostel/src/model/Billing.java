package model;

public class Billing {

	private int id;
    	private int studentId;
    	private String cms;           
    	private String studentName;   
    	private String month;
    	private double totalAmount;
    	private String status;

    	public Billing(int id, int studentId, String month, double totalAmount, String status) {
        this.id = id;
        this.studentId = studentId;
        this.month = month;
        this.totalAmount = totalAmount;
        this.status = status;
   	}

    	public Billing() {}

    	public int getId() { return id; }
    	public void setId(int id) { this.id = id; }

    	public int getStudentId() { return studentId; }
    	public void setStudentId(int s) { this.studentId = s; }

    	public String getCms() { return cms; }
    	public void setCms(String c) { this.cms = c; }

    	public String getStudentName() { return studentName; }
    	public void setStudentName(String n) { this.studentName = n; }

    	public String getMonth() { return month; }
   	public void setMonth(String m) { this.month = m; }

    	public double getTotalAmount() { return totalAmount; }
    	public void setTotalAmount(double a) { this.totalAmount = a; }

    	public String getStatus() { return status; }
    	public void setStatus(String s) { this.status = s; }
}
