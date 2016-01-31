package id.semmi.todoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.semmi.todoapp.R;
import id.semmi.todoapp.model.Notes;

/**
 * Created by semmi on 29/01/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    public List<Notes> mNotes;
    private Context mContext;
    private  TickListener listener;
    private OnItemClickListener listenerItem;

    public interface OnItemClickListener{
        void onItemClickListener(View view , int position);
    }

    public interface TickListener{
         void onTickClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listenerItem){
        this.listenerItem = listenerItem;
    }

    public void setTickListener(TickListener listener){
        this.listener =listener;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

       public TextView notesName;
//       public Button deleteButton;
        public com.rey.material.widget.CheckBox checkedNotes;

       public ViewHolder(View itemView) {
           super(itemView);
           notesName = (TextView) itemView.findViewById(R.id.notesName);
//           deleteButton = (Button) itemView.findViewById(R.id.deleteNotes);
           checkedNotes = (com.rey.material.widget.CheckBox) itemView.findViewById(R.id.checkNotes);
           itemView.setOnClickListener(this);
//           deleteButton.setOnClickListener(this);
           checkedNotes.setOnCheckedChangeListener(this);
//            checkedNotes.setOnClickListener(this);
       }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();

            if(v.getId() == itemView.getId()){
                if(listenerItem !=null){
                    listenerItem.onItemClickListener(itemView,position);
                }
            }

//            if(v.getId() == deleteButton.getId()){
//                Toast.makeText(mContext, "BUtton on click"+notes.getNote(), Toast.LENGTH_SHORT).show();
//                if(listener!=null){
//                    listener.onTickClick(itemView,position);
//                }
//                return;
//            }
//            Toast.makeText(mContext, ""+notes.getNote(), Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getLayoutPosition();
            if(buttonView.getId() == checkedNotes.getId()){

                if(listener!=null){
                    listener.onTickClick(itemView,position);
                }
                if(isChecked ){
                    checkedNotes.setChecked(false);
                }
            }

        }
    }


    public NotesAdapter(Context context,List<Notes> notes){
        this.mContext = context;
        this.mNotes= notes;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View notesView = inflater.inflate(R.layout.list_notes, parent, false);
        return new ViewHolder(notesView);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position) {
        Notes note = mNotes.get(position);
        TextView textView = holder.notesName;
        textView.setText(note.getNote());

//        Button button = holder.deleteButton;
//        button.setText(note.getDate());
        com.rey.material.widget.CheckBox checkBox = holder.checkedNotes;

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


}
