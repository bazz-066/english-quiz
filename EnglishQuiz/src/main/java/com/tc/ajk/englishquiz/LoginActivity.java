package com.tc.ajk.englishquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by baskoro on 2/26/15.
 */
public class LoginActivity extends Activity {
    private EditText txtUsername;
    private Button btnLogin;

    public static final String KEYUSERNAME = "username";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        this.txtUsername = (EditText) this.findViewById(R.id.txtUsername);
        this.btnLogin = (Button) this.findViewById(R.id.btnLogin);

        this.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUsername.getText().toString().length() > 0) {
                    Intent intent = new Intent(LoginActivity.this, ChooseQuizActivity.class);
                    String test = txtUsername.getText().toString();
                    intent.putExtra(LoginActivity.KEYUSERNAME, txtUsername.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}
