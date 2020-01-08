package recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.myapplication.R;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.List_Item;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private final List<List_Item>  itemList;
    private LayoutInflater mInflater;
    DBHelper dbHelper;

    /**
     * Creating the viewHolder for the recyclerview
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView itemListView;
        public final ItemListAdapter mAdapter;

        /**
         * Constructor for the ViewHolder assigning the itemlistView to the proper recyclerview layout and the adapter for it
         * @param itemView
         * @param adapter
         */
        public ViewHolder(View itemView, ItemListAdapter adapter) {
            super(itemView);
            itemListView = itemView.findViewById(R.id.textViewItemView);
            this.mAdapter = adapter;
        }

    }

    /**
     * Creating an adapter for the list
     * @param context
     * @param list
     */
    public ItemListAdapter(Context context, List<List_Item> list){
        mInflater = LayoutInflater.from(context);
        this.itemList = list;
    }

    /**
     * Sets up the layout for each ViewHolder
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /**Assigning the layout for each individual item in the recyclerview*/
        View mItemView = mInflater.inflate(R.layout.recyclerview_itemlist_row, viewGroup, false);
        return new ViewHolder(mItemView, this);
    }

    /**
     * Properties of each individual item in the recyclerview
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        /**assigning a List_Item object to each item in the Recyclerview*/
        List_Item mCurrent = itemList.get(position);
        /**Setting the name for the text in ViewHolder*/
        viewHolder.itemListView.setText(mCurrent.getItemName());
        /**Setting the tag for each ViewHolder which contains the properties of the current object*/
        viewHolder.itemView.setTag(mCurrent);
    }

    /**Return the size of the list of the recycler view*/
    @Override
    public int getItemCount() {
        return itemList.size();
    }


}
