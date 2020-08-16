package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robolancers.dragonclass.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DependencyAdapter extends RecyclerView.Adapter<DependencyAdapter.DependencyViewHolder> {
    private final LayoutInflater inflater;
    private List<String> courseIDs;
    private Context context;

    public DependencyAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public DependencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DependencyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DependencyViewHolder holder, int position) {
        if (courseIDs != null) {
            holder.bind(courseIDs.get(position));
        } else {
            holder.dependencyItemView.setText("N/A");
        }
    }

    public void setCourseIDs(List<String> courseIDs) {
        this.courseIDs = courseIDs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseIDs != null ? courseIDs.size() : 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DependencyViewHolder extends RecyclerView.ViewHolder {
        private final TextView dependencyItemView;

        private DependencyViewHolder(View itemView) {
            super(itemView);
            dependencyItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(String courseID) {
            dependencyItemView.setText(courseID);
        }
    }
}