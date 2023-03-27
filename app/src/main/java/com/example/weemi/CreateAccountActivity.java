package com.example.weemi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    private EditText email;
    private EditText prenom;
    private EditText nom;
    private EditText password;
    private EditText confirmPassword;
    private Button createConfirmBtn;
    private ImageButton retour;
    private TextView accueil;

    private String username;
    private String lastname;
    private String motdepasse;
    private String confirmMotdepasse;

    private String mail;
    private ProgressBar errorCreate;


    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        email = findViewById(R.id.createEmailAddressTextView);
        prenom = findViewById(R.id.createTextPersonPrenom);
        nom = findViewById(R.id.createTextPersonName);
        password = findViewById(R.id.createEditPassword);
        confirmPassword = findViewById(R.id.createConfirmEditPassword);
        createConfirmBtn = findViewById(R.id.createValiderButton);
        retour = findViewById(R.id.createForwardImageButton);
        accueil = findViewById(R.id.createTextView);
        errorCreate = findViewById(R.id.errorCreateAccountTextView);


        createConfirmBtn.setOnClickListener(v -> createAccount());


        Intent retourAccueil = new Intent(CreateAccountActivity.this,MainActivity.class);

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


    public void createAccount(){

        String mail = email.getText().toString();
        String username = prenom.getText().toString();
        String lastname = nom.getText().toString();
        String motdepasse = password.getText().toString();
        String confirmMotdepasse = confirmPassword.getText().toString();

        boolean isValidated = validate(mail,motdepasse,confirmMotdepasse);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(mail,motdepasse);

}

void createAccountInFirebase(String mail,String motdepasse){
changeInProgress(true);
    firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    firebaseAuth.createUserWithEmailAndPassword(mail, motdepasse).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            changeInProgress(false);
            if (task.isSuccessful()) {
                //Quand la creation du compte est ok
                Toast.makeText(CreateAccountActivity.this, "Compte crée avec succès,Verifié votre email pour valider", Toast.LENGTH_SHORT).show();
                firebaseAuth.getCurrentUser().sendEmailVerification();
                firebaseAuth.signOut();
                finish();
            }else{
                //ECHECH DE LA CREATION DU COMPTE
                Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    });
}

void changeInProgress(boolean inProgress){
        if(inProgress){
            errorCreate.setVisibility(View.VISIBLE);
            createConfirmBtn.setVisibility(View.GONE);
        }else {
            errorCreate.setVisibility(View.GONE);
            createConfirmBtn.setVisibility(View.VISIBLE);
        }
}

boolean validate(String mail,String motdepasse,String confirmMotdepasse){
//valider les données entrée par l'utilisateur
    if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
        email.setError("Email invalide");
        return false;
    }

    if(motdepasse.length()<6){
        password.setError("mdp trop court");
        return false;
    }

    if (!motdepasse.equals(confirmMotdepasse)){
        confirmPassword.setError("mot de passe invalide");
        return false;
    }
return true;

}


}