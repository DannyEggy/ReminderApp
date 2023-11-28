package tdtu.android.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Done#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Done extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Done() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Done.
     */
    // TODO: Rename and change types and number of parameters
    public static Done newInstance(String param1, String param2) {
        Done fragment = new Done();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private RecyclerView doneRecyclerView;
    private List<Reminder> doneReminderList;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference("User");
    private List<Reminder> deleteList = new ArrayList<Reminder>();
    private String name;
    private ImageView btnDeleteAll;
    private TextView doneNamePerson;
    private int dontRemove = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_done, container, false);
        //get userName
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        name = sh.getString("name", "");

        doneReminderList = new ArrayList<Reminder>();

        doneNamePerson = view.findViewById(R.id.doneNamePerson);
        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("")){
                    doneNamePerson.setText(name);
                }
                else{
                    doneNamePerson.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        doneRecyclerView = view.findViewById(R.id.doneRecyclerView);
        doneRecyclerView.setAdapter(new RecyclerViewAdapter(doneReminderList, getActivity()));
        doneRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //set swiped options
        ItemTouchHelper itemTouchHelper_Done = new ItemTouchHelper(simpleCallback_DoneList);
        itemTouchHelper_Done.attachToRecyclerView(doneRecyclerView);


        reference.child(name).child("Reminder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);


                    if (reminder.getReminderDone() == 1 && dontRemove ==0) {
                        doneReminderList.add(reminder);
                    }

                }


                doneRecyclerView.getAdapter().notifyDataSetChanged();
//                Toast.makeText(getActivity(), "running", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        //delete Done Reminder
        btnDeleteAll = view.findViewById(R.id.btnDeleteAll2);
        btnDeleteAll.setOnClickListener((View view1)->{
            dontRemove = 1;
            for(Reminder reminder: doneReminderList){
                reference.child(name).child("Reminder").child(String.valueOf(reminder.getReminderID())).removeValue();

            }
            doneReminderList.clear();
            doneRecyclerView.getAdapter().notifyDataSetChanged();
        });
        return view;
    }

    //Swiped options
    ItemTouchHelper.SimpleCallback simpleCallback_DoneList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    Reminder deleteReminder = doneReminderList.get(position);
                    String deleteReminderName = deleteReminder.getReminderName();

                    //todo
                    dontRemove = 1;
                    deleteList.add(deleteReminder);
                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).removeValue();

                    doneReminderList.remove(position);
                    doneRecyclerView.getAdapter().notifyItemRemoved(position);
                    Snackbar.make(doneRecyclerView, deleteReminderName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //todo
                                    doneReminderList.add(position, deleteReminder);
                                    deleteList.remove(deleteReminder);

                                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).setValue(deleteReminder);

                                    doneRecyclerView.getAdapter().notifyItemInserted(position);
                                    doneRecyclerView.scrollToPosition(position);
                                }
                            }).show();


                    break;

            }


        }

        //Custom the options swiped
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(),R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)

                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


}