package model;

import java.util.List;
import java.util.Vector;

public class DepartmentManager {
    private List<Department> list;
    private static DepartmentManager instance;

    private DepartmentManager() {
        list = new Vector<>();
    }

    public static DepartmentManager getInstance() {
        if (instance == null) {
            instance = new DepartmentManager();
        }
        return instance;
    }

    public void addDep(Department newDep) {
        list.add(newDep);
    }

    public void adDepList(List<Department> depList) {
        list.addAll(depList);
    }

    public List<Department> getDepList() {
        return list;
    }

    public Department getDepById(String id) {
        for (Department d : list) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public Department getDepByName(String name) {
        for (Department d : list) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    public void deleteList() {
        list.clear();
    }

    public String howMany() {
        return "" + list.size();
    }
}
