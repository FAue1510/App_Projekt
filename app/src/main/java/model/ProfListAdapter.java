package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21q4_app_projekt.R;

import java.util.List;

public class ProfListAdapter extends RecyclerView.Adapter<ProfListAdapter.ViewHolder> {

    private List<Professors> values;
    private Context context;

    public ProfListAdapter(Context context, List<Professors> values) {
        this.values = values;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewRowItem = inflater.inflate(R.layout.row_item_prof, parent, false);

        return new ViewHolder(viewRowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Professors prof = values.get(position);

        holder.tvName.setText(prof.getFirstName());
        holder.tvAdress.setText(String.format("%s %s, %s, %s", prof.getStreet(), prof.getHouseNumber(),
                                                                prof.getPlz(), prof.getCity()));
        holder.tvSubject.setText(prof.getSubject());
        holder.tvEmail.setText(prof.getBirthday());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Klicken funktioniert", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvAdress;
        public TextView tvSubject;
        public TextView tvEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvAdress = itemView.findViewById(R.id.tvAdress);
            this.tvSubject = itemView.findViewById(R.id.tvSubject);
            this.tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
