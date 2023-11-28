package tdtu.android.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
    private String name;
    private RecyclerView searchRecyclerView;
    private EditText searchText;
    private List<Reminder> allReminderList = new ArrayList<Reminder>();
    private List<Reminder> filterList = new ArrayList<Reminder>();
    private TextView searchNamePerson;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    private DatabaseReference reference = db.getReference("User"); ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        searchRecyclerView = view.findViewById(R.id.doneListRecyclerView);
        searchNamePerson = view.findViewById(R.id.searchNamePerson);
        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        name = sh.getString("name", "");

        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("")){
                    searchNamePerson.setText(name);
                }
                else{
                    searchNamePerson.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        reference.child(name).child("Reminder").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                     Reminder reminder = dataSnapshot.getValue(Reminder.class);


                     allReminderList.add(reminder);

                     searchText = view.findViewById(R.id.searchText);
                     searchText.addTextChangedListener(new TextWatcher() {
                         @Override
                         public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                             searchRecyclerView.setAdapter(new RecyclerViewAdapter(allReminderList, getActivity()));
                             searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                         }

                         @Override
                         public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                             filterList.clear();

                             if(charSequence.toString().isEmpty()){
                                 searchRecyclerView.setAdapter(new RecyclerViewAdapter(filterList, getActivity()));
                                 searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                             }
                             else{
                                 Filter(charSequence.toString().toLowerCase());
                             }
                         }

                         @Override
                         public void afterTextChanged(Editable editable) {
//
                         }
                     });

                 }


//                 Toast.makeText(getActivity(), "running", Toast.LENGTH_LONG).show();

             }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });






        return view ;
    }

    private void Filter(String toString) {
        for(Reminder reminder: allReminderList){
            if(reminder.getReminderName().toLowerCase().contains(toString)){
                filterList.add(reminder);
            }
        }
        searchRecyclerView.setAdapter(new RecyclerViewAdapter(filterList, getActivity()));
        searchRecyclerView.getAdapter().notifyDataSetChanged();
    }


}