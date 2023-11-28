package tdtu.android.final_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;

    private FirebaseDatabase db;
    private DatabaseReference reference;
    public static final String INTENT_EVENT_ACTION = "INTENT_EVENT_ACTION";
    public static final String INTENT_NEW_EVENT_ACTION = "NEW";
    private  String[] words =new String[] {};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Not edit
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String edit = sh.getString("EditCase", "");
        if(!edit.equals("edit")){

            String emailName = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

            words = emailName.split("@");



            db = FirebaseDatabase.getInstance();
            reference = db.getReference("User");

            reference.child(words[0]).child("userCount").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("id", snapshot.getValue().toString());
                    myEdit.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("name", words[0]);
            myEdit.commit();

        }
        else{

        }


        // Toolbar Setting
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle("Home");



        //load fragment
        Fragment fragment;
        fragment = new Home();
        loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.logout:
                //code xử lý khi bấm menu1
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.clear();
                myEdit.commit();
                FirebaseAuth.getInstance().signOut();


                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                break;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_add:
                    toolbar.setTitle("Add Todo");
                    Intent newEventActivityIntent = new Intent(MainActivity.this, Add_Activity.class);
                    newEventActivityIntent.putExtra(INTENT_EVENT_ACTION, INTENT_NEW_EVENT_ACTION);
                    startActivityForResult(newEventActivityIntent, 100);
//                    fragment = new Add();
//                    loadFragment(fragment);

                    return true;
                case R.id.navigation_search:
                    toolbar.setTitle("Search");
                    fragment = new Search();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new Info();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_done:
                    toolbar.setTitle("Done");
                    fragment = new Done();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment;
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            toolbar.setTitle("Home");
            fragment = new Home();
            loadFragment(fragment);
//            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
        }
        if(resultCode == Activity.RESULT_CANCELED){
            toolbar.setTitle("Home");
            fragment = new Home();
            loadFragment(fragment);
//            Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_LONG).show();

        }
    }

}

