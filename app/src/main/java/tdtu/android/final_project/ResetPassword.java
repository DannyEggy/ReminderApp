package tdtu.android.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private EditText emailReset;
    private Button btnResetPassword;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        emailReset = findViewById(R.id.emailReset);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener((View view)->{
            resetPassword();
        });
    }

    private void resetPassword(){
        String email = emailReset.getText().toString().trim();

        if(email.isEmpty()){
            emailReset.setError("Email is required!!!");
            emailReset.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailReset.setError("Please input valid email!!!");
            emailReset.requestFocus();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loading loading = new loading(ResetPassword.this);
                    loading.startLoading();

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loading.dismiss();
                        }
                    },5000);
                    Toast.makeText(getApplicationContext(),"Please check your email to Reset Password", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ResetPassword.this, Login.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
}