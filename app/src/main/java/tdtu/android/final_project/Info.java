package tdtu.android.final_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Info extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Info.
     */
    // TODO: Rename and change types and number of parameters
    public static Info newInstance(String param1, String param2) {
        Info fragment = new Info();
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

    private TextView name_info;
    private EditText namePerson, emailPerson, phoneNumber, addressPerson, jobPerson;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Button saveInfo;

    private DatabaseReference reference = db.getReference("User");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        name_info = view.findViewById(R.id.name_info);
        namePerson = view.findViewById(R.id.namePerson);
        emailPerson = view.findViewById(R.id.emailPerson);
        addressPerson = view.findViewById(R.id.addressPerson);
        jobPerson = view.findViewById(R.id.jobPerson);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        saveInfo = view.findViewById(R.id.saveInfo);


        SharedPreferences sh = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String name = sh.getString("name", "");

        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("")){
                    name_info.setText(name);
                }
                else{
                    name_info.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference.child(name).child("account").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emailPerson.setText(snapshot.getValue().toString());
                emailPerson.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                namePerson.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(name).child("phoneNumber").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phoneNumber.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(name).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addressPerson.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(name).child("job").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobPerson.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        saveInfo.setOnClickListener((View view2)->{
            reference.child(name).child("userName").setValue(namePerson.getText().toString());
            reference.child(name).child("phoneNumber").setValue(phoneNumber.getText().toString());
            reference.child(name).child("address").setValue(addressPerson.getText().toString());
            reference.child(name).child("job").setValue(jobPerson.getText().toString());


            Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Info.this, MainActivity.class);
//            startActivity(intent);
        });


        return view;
    }
}