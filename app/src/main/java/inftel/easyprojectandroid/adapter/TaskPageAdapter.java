package inftel.easyprojectandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import inftel.easyprojectandroid.fragment.TaskListFragment;
import inftel.easyprojectandroid.model.Tarea;

/**
 * Created by csalas on 13/4/16.
 */
public class TaskPageAdapter extends FragmentPagerAdapter {
    int numTabs;
    private List<Tarea> taskList;
    private String idProject;

    public TaskPageAdapter(FragmentManager fm, int numTabs, String idProject) {
        super(fm);
        this.numTabs = numTabs;
        this.idProject = idProject;
    }

    @Override
    public Fragment getItem(int position) {
        TaskListFragment taskListFragment = new TaskListFragment();
        List<Tarea> filterTask;
        if (position == 0)
            filterTask = filterTask("to do");
        else if (position == 1)
            filterTask = filterTask("doing");
        else
            filterTask = filterTask("done");

        taskListFragment.setTaskList((ArrayList<Tarea>)filterTask);
        taskListFragment.setIdProject(idProject);
        return taskListFragment;

    }

    @Override
    public int getCount() {
        return numTabs;
    }

    public void setTaskList(List<Tarea> taskList) {
        this.taskList = taskList;
    }

    public List<Tarea> filterTask (String type) {

        List<Tarea> filterTaks = new ArrayList<>();

        for (Tarea task: taskList) {
            if (task.getEstado().equals(type)) {
                filterTaks.add(task);
            }
        }
        return filterTaks;

    }
}
