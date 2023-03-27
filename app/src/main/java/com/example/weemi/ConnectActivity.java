package com.example.weemi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class ConnectActivity extends AppCompatActivity {


    private Button confirm;
    private EditText email;
    private EditText password;
    private TextView forgotPassword;
    private ImageButton retour;
    private TextView accueil;

    private ProgressBar errorConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        errorConnect = findViewById(R.id.errorConnectAccountTextView);
        confirm = findViewById(R.id.createValiderButton);
        email = findViewById(R.id.createEmailAddressTextView);
        password = findViewById(R.id.createEditPassword);
        forgotPassword = findViewById(R.id.forgotPasswordTextView);
        retour = findViewById(R.id.createForwardImageButton);
        accueil = findViewById(R.id.createTextView);

        confirm.setOnClickListener(v -> connectUser());


        Intent retourAccueil = new Intent(ConnectActivity.this,MainActivity.class);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(retourAccueil);

            }
        });

        accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(retourAccueil);

            }
        });


    }


    // METHODE DE CONNECTION DE LUTILISATEUR A LA BASE DE DONNEE FIREBASE
    public void connectUser() {
        String mail = email.getText().toString();
        String motdepasse = password.getText().toString();
        boolean isValidated = validate(mail,motdepasse);
        if(!isValidated){
            return;
        }

        loginAccountInFirebase(mail,motdepasse);
    }


    void loginAccountInFirebase(String mail,String motdepasse){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(mail,motdepasse).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            changeInProgress(false);
            if(task.isSuccessful()){
                //login is success
                if(firebaseAuth.getCurrentUser().isEmailVerified()){
                    // SI LOGIN OK ALORS DEMARRE NOUVELLE ACTIVITE
                    startActivity(new Intent(ConnectActivity.this,MenuActivity.class));
                    finish();
                }else {
                    Toast.makeText(ConnectActivity.this, "Email non verifié, S'il vous plait verifié votre email", Toast.LENGTH_SHORT).show();
                }
            }else {
                // login failed
                Toast.makeText(ConnectActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            }
        });

    }
    void changeInProgress(boolean inProgress){
        if(inProgress){
            errorConnect.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.GONE);
        }else {
            errorConnect.setVisibility(View.GONE);
            confirm.setVisibility(View.VISIBLE);
        }
    }

boolean validate(String mail,String motdepasse){
//valider les données entrée par l'utilisateur
    if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Email invalide");
        return false;
    }
    if(motdepasse.length()<6){
            password.setError("mdp trop court");
        return false;
    }
        return true;
    }


}
