package id.semmi.todoapp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by semmi on 29/01/2016.
 */
public class Notes extends RealmObject {
    @PrimaryKey
    private int id;
    private String note;
    private String date;

    public Notes(){}

    public Notes(int id, String note, String date) {
        this.id = id;
        this.note = note;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
