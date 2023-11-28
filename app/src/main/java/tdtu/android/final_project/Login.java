package tdtu.android.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText account, pass;
    private Button login, register;
    private FirebaseAuth mAuth;
    private static final String TAG = "MyActivity";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private TextView forgotPassword;
    private DatabaseReference reference = db.getReference("User"); ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        account = findViewById(R.id.account);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        forgotPassword = findViewById(R.id.textForgot);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            if(user.isEmailVerified()){
                Intent intent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(intent);
            }



        }


        forgotPassword.setOnClickListener((View view2)->{
            Intent forgotIntent = new Intent(Login.this, ResetPassword.class);
            startActivity(forgotIntent);
        });

        login.setOnClickListener((View view)->{
            login();
        });

        register.setOnClickListener((View view)->{
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

    }



    private void login(){
        String email,password;
        email=account.getText().toString();
//        Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
        password=pass.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email",Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();

            return;
        }

        loading loading = new loading(Login.this);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loading.startLoading();

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loading.dismiss();
                                }
                            },5000);

//                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSession",MODE_PRIVATE);
//                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                            myEdit.putString("session", "yes");
//                            myEdit.commit();
                            if(user.isEmailVerified()){
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                Login.this.startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, displ  ay a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//
                            Toast.makeText(getApplicationContext(), "Can't sign in, please check your Email, Password And Internet", Toast.LENGTH_LONG).show();

                        }
                    }
                });


    }
    }
