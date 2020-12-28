package com.example.testproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.api.model.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class AuthActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    private FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    RelativeLayout root;
    SharedPreferences mSettings;
    public static final String PREFERENCES_LOGIN = "LOGIN";
    public static final String PREFERENCES_PASSWORD = "PASSWORD";
    public static final String PREFERENCES_FILE = "mysettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btnSignIn=findViewById(R.id.btnSignIn);
        btnRegister=findViewById(R.id.btnRegister);
        root=findViewById(R.id.root_element);

        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        users=db.getReference("People");
        mSettings = getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        if (mSettings.contains(PREFERENCES_LOGIN) && mSettings.contains(PREFERENCES_PASSWORD)) {
            // выводим данные в TextView
            String login = mSettings.getString(PREFERENCES_LOGIN,
                    "");
            String password = mSettings.getString(PREFERENCES_PASSWORD,
                    "");

            auth.signInWithEmailAndPassword(login, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Snackbar.make(root, "Сессия сохранена",Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(AuthActivity.this, AviaMainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(root, "Ошибка авторизации."+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                }
            });


        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Вход");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.window_sigh_in, null);
        dialog.setView(sign_in_window);

        final MaterialEditText email=sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText password=sign_in_window.findViewById(R.id.passwordField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().length()<5){
                    Snackbar.make(root, "Введите ваш пароль (более 5 символов)", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //ЗАПИСЬ СЕССИИ В PREFERENCIES
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString(PREFERENCES_LOGIN, email.getText().toString());
                                editor.putString(PREFERENCES_PASSWORD, password.getText().toString());
                                editor.apply();



                                startActivity(new Intent(AuthActivity.this, AviaMainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, "Ошибка авторизации."+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });

                
            }
        });

        dialog.show();

    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.window_register, null);
        dialog.setView(register_window);

        final MaterialEditText email=register_window.findViewById(R.id.emailField);
        final MaterialEditText name=register_window.findViewById(R.id.nameField);
        final MaterialEditText password=register_window.findViewById(R.id.passwordField);
        final MaterialEditText phone=register_window.findViewById(R.id.phoneField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().length()<5){
                    Snackbar.make(root, "Введите ваш пароль (более 5 символов)", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPassword(password.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make( root,"Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

            }
        });

        dialog.show();

    }

}
