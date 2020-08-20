package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.activities.DragonMajorClassesActivity;
import com.robolancers.dragonclass.room.entities.DragonMajor;

import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DragonMajorAdapter extends RecyclerView.Adapter<DragonMajorAdapter.DragonMajorViewHolder> implements Filterable {
    private final LayoutInflater inflater;
    private List<DragonMajor> dragonMajors, dragonMajorsFiltered;
    private Context context;

    private Filter filter;

    public DragonMajorAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;

        filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterString = charSequence.toString();
                dragonMajorsFiltered = filterString.isEmpty() ? dragonMajors : dragonMajors.stream().filter(dragonMajor -> dragonMajor.getMajorName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList());
                Log.e("TEST", dragonMajorsFiltered.toString());

                FilterResults filterResults = new FilterResults();
                filterResults.values = dragonMajorsFiltered;

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values != null) {
                    dragonMajorsFiltered = (List<DragonMajor>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    @NonNull
    @Override
    public DragonMajorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DragonMajorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DragonMajorViewHolder holder, int position) {
        if (dragonMajorsFiltered != null) {
            holder.bind(dragonMajorsFiltered.get(position));
        } else {
            holder.dragonMajorItemView.setText("N/A");
        }
    }

    public void setDragonMajors(List<DragonMajor> dragonMajors) {
        this.dragonMajors = dragonMajors;
        this.dragonMajorsFiltered = dragonMajors;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dragonMajorsFiltered != null ? dragonMajorsFiltered.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DragonMajorViewHolder extends RecyclerView.ViewHolder {
        private final TextView dragonMajorItemView;

        private DragonMajorViewHolder(View itemView) {
            super(itemView);
            dragonMajorItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(DragonMajor dragonMajor) {
            dragonMajorItemView.setText(dragonMajor.getMajorName());

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, DragonMajorClassesActivity.class);
                intent.putExtra("DragonMajor", dragonMajor);
                context.startActivity(intent);
            });
        }
    }
}
