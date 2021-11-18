package model;

import android.content.Context;

import java.util.List;
import java.util.Vector;

public class ProfManager {

    private List<Professors> list;
    private Context context;
    private static ProfManager instance;

    private ProfManager() {
        list = new Vector<>();
        addTestData();
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

    private void addTestData() {
        list.add(new Professors("test1@test.de", "Peter", "Parker", "x", "Teststr.", "1", "33101", "Paderborn", "Apfel", "1"));
        list.add(new Professors("test2@test.de", "Bruce", "Banner", "x", "Teststra.", "1", "33102", "Paderborn", "Birne", "2"));
        list.add(new Professors("test3@test.de", "Tony", "Stark", "x", "Teststraße", "1", "33103", "Paderborn", "Spinat", "3"));
    }
}
