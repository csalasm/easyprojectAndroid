package inftel.easyprojectandroid.service;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import inftel.easyprojectandroid.R;
import inftel.easyprojectandroid.http.HttpRequest;
import inftel.easyprojectandroid.http.HttpTask;
import inftel.easyprojectandroid.interfaces.ResponseListener;
import inftel.easyprojectandroid.interfaces.ServiceListener;
import inftel.easyprojectandroid.model.Tarea;

/**
 * Created by anotauntanto on 7/4/16.
 */
public class TaskService implements ResponseListener {
    private ServiceListener listener;
    private String SERVER_IP;
    private String SERVER_PATH;


    public TaskService(Context context, ServiceListener listener) {
        this.listener = listener;
        SERVER_IP = context.getResources().getString(R.string.server_ip);
        SERVER_PATH = context.getResources().getString(R.string.server_path);
    }

    public void getTasks(String idUser, String idProject) {
        String url = SERVER_IP+SERVER_PATH+"entity.tarea/findTasksinProjectByIdUser/"+idUser+"/"+idProject;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.GET,url, null);
        new HttpTask(this,"getTasks").execute(httpRequest);
    }

    public void postTask(JSONObject taskJson) {
        String url = SERVER_IP+SERVER_PATH+"entity.tarea/";
        HttpRequest httpRequest = new HttpRequest(HttpRequest.POST,url, taskJson);
        new HttpTask(this,"postTask").execute(httpRequest);
    }

    @Override
    public void onResponse(Pair<String, String> response) {
        Log.e("RESPONSE", response.second);
        if (response.first.equals("getTasks"))
            parseTaskList(response.second);
    }


    private void parseTaskList(String response) {
        ArrayList<Tarea> taskList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++) {
                Tarea t = Tarea.fromJSON(jsonArray.getString(i));
                taskList.add(t);
            }

            listener.onListResponse(new Pair("getTasks", taskList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}