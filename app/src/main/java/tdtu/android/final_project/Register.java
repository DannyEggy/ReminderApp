package tdtu.android.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText accountRegister, passRegister;
    private Button register1;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference reference = db.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        accountRegister = findViewById(R.id.accountRegister);
        passRegister = findViewById(R.id.passwordRegister);
        register1 = findViewById(R.id.register1);



        loading loading =new loading(Register.this);
        register1.setOnClickListener((View view)->{
            register();

        });

    }

    private void register(){
        String password;
        String email=accountRegister.getText().toString();
        password=passRegister.getText().toString();

//        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();

            return;
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
                            String[] words = email.split("@");
                            User user = new User(email, password);
                            //        reference.setValue();

                            reference.child(words[0]).setValue(user);

                            loading.startLoading();

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.dismiss();
                                }
                            },5000);


                            Toast.makeText(getApplicationContext(), "Create User successfully, Please verify account in your Email !!!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Email is already exist", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }
}