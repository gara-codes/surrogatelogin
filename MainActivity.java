package com.example.surrogatelogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setTitle("Log in screen");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void doSignUp(View v){
        EditText Role = (EditText)findViewById(R.id.RoleeditTextText);
        EditText UserID = (EditText)findViewById(R.id.UsereditText);
        EditText Password = (EditText)findViewById(R.id.editTextTextPassword);
        TextView t = (TextView)findViewById(R.id.textView4);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("UserID",UserID.getText().toString())
                .add("Password",Password.getText().toString())
                .add("Role",Role.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2814267/SignUpSurr.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Unexpected code" + response);
                }else{
                        final String responsee = response.body().string();
                        Log.d("RESPONSE", responsee);

                    try {
                        JSONObject jsonObject = new JSONObject(responsee);
                        String status = jsonObject.getString("Status");
                        runOnUiThread(() -> {
                        if(status.equals("Success")){
                                t.setText("Success");
                                Toast.makeText(getApplicationContext(),"Sign up successful!",Toast.LENGTH_SHORT).show();
                            }else if(status.equals("Exists")){
                            t.setText("User already exists");
                                Toast.makeText(getApplicationContext(),"User already exists!",Toast.LENGTH_SHORT).show();
                            }else if(status.equals("IncRole")){
                            t.setText("Please select either an Admin, Individual or Volunteer role");
                                Toast.makeText(getApplicationContext(),"Please select either an Admin, Individual or Volunteer role",Toast.LENGTH_SHORT).show();
                            }else{
                                t.setText("Unsuccessful");
                                Toast.makeText(getApplicationContext(),"Sign up unsuccessful!",Toast.LENGTH_SHORT).show();
                            }
                            });

                    } catch (JSONException e) {
                       e.printStackTrace();
                    }

                }
            }
        });




        }



    public void doLogin(View v){
        EditText Role = (EditText)findViewById(R.id.RoleeditTextText);
        EditText UserID = (EditText)findViewById(R.id.UsereditText);
        EditText Password = (EditText)findViewById(R.id.editTextTextPassword);
        String role = Role.getText().toString();
        TextView t = (TextView)findViewById(R.id.textView4);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("UserID",UserID.getText().toString())
                .add("Password",Password.getText().toString())
                .add("Role",Role.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2814267/LogInSurr.php")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("Unexpected code" + response);
                }else{
                    final String responsee = response.body().string();
                    Log.d("RESPONSE", responsee);

                    try {
                        JSONObject jsonObject = new JSONObject(responsee);
                        String status = jsonObject.getString("Status");

                        if(status.equals("Success")){
                            runOnUiThread(() -> {

                                Toast.makeText(getApplicationContext(),"Log in successful!",Toast.LENGTH_SHORT).show();
                                if(role.equals("Admin")){
                                    Intent intent = new Intent(MainActivity.this,AdminActivity.class);
                                    startActivity(intent);
                                } else if (role.equals("Volunteer")) {
                                    Intent intent = new Intent(MainActivity.this,VolunteerActivity.class);
                                    startActivity(intent);
                                } else if (role.equals("Individual")) {
                                    Intent intent = new Intent(MainActivity.this,IndividualActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }else if(status.equals("User not found")){
                            runOnUiThread(() -> {
                                t.setText("User not found");
                                Toast.makeText(getApplicationContext(),"User not found!",Toast.LENGTH_SHORT).show();
                            });

                        }else if(status.equals("Fail")){
                            runOnUiThread(() -> {
                                t.setText("Incorrect password or username");
                                Toast.makeText(getApplicationContext(),"Incorrect UserID or Password!",Toast.LENGTH_SHORT).show();
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}