package model;

public class Attendance {

	private int id;
	private int studentId;
	private String cms;           
	private String studentName;   
	private String date;
	private String mealType;
	private String status;
	private double mealPrice;     

	public Attendance(int id, int studentId, String date, String mealType, String status, double mealPrice) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.mealType  = mealType;
       	this.status = status;
       	this.mealPrice = mealPrice;
    	}

    	public Attendance() {}

   	public int getId() { return id; }
    	public void setId(int id) { this.id = id; }

    	public int getStudentId() { return studentId; }
    	public void setStudentId(int s) { this.studentId = s; }

    	public String getCms() { return cms; }
    	public void setCms(String c) { this.cms = c; }

    	public String getStudentName() { return studentName; }
    	public void setStudentName(String n) { this.studentName = n; }

    	public String getDate() { return date; }
    	public void setDate(String d) { this.date = d; }

    	public String getMealType() { return mealType; }
    	public void setMealType(String m) { this.mealType = m; }

   	public String getStatus() { return status; }
    	public void setStatus(String s) { this.status = s; }

    	public double getMealPrice() { return mealPrice; }
    	public void setMealPrice(double p)  { this.mealPrice = p; }
}
