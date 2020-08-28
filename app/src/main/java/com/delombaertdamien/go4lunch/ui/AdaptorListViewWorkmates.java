package com.delombaertdamien.go4lunch.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.delombaertdamien.go4lunch.R;

import java.util.Arrays;
import java.util.List;

public class AdaptorListViewWorkmates extends RecyclerView.Adapter<WorkmatesViewHolder>{

    private List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9);

    @Override
    public WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_workmate, parent, false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkmatesViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}


class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    private final ImageView icon;
    private final TextView text;


    public WorkmatesViewHolder(View itemView) {
        super(itemView);

        icon = itemView.findViewById(R.id.item_workmate_icon);
        text = itemView.findViewById(R.id.item_workmate_text);

    }

    public void bind (){

    }
}
