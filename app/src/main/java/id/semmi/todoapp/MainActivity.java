package id.semmi.todoapp;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.List;

import id.semmi.todoapp.adapter.AddNoteAdapter;
import id.semmi.todoapp.adapter.EditNoteAdapter;
import id.semmi.todoapp.adapter.NotesAdapter;
import id.semmi.todoapp.model.Notes;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Realm myRealm;
    private NotesAdapter mAdapter;
    private List<Notes> notes;
    private CoordinatorLayout coordinatorLayout;
    private boolean flag = false;
    private  String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyvleNotes);
        myRealm = Realm.getInstance(this);
//        setDummyData();


        // show all data
         RealmResults<Notes> results = myRealm.where(Notes.class)
                                            .isNotNull("note")
                                            .findAll();
        notes = results;
        for (Notes note : results){
            Log.d("tag", "onCreate: "+note.getId());
        }
        // create Adapater
        mAdapter = new NotesAdapter(this,results);
        rv.setAdapter(mAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new LandingAnimator());
        fab.setOnClickListener(this);

        mAdapter.setTickListener(new NotesAdapter.TickListener() {
            @Override
            public void onTickClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "Try gan "+notes.get(position).getNote(), Toast.LENGTH_SHORT).show();
                final int id = notes.get(position).getId();
                note = notes.get(position).getNote();
                final String date = notes.get(position).getDate();

                myRealm.beginTransaction();
                Notes results = myRealm.where(Notes.class)
                        .equalTo("id", id)
                        .findFirst();
                results.removeFromRealm();
                myRealm.commitTransaction();
                // Delete the data from adapter list
                mAdapter.notifyItemRemoved(id - 1);
                // have some snackbar for undo task
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "" + note + " telah selesai ", Snackbar.LENGTH_SHORT);
                snackbar.show();
                flag = true;
            }
        });
        mAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                EditNoteAdapter editNoteAdapter;
                String currentText = notes.get(position).getNote();
                final int id = notes.get(position).getId();
                editNoteAdapter = new EditNoteAdapter(MainActivity.this, currentText);
                final DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                        .setAdapter(editNoteAdapter)
                        .setHeader(R.layout.header_view_edit)
                        .setGravity(Gravity.CENTER)
                        .create();
                dialogPlus.show();

                editNoteAdapter.setSubmitListener(new EditNoteAdapter.SubmitListener() {
                    @Override
                    public void onSubmitListener(View view, String noteText) {
                        myRealm.beginTransaction();
                        Notes result = myRealm.where(Notes.class)
                                .equalTo("id", id)
                                .findFirst();
                        result.setNote(noteText);
                        myRealm.copyToRealmOrUpdate(result);
                        myRealm.commitTransaction();
                        mAdapter.notifyItemRemoved(id - 1);
                        mAdapter.notifyItemChanged(id - 1);
                        dialogPlus.dismiss();
                    }
                });
            }
        });



    }

    private void setDummyData() {
        // first method
        myRealm.beginTransaction();
        Notes notes = myRealm.createObject(Notes.class);
        notes.setId(1);
        notes.setNote("Hello world");
        notes.setDate("21212");
        myRealm.commitTransaction();

        // second method
        Notes notes2nd = new Notes(2,"cara ke dua","9090");
        myRealm.beginTransaction();
        Notes realmNotes = myRealm.copyToRealm(notes2nd);
        myRealm.commitTransaction();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.fab:
//                Notes notes2nd = new Notes(setNoteId(),"insert new Data","9090");
//                myRealm.beginTransaction();
//                Notes realmNotes = myRealm.copyToRealm(notes2nd);
//                myRealm.commitTransaction();
//                mAdapter.notifyItemInserted(notes.size() - 1);

                AddNoteAdapter noteAdapter = new AddNoteAdapter(this);

                final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                        .setAdapter(noteAdapter)
                        .setHeader(R.layout.header_view)
                        .create();
                dialogPlus.show();

                noteAdapter.setSubmitListener(new AddNoteAdapter.SubmitListener() {
                    @Override
                    public void onSubmitListener(View view, String noteText) {
                        if(noteText.equals("")){
                            Toast.makeText(MainActivity.this, "No Empty Note", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        insertData(noteText);
                        dialogPlus.dismiss();
                        Toast.makeText(MainActivity.this, ""+noteText, Toast.LENGTH_SHORT).show();
                    }
                });

        }
    }

    private void insertData(String noteText) {
        Notes notes2nd = new Notes(setNoteId(),noteText,"date");
        myRealm.beginTransaction();
        Notes realmNotes = myRealm.copyToRealm(notes2nd);
        myRealm.commitTransaction();
        mAdapter.notifyItemInserted(getMaxId() - 1);
    }

    private int setNoteId(){
        if(myRealm.where(Notes.class).count() == 0){
            return 1;
        }
        return myRealm.where(Notes.class).max("id").intValue()+1;
    }
    private int getMaxId(){
        return myRealm.where(Notes.class).max("id").intValue();
    }



}
