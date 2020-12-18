package com.slappsm.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetNameActivity extends AppCompatActivity {

    EditText loginInp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        loginInp = findViewById(R.id.loginInp);
        loginInp.setText(loadData());
    }


    private String loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    public void saveData(View v) {
        SharedPreferences sharedPreferences =
                getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login = loginInp.getText().toString();
        editor.putString("username", login);
        editor.apply();
        Intent intent = new Intent(SetNameActivity.this, MainActivity.class);
        intent.putExtra("username", login);
        startActivity(intent);

    }
}