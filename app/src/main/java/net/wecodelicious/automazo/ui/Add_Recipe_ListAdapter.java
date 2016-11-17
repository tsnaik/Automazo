package net.wecodelicious.automazo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bhaskar on 03/10/02016.
 */
public class Add_Recipe_ListAdapter extends RecyclerView.Adapter<Add_Recipe_ListAdapter.ViewHolder> {

    private ArrayList<String> mDataset_text;
    private ArrayList<Integer> mDataset_image;


    public Add_Recipe_ListAdapter(ArrayList<String> myDataset_text, ArrayList<Integer> myDataset_image) {
        mDataset_image=myDataset_image;
        mDataset_text=myDataset_text;
    }

    @Override
    public Add_Recipe_ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_recipe_list_row,parent,false);


        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(Add_Recipe_ListAdapter.ViewHolder holder, int position) {

        final String text = mDataset_text.get(position);
        final int image = mDataset_image.get(position);
        holder.tile_text.setText(text);
        holder.tile_image.setImageResource(image);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView tile_image;
        public TextView tile_text;

        public ViewHolder(View v ){
            super(v);
            tile_image = (ImageView)v.findViewById(R.id.add_recipe_list_row_image);
            tile_text = (TextView)v.findViewById(R.id.add_recipe_list_row_text);
        }

        public void add(int position, String text, Integer image)
        {
            mDataset_image.add(position,image);
            mDataset_text.add(position,text);
            notifyItemInserted(position);
        }

        public void remove(String text){
            int position = mDataset_text.indexOf(text);
            mDataset_text.remove(position);
            mDataset_image.remove(position);
        }
    }
}

