package tdtu.android.final_project;



import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
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
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

//    @Override
//    public void onStop() {
//        super.onStop();
////        deleteReminderList();
//        setDoneList();
//        todo_recyclerView.getAdapter().notifyDataSetChanged();
//        important_recyclerView.getAdapter().notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        deleteReminderList();
//        setDoneList();
//        todo_recyclerView.getAdapter().notifyDataSetChanged();
//        important_recyclerView.getAdapter().notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        todo_recyclerView.getAdapter().notifyDataSetChanged();
//        important_recyclerView.getAdapter().notifyDataSetChanged();
//
////        deleteReminderList();
//        setDoneList();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        deleteReminderList();
//
//        todo_recyclerView.getAdapter().notifyDataSetChanged();
//        important_recyclerView.getAdapter().notifyDataSetChanged();
//
//
//    }



    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
//        deleteReminderList();
    }

    public static final String INTENT_EVENT_ACTION = "INTENT_EVENT_ACTION";
    public static final String INTENT_NEW_EVENT_ACTION = "NEW";

    private RecyclerView todo_recyclerView;
    private RecyclerView important_recyclerView;

    private List<Reminder> reminderList;
    private List<Reminder> allReminderList;
    private List<Reminder> importantReminderList;
    private List<Reminder> doneReminderList;

    private List<Reminder> deleteList= new ArrayList<Reminder>();
    public List<Reminder> doneList = new ArrayList<Reminder>();
    private TextView namePerson;
    private int dontChange =0;
    private String name;



    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private DatabaseReference reference = db.getReference("User"); ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        todo_recyclerView = view.findViewById(R.id.doneListRecyclerView);
        important_recyclerView = view.findViewById(R.id.importantListRecyclerView);
        namePerson = view.findViewById(R.id.namePerson);

        //get Name
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        name = sh.getString("name", "");

        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("")){
                    namePerson.setText(name);
                }
                else{
                    namePerson.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        allReminderList = new ArrayList<Reminder>();
        reminderList  = new ArrayList<Reminder>();
        importantReminderList = new ArrayList<Reminder>();
        doneReminderList = new ArrayList<Reminder>();

        todo_recyclerView.setAdapter(new RecyclerViewAdapter(reminderList, getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        todo_recyclerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper itemTouchHelper_Todo = new ItemTouchHelper(simpleCallback);
        itemTouchHelper_Todo.attachToRecyclerView(todo_recyclerView);


//        important_recyclerView.setAdapter(new RecyclerViewSortListAdapter(importantReminderList, getActivity()));
        important_recyclerView.setAdapter(new RecyclerViewAdapter(importantReminderList, getActivity()));

        important_recyclerView.setAdapter(new RecyclerViewAdapter(importantReminderList, getActivity()));

        important_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ItemTouchHelper itemTouchHelper_Important = new ItemTouchHelper(simpleCallback_ImportantList);
        itemTouchHelper_Important.attachToRecyclerView(important_recyclerView);


        reference.child(name).child("Reminder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);


                    if(reminder.getReminderImportant()==0 && reminder.getReminderDone()==0 && dontChange==0){
                        reminderList.add(reminder);
                    }

                    if(reminder.getReminderImportant()>=1 && reminder.getReminderDone()==0 && dontChange==0) {

                        if(reminder.getReminderImportant()==3) {
                            int count = 0;
                            for(Reminder reminderLevel3: importantReminderList){
                                if(reminderLevel3.getReminderImportant()==3){
                                    count+=1;
                                }
                            }
                            importantReminderList.add(count, reminder);
                        }

                        else if(reminder.getReminderImportant()==2){
                            int count = 0;
                            for(Reminder reminderLevel3: importantReminderList){
                                if(reminderLevel3.getReminderImportant()==3){
                                    count+=1;
                                }
                                if(reminderLevel3.getReminderImportant()==2){
                                    count+=1;
                                }
                            }
                            importantReminderList.add(count, reminder);
                        }
                        else{
                            importantReminderList.add(reminder);
                        }


                    }


                }

                todo_recyclerView.getAdapter().notifyDataSetChanged();
                important_recyclerView.getAdapter().notifyDataSetChanged();

//                Toast.makeText(getActivity(), "running", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        return view;

    }

//    private List<Reminder> sortList(List<Reminder> reminderList){
//        List<Reminder> sortList = new ArrayList<Reminder>();
//        for(int i=3; i>0; i--){
//            for(Reminder reminder: reminderList){
//                if(reminder.getReminderImportant()==3){
//                    sortList.add(reminder);
//                }
//            }
//        }
//
//
//        return sortList;
//    }


// todo_recyclerView
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:

                    //todo
                    Reminder deleteReminder = reminderList.get(position);
                    String deleteReminderName = reminderList.get(position).getReminderName();
                    dontChange =1;
//                    deleteList.add(deleteReminder);

                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).removeValue();
                    reminderList.remove(position);

                    todo_recyclerView.getAdapter().notifyItemRemoved(position);
                    Snackbar.make(todo_recyclerView, deleteReminderName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public  void onClick(View view) {
                                    dontChange =1;
                                    //todo
                                    reminderList.add(position, deleteReminder);
//                                    deleteList.remove(deleteReminder);
                                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).setValue(deleteReminder);

                                    todo_recyclerView.getAdapter().notifyItemInserted(position);
                                    todo_recyclerView.scrollToPosition(position);
                                }
                            }).show();

                    break;
                case ItemTouchHelper.RIGHT:
                    Reminder doneReminder = reminderList.get(position);
                    String doneReminderName = doneReminder.getReminderName();
                    dontChange =1;
                    //todo
                    //delete the reminder from todoList , DO NOT delete reminder from database
                    reminderList.remove(position);




                    //todo
                    // Add doneList and set done attribute to 1
                    doneReminder.setReminderDone(1);


                    reference.child(name).child("Reminder").child(String.valueOf(doneReminder.getReminderID())).setValue(doneReminder);

//                    doneReminderList.add(doneReminder);
                    todo_recyclerView.getAdapter().notifyItemRemoved(position);

                    Snackbar.make(todo_recyclerView, doneReminderName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dontChange =1;
                                    //todo
                                    reminderList.add(position, doneReminder);
                                    doneReminder.setReminderDone(0);
//                                    doneReminderList.remove(doneReminder);


                                    reference.child(name).child("Reminder").child(String.valueOf(doneReminder.getReminderID())).setValue(doneReminder);


//                                    todo_recyclerView.getAdapter().notifyItemInserted(position);
                                    todo_recyclerView.scrollToPosition(position);
                                }
                            }).show();
                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(),R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(),R.color.done))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_done_24)


                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    // ItemTouchHelper for ImportantList
    ItemTouchHelper.SimpleCallback simpleCallback_ImportantList = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:

                    Reminder deleteReminder = importantReminderList.get(position);

                    String deleteReminderName = deleteReminder.getReminderName();
                    dontChange =1;
                    //todo
                    // delete reminder

//                    deleteList.add(deleteReminder);


                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).removeValue();
                    importantReminderList.remove(position);
                    // adapter refresh
                    important_recyclerView.getAdapter().notifyDataSetChanged();

                    // Undo
                    Snackbar.make(important_recyclerView, deleteReminderName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dontChange =1;
                                    //todo
                                    //undo
                                    importantReminderList.add(position, deleteReminder);
//                                    deleteList.remove(deleteReminder);
                                    reference.child(name).child("Reminder").child(String.valueOf(deleteReminder.getReminderID())).setValue(deleteReminder);

                                    //adapter refresh

                                    important_recyclerView.getAdapter().notifyItemInserted(position);
                                    important_recyclerView.scrollToPosition(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Reminder doneReminder = importantReminderList.get(position);
                    String doneReminderName = doneReminder.getReminderName();
                    dontChange =1;
                    //todo
                    //move  importantList to done list and set done attribute to 1
                    importantReminderList.remove(position);
                    doneReminder.setReminderDone(1);
//                    doneReminderList.add(doneReminder);

                    reference.child(name).child("Reminder").child(String.valueOf(doneReminder.getReminderID())).setValue(doneReminder);


                    important_recyclerView.getAdapter().notifyItemRemoved(position);

                    Snackbar.make(important_recyclerView, doneReminderName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dontChange =1;
                                    //todo
                                    //undo
                                    importantReminderList.add(position, doneReminder);
                                    doneReminder.setReminderDone(0);
//                                    doneReminderList.remove(doneReminder);

                                    reference.child(name).child("Reminder").child(String.valueOf(doneReminder.getReminderID())).setValue(doneReminder);



                                    important_recyclerView.getAdapter().notifyItemInserted(position);
                                    important_recyclerView.scrollToPosition(position);
                                }
                            }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getActivity(),R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getActivity(),R.color.done))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_done_24)


                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };







}