package model;

import java.util.List;
import java.util.Vector;

public class ProfManager {

    private List<Professors> list;
    private static ProfManager instance;

    private ProfManager() {
        list = new Vector<>();
    }

    public static ProfManager getInstance() {
        if (instance == null) {
            instance = new ProfManager();
        }
        return instance;
    }

    public void addProf(Professors newProf) {
        list.add(newProf);
    }

    public void addProfList(List<Professors> profList) {
        list.addAll(profList);
    }

    public List<Professors> getDozentenList() {
        return list;
    }

    public Professors getProf(String id) {
        for (Professors p : list) {
            if (p.getid().equals(id)) {
                return p;
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
