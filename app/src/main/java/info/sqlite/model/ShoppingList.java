package info.sqlite.model;

public class ShoppingList {
    int listID;
    String listName;
    boolean isSelected;


    /**
     * Constructor for ShoppingList
     * @param listID
     * @param listName
     */
    public ShoppingList(int listID, String listName) {
        this.listID = listID;
        this.listName = listName;

    }

    /**Getters and Setters*/
    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
