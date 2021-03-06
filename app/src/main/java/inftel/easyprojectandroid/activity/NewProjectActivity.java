package inftel.easyprojectandroid.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import inftel.easyprojectandroid.R;
import inftel.easyprojectandroid.interfaces.ServiceListener;
import inftel.easyprojectandroid.model.EasyProjectApp;
import inftel.easyprojectandroid.model.Proyecto;
import inftel.easyprojectandroid.model.Usuario;
import inftel.easyprojectandroid.service.ProjectService;

public class NewProjectActivity extends AppCompatActivity implements ServiceListener {

    private ProjectService projectService;
    AutoCompleteTextView text;
    MultiAutoCompleteTextView text1;
    ArrayList<String> emails = new ArrayList<String>();
    ArrayList<Usuario> CollectionUsers = new ArrayList<>();
    Usuario director = EasyProjectApp.getInstance().getUser();

    EditText projectName;
    EditText projectDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectService = new ProjectService(this, this);
        projectService.getUsersEmail();

        text1=(MultiAutoCompleteTextView)findViewById(R.id.multiAutoCompleteTextView1);
        //emails.remove(director.getEmail());
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, emails);


        text1.setAdapter(adapter);

        text1.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());



    }

    public void save(View view){
        projectName = (EditText) findViewById(R.id.input_nameProject);
        projectDescription = (EditText) findViewById(R.id.input_projectDescription);


        try {


            Proyecto newProject = new Proyecto();
            newProject.setNombreP(projectName.getText().toString());
            newProject.setDescripcion(projectDescription.getText().toString());
            newProject.setDirector(director);


            Gson trad = new Gson();

            projectService = new ProjectService(this, this);
            JSONObject jsonObject = new JSONObject(trad.toJson(newProject));
            String EmailNewProject = text1.getText().toString() + director.getEmail();
            jsonObject.put("listEmails", EmailNewProject);

            projectService.setNewProject(jsonObject);

            List<String> ArrayEmailNewProject = Arrays.asList(EmailNewProject.split("\\s*,\\s*"));
            String subject = projectName.getText().toString();
            String message = "Has sido añadido al proyecto: " + projectName.getText().toString() + ". El director del proyecto es: " + director.getNombreU();

            for(String email: ArrayEmailNewProject){
                JSONObject jsonEmailNewProject = new JSONObject();
                jsonEmailNewProject.put("destiny",email);
                jsonEmailNewProject.put("subject",subject);
                jsonEmailNewProject.put("message",message);
                projectService.sendEmailNewProject(jsonEmailNewProject);
            }

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);



        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onObjectResponse(Pair<String, ?> response) {

    }

    @Override
    public void onListResponse(Pair<String, List<?>> response) {
        if (response.first.equals("getUserEmailList")){
            for(Object email: response.second){
                if(!email.equals(director.getEmail())){
                    emails.add((String) email);
                }
            }
        }

    }

}
