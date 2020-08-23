package com.robolancers.dragonclass.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robolancers.dragonclass.R;
import com.robolancers.dragonclass.activities.DragonClassDetailActivity;
import com.robolancers.dragonclass.room.DragonClassDatabase;
import com.robolancers.dragonclass.room.entities.DragonClass;
import com.robolancers.dragonclass.utilities.CourseNotFoundException;

import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DependencyAdapter extends RecyclerView.Adapter<DependencyAdapter.DependencyViewHolder> {
    private final LayoutInflater inflater;
    private List<DragonClass> courses;
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
        if (courses != null) {
            holder.bind(courses.get(position));
        } else {
            holder.dependencyItemView.setText("N/A");
        }
    }

    public void setCourses(List<DragonClass> courseIDs) {
        this.courses = courseIDs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courses != null ? courses.size() : 0;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    class DependencyViewHolder extends RecyclerView.ViewHolder {
        private final TextView dependencyItemView;

        private DependencyViewHolder(View itemView) {
            super(itemView);
            dependencyItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(DragonClass course) {
            if (!course.getCourseID().equals("This course has no dependencies")) {
                dependencyItemView.setText(course.getCourseID() + ": " + course.getCourseName());

                dependencyItemView.setOnClickListener(view -> DragonClassDatabase.getDatabase(context).dragonClassDao().getAllClasses().observe((DragonClassDetailActivity) context, dragonClasses -> {
                    Intent intent = new Intent(context, DragonClassDetailActivity.class);
                    intent.putExtra("DragonClass", course);
                    context.startActivity(intent);
                }));
            } else {
                dependencyItemView.setText(course.getCourseID());
            }
        }
    }
}
