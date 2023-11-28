package tdtu.android.final_project;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ReminderViewHolder>  {
    private List<Reminder> reminderList;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(List<Reminder> reminderList, Context context) {

        this.reminderList = reminderList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }



    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerViewItem = layoutInflater.inflate(R.layout.reminder_list_item, parent, false);
        recyclerViewItem.setOnClickListener((View view)->{

        });

        return new ReminderViewHolder(recyclerViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder reminder = this.reminderList.get(position);
        holder.reminderName.setText(reminder.getReminderName());
        holder.reminderContent.setText(reminder.getReminderContent());
        holder.reminderTime.setText(reminder.getReminderTime());
        holder.reminderDate.setText(reminder.getReminderDate());
        holder.reminderPlace.setText(reminder.getReminderPlace());

        if(reminder.getReminderImportant()==1){
            holder.reminderName.setText("✧ "+reminder.getReminderName());
        }

        if(reminder.getReminderImportant()==2){
            holder.reminderName.setText("✧✧ "+reminder.getReminderName());
        }

        if(reminder.getReminderImportant()==3){
            holder.reminderName.setText("✧✧✧ "+reminder.getReminderName());
        }

        if(reminder.getReminderDone()==1){
            holder.info.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class ReminderViewHolder extends  RecyclerView.ViewHolder {

        TextView reminderName;
        TextView reminderContent;
        TextView reminderTime;
        TextView reminderDate;
        TextView reminderPlace;
        ImageView info;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            this.reminderName = itemView.findViewById(R.id.name_reminder);
            this.reminderContent = itemView.findViewById(R.id.content_reminder);
            this.info = itemView.findViewById(R.id.info);
            this.reminderTime = itemView.findViewById(R.id.time_reminder);
            this.reminderDate = itemView.findViewById(R.id.date_reminder);
            this.reminderPlace = itemView.findViewById(R.id.place_reminder);

            info.setOnClickListener((View view)->{
                Intent editIntent = new Intent(context, Add_Activity.class);

                Reminder reminder = reminderList.get(getAdapterPosition());
                editIntent.putExtra("key",String.valueOf(reminder.getReminderID()) );
                editIntent.putExtra("check", 1);
                context.startActivity(editIntent);
            });

        }
    }


}

