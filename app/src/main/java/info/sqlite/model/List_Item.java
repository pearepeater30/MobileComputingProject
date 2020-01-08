package info.sqlite.model;

public class List_Item {
    int list_ID;
    int item_ID;
    String item_name;
    boolean isSelected;

    /**
     * Constructor for each item in a list
     * @param list_ID
     * @param item_ID
     * @param item_name
     */
    public List_Item(int list_ID, int item_ID, String item_name) {
        this.list_ID = list_ID;
        this.item_ID = item_ID;
        this.item_name = item_name;
    }

    /**Getters and Setters for model*/
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getList_ID() {
        return list_ID;
    }

    public String getItemName() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setList_ID(int list_ID) {
        this.list_ID = list_ID;
    }

    public int getItem_ID() {
        return item_ID;
    }

    public void setItem_ID(int item_ID) {
        this.item_ID = item_ID;
    }
}
