package id.semmi.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import id.semmi.todoapp.R;

/**
 * Created by semmi on 30/01/2016.
 */
public class EditNoteAdapter extends BaseAdapter implements View.OnClickListener {


    private Context mContext;
    private SubmitListener listener;
    private String noteText;
    private String currentText;



    public interface SubmitListener{
        void onSubmitListener(View view, String noteText);
    }

    public void setSubmitListener(SubmitListener listener){
        this.listener = listener;
    }


    public EditNoteAdapter(Context context, String currentText){
        this.mContext= context;
        this.currentText=currentText;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_edit_note, parent, false);
            holder.noteText = (EditText) convertView.findViewById(R.id.editNote);
            holder.editNote = (Button) convertView.findViewById(R.id.buttonEdit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.noteText.setText(currentText);

        holder.editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteText = holder.noteText.getText().toString();
                if(listener!= null){
                    listener.onSubmitListener(v,noteText);
                }
            }
        });


        return convertView;
    }

    @Override
    public void onClick(View v) {

        if(listener!= null){
            listener.onSubmitListener(v,noteText);
        }
    }

    class ViewHolder{
        EditText noteText;
        Button  editNote;
    }
}
