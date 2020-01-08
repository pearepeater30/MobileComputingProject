package recyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.myapplication.R;
import com.example.myapplication.SecondaryList;
import info.sqlite.helper.DBHelper;
import info.sqlite.model.ShoppingList;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private final List<ShoppingList> shoppingListList;
    private LayoutInflater mInflater;
    DBHelper dbHelper;

    /**
     * Creating the viewHolder for the recyclerview
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView shoppingListView;
        public final ShoppingListAdapter mAdapter;

        /**
         * Constructor for the ViewHolder assigning the itemListView to the proper recyclerview layout and the adapter for it.
         * @param itemView
         * @param adapter
         */
        public ViewHolder(View itemView, ShoppingListAdapter adapter){
            super(itemView);
            shoppingListView = itemView.findViewById(R.id.textViewShoppingListView);
            this.mAdapter = adapter;
            /**Creating a listener for clicks for each individual item in the list*/
            itemView.setOnClickListener(this);
        }

        /**
         * passing data to the Secondary List activity using intents.
         * @param v
         */
        @Override
        public void onClick(View v) {
            int mPosition = getLayoutPosition();
            int listID = shoppingListList.get(mPosition).getListID();
            String listName = shoppingListList.get(mPosition).getListName();
            Context context = v.getContext();
            Intent intent = new Intent(context, SecondaryList.class);
            intent.putExtra("List_ID", listID);
            intent.putExtra("List_Name",listName);
            context.startActivity(intent);
        }
    }

    /**
     * Creating an adapter for the layout
     * @param context
     * @param list
     */
    public ShoppingListAdapter(Context context, List<ShoppingList> list) {
        mInflater = LayoutInflater.from(context);
        this.shoppingListList = list;
    }

    /**
     * Sets up the layout for the ViewHolder
     * @param viewGroup
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        /**Assigning the layout for each individual item in the recyclerview*/
        View mItemView = mInflater.inflate(R.layout.recyclerview_shoppipnglist_row, viewGroup, false);
        return new ViewHolder(mItemView, this);
    }

    /**
     * Properties of each individual item in the recyclerview
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        /**assigning a ShoppingList object to each item in the Recyclerview*/
        ShoppingList mCurrent = shoppingListList.get(position);
        /**Setting the name for the text in ViewHolder*/
        viewHolder.shoppingListView.setText(mCurrent.getListName());
        /**Setting the tag for each ViewHolder which contains the properties of the current object*/
        viewHolder.itemView.setTag(mCurrent);
    }

    /**
     * Returns the size of the list of the recyclerview
     * @return
     */
    @Override
    public int getItemCount() {
        return shoppingListList.size();
    }

}
