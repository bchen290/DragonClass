package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.activities.DragonClassDetailActivity;
import com.robolancers.dragonclass.room.entities.DragonClass;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class DragonClassAdapter extends RecyclerView.Adapter<DragonClassAdapter.DragonClassViewHolder>{
    private final LayoutInflater inflater;
    private List<DragonClass> classes;
    private Context context;

    public DragonClassAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public DragonClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DragonClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonClassViewHolder holder, int position) {
        if (classes != null) {
            holder.bind(classes.get(position));
        } else {
            holder.dragonClassItemView.setText("N/A");
        }
    }

    public void setClasses(List<DragonClass> dragonClasses) {
        this.classes = dragonClasses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return classes != null ? classes.size() : 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DragonClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView dragonClassItemView;

        private DragonClassViewHolder(View itemView) {
            super(itemView);
            dragonClassItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(DragonClass dragonClass) {
            dragonClassItemView.setText(dragonClass.getCourseID() + ": " + dragonClass.getCourseName());

            itemView.setOnClickListener(view -> {
                //new AlertDialog.Builder(context).setTitle(dragonClass.getCourseID()).setMessage("Description: " + dragonClass.getCourseDescription() + "\nPrerequisites: " + dragonClass.getCoursePrerequisites()).show();
                Intent intent = new Intent(context, DragonClassDetailActivity.class);
                intent.putExtra("DragonClass", dragonClass);
                context.startActivity(intent);
            });
        }
    }
}
