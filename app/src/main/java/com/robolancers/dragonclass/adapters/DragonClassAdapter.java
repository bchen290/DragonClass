package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.activities.DragonClassDetailActivity;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DragonClassAdapter extends RecyclerView.Adapter<DragonClassAdapter.DragonClassViewHolder>{
    private final LayoutInflater inflater;
    private List<DragonClass> classes, classesFiltered;
    private Context context;

    private Filter filter;

    public DragonClassAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;

        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString();
                classesFiltered = filterString.isEmpty() ? classes : classes.stream().filter(dragonClass -> dragonClass.getCourseID().toLowerCase().contains(filterString.toLowerCase()) || dragonClass.getCourseName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList());

                FilterResults filterResults = new FilterResults();
                filterResults.values = classesFiltered;

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values != null) {
                    classesFiltered = (List<DragonClass>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    @NonNull
    @Override
    public DragonClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DragonClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonClassViewHolder holder, int position) {
        if (classesFiltered != null) {
            holder.bind(classesFiltered.get(position));
        } else {
            holder.dragonClassItemView.setText("N/A");
        }
    }

    public void setClasses(List<DragonClass> dragonClasses) {
        this.classes = dragonClasses;
        this.classesFiltered = dragonClasses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return classesFiltered != null ? classesFiltered.size() : 0;
    }

    public Filter getFilter() {
        return filter;
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
