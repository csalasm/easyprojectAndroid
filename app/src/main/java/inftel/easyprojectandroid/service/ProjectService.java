package inftel.easyprojectandroid.service;

import android.content.Context;
import android.util.Pair;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

import inftel.easyprojectandroid.R;
import inftel.easyprojectandroid.http.HttpRequest;
import inftel.easyprojectandroid.http.HttpTask;
import inftel.easyprojectandroid.interfaces.ResponseListener;
import inftel.easyprojectandroid.interfaces.ServiceListener;
import inftel.easyprojectandroid.model.Message;
import inftel.easyprojectandroid.model.Proyecto;
import inftel.easyprojectandroid.model.Usuario;

/**
 * Created by csalas on 7/4/16.
 */
public class ProjectService implements ResponseListener {
    private ServiceListener listener;
    private String SERVER_IP;
    private String SERVER_PATH;


    public ProjectService(Context context, ServiceListener listener) {
        this.listener = listener;
        SERVER_IP = context.getResources().getString(R.string.server_ip);
        SERVER_PATH = context.getResources().getString(R.string.server_path);
    }

    public void getProjects(String idUser) {
        String url = SERVER_IP+SERVER_PATH+"entity.proyecto/findProjectByIdUser/"+idUser;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getProjects").execute(httpRequest);
    }

    public void getProjectDetails (String idProject) {
        String url = SERVER_IP+SERVER_PATH+"entity.proyecto/getProjectDetails/"+idProject;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getProjectDetails").execute(httpRequest);

    }

    public void getUsersEmail(){
        String url = SERVER_IP + SERVER_PATH + "entity.usuario/getUsersEmail";
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getUserEmailList").execute(httpRequest);
    }

    public void setNewProject(JSONObject jsonObject){
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto?";
        HttpRequest httpRequest = new HttpRequest(HttpRequest.POST,url, jsonObject);
        new HttpTask(this,"setNewProject").execute(httpRequest);
    }

    public void sendEmailNewProject(JSONObject jsonObject){
        String url = SERVER_IP + SERVER_PATH + "entity.usuario/sendEmailCreate";
        HttpRequest httpRequest = new HttpRequest(HttpRequest.POST,url, jsonObject);
        new HttpTask(this,"sendEmailNewProject").execute(httpRequest);
    }

    public void getUsersEmailNonProject(String idProjet){
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/getUsersEmailNonProject/"+idProjet;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getUsersEmailNonProject").execute(httpRequest);
    }

    public void getChatFromProject(String idProject) {
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/getProjectChat/" + idProject;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET, url, null);
        new HttpTask(this, "getChatFromProject").execute(httpRequest);
    }

    public void getUsersEmailProject(String idProjet){
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/getUsersEmailProject/"+idProjet;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getUsersEmailProject").execute(httpRequest);
    }

    public void getUsersProject(String idProjet){
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/getUsersProject/"+idProjet;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getUsersProject").execute(httpRequest);
    }

    public void putProject (String idProject, JSONObject jsonObject) {
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/editProject/"+idProject;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.PUT,url, jsonObject);
        new HttpTask(this,"putProject").execute(httpRequest);

    }

    public void deleteProject (String idProject) {
        String url = SERVER_IP + SERVER_PATH + "entity.proyecto/deleteProject/"+idProject;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"deleteProject").execute(httpRequest);

    }

    @Override
    public void onResponse(Pair<String, String> response) {
        if (response.first.equals("getProjects")) {
            parseProjectList(response.second);
        } else if (response.first.equals("getUserEmailList")){
            parseEmails(response.second, "getUserEmailList");
        } else if (response.first.equals("getUsersEmailNonProject")) {
            parseEmails(response.second, "getUsersEmailNonProject");
        } else if (response.first.equals("getUsersEmailProject")) {
            parseEmails(response.second, "getUsersEmailProject");
        } else if (response.first.equals("getProjectDetails")) {
            parseProject(response.second);
        } else if (response.first.equals("getUsersProject")) {
            parseUsers(response.second);
        } else if (response.first.equals("getChatFromProject")) {
            parseChatProject(response.second);
        }

    }

    private void parseUsers(String response){
        ArrayList<Usuario> usersList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++) {
                Usuario u = Usuario.fromJSON(jsonArray.getString(i));
                usersList.add(u);
            }

            listener.onListResponse(new Pair("getUsersProject", usersList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseProject (String response) {
        Gson converter = new Gson();
        Proyecto p = converter.fromJson(response, Proyecto.class);
        listener.onObjectResponse(new Pair("getProjectDetails", p));

    }

    private void parseEmails (String response, String method) {

        ArrayList<String> userEmailList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++) {
                String email = jsonArray.getString(i);
                userEmailList.add(email);
            }
            listener.onListResponse(new Pair(method, userEmailList));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void parseProjectList(String response) {
        ArrayList<Proyecto> projectList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++) {
                Proyecto p = Proyecto.fromJSON(jsonArray.getString(i));
                projectList.add(p);
            }

            listener.onListResponse(new Pair("getProjects", projectList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseChatProject(String response) {
        ArrayList<Message> messageList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++) {
                Message m = Message.fromJSON(jsonArray.getString(i));
                messageList.add(m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onListResponse(new Pair("getChatFromProject", messageList));
    }

}
