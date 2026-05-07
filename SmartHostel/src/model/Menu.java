package model;

public class Menu {

	private int id;
    	private String date;
    	private String breakfast;
    	private String lunch;
    	private String dinner;
    	private double breakfastPrice;
    	private double lunchPrice;
    	private double dinnerPrice;

	public Menu() {}

    	public Menu(int id, String date, String breakfast, String lunch, String dinner, double breakfastPrice, double lunchPrice, double dinnerPrice) {
        this.id = id;
        this.date = date;
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.breakfastPrice = breakfastPrice;
        this.lunchPrice = lunchPrice;
        this.dinnerPrice = dinnerPrice;
    	}

    	public int getId() { return id; }
    	public void setId(int id) { this.id = id; }
	
    	public String getDate() { return date; }
    	public void setDate(String date) { this.date = date; }

    	public String getBreakfast() { return breakfast; }
    	public void setBreakfast(String b){ this.breakfast = b; }

    	public String getLunch() { return lunch; }
    	public void setLunch(String l) { this.lunch = l; }	

    	public String getDinner() { return dinner; }
    	public void setDinner(String d) { this.dinner = d; }

    	public double getBreakfastPrice() { return breakfastPrice; }
    	public void setBreakfastPrice(double p) { this.breakfastPrice = p; }

    	public double getLunchPrice() { return lunchPrice; }
    	public void setLunchPrice(double p) { this.lunchPrice = p; }

    	public double getDinnerPrice() { return dinnerPrice; }
    	public void setDinnerPrice(double p) { this.dinnerPrice = p; }
   
    	public double getPriceForMeal(String mealType) {
       	
	switch (mealType.toUpperCase()) {
        	case "BREAKFAST": return breakfastPrice;
            	case "LUNCH": return lunchPrice;
           	case "DINNER": return dinnerPrice;
            	default:          
			return 0.0;  }
    		
	}

}
