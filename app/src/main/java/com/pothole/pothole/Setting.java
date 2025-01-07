package com.pothole.pothole;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class Setting extends AppCompatActivity {
    TextView username, password;
    Button logout;
    DatabaseHelper DB;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        logout = findViewById(R.id.logout);

        DB = new DatabaseHelper(this);

        Cursor cursor = DB.getUsers();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // Yêu cầu email
                .build();

        gsc = GoogleSignIn.getClient(this, gso);
//
//        String userName1 = cursor.getString(cursor.getColumnIndexOrThrow("USERNAME"));
//        String password1 = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));
//
//        username.setText(userName1);
//        password.setText(password1);
//
//        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("username", userName1);
//        editor.putString("password", password1);
//        editor.apply();

        if (cursor != null && cursor.moveToFirst()) { // Kiểm tra và di chuyển con trỏ đến hàng đầu tiên
            String userName1 = cursor.getString(cursor.getColumnIndexOrThrow("USERNAME"));
            String password1 = cursor.getString(cursor.getColumnIndexOrThrow("PASSWORD"));

            username.setText(userName1);
            password.setText(password1);

            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", userName1);
            editor.putString("password", password1);
            editor.apply();

            cursor.close(); // Đừng quên đóng con trỏ để tránh rò rỉ tài nguyên
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                startActivity(new Intent(Setting.this, Login.class));
            }
        });
    }
}
