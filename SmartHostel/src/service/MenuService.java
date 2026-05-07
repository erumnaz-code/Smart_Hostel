package service;

import dao.MenuDAO;
import model.Menu;

import java.util.List;

public class MenuService {

    private MenuDAO menuDAO = new MenuDAO();

    public boolean saveMenu(Menu menu) {
        // Basic validation
        if (menu.getBreakfast().isEmpty() || menu.getLunch().isEmpty() || menu.getDinner().isEmpty()) {
            System.out.println("All meal fields must be filled!");
            return false;
        }
        return menuDAO.saveMenu(menu);
    }

    public Menu getMenuByDate(String date) {
        return menuDAO.getMenuByDate(date);
    }

    public List<Menu> getAllMenus() {
        return menuDAO.getAllMenus();
    }
}
