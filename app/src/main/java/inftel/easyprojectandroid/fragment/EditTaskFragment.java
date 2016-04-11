package inftel.easyprojectandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import inftel.easyprojectandroid.R;
import inftel.easyprojectandroid.interfaces.ServiceListener;
import inftel.easyprojectandroid.service.ProjectService;

/**
 * Created by macbookpro on 10/4/16.
 */
public class EditTaskFragment extends Fragment implements ServiceListener {

    private View view;
    private ProjectService projectService;
    private MultiAutoCompleteTextView textAutocomplete;
    private RecyclerView recyclerView;
   // private RecyclerViewEditProjectAdapter adapter;

    ArrayList<String> emails = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        projectService = new ProjectService(getActivity(), this);
        projectService.getUsersEmailNonProject("948");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_task,container,false);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_editTask).setVisible(false);
        menu.findItem(R.id.action_visualizeTask).setVisible(true);

    }

    @Override
    public void onObjectResponse(Pair<String, ?> response) {

    }

    @Override
    public void onListResponse(Pair<String, List<?>> response) {

        if (response.first.equals("getUsersEmailNonProject")){
            for(Object email: response.second){
                emails.add((String) email);
                loadAutoCompleteContent();
            }
        }

    }

    public void loadAutoCompleteContent () {
        textAutocomplete=(MultiAutoCompleteTextView)getActivity().findViewById(R.id.editMultiAutoComplete);
        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, emails);
        textAutocomplete.setAdapter(adapter);
        textAutocomplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    public void loadCheckBoxContent(){


    }
}