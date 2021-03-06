package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21q4_app_projekt.R;

import java.util.List;

import activity.ProfActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import fontsUI.cairoTextView;

public class ProfListAdapter extends RecyclerView.Adapter<ProfListAdapter.ViewHolder> {

    private List<Professors> values;
    private Context context;
    private Activity mActivity;

    public ProfListAdapter(Context context, List<Professors> values, Activity mActivity) {
        this.values = values;
        this.context = context;
        this.mActivity = mActivity;
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

        holder.id_prof_name.setText(prof.getFirstName() + " " + prof.getLastName());
        holder.id_prof_city.setText(String.format("%s %s", prof.getPlz(), prof.getCity()));
        holder.rb_prof_rating.setRating(2.5f);
        holder.img_tutor_picture.setImageBitmap(prof.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfActivity.class);
                intent.putExtra("id", prof.getid());
                view.getContext().startActivity(intent);
                mActivity.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_tutor_picture;
        public cairoTextView id_prof_name;
        public cairoTextView id_prof_city;
        public RatingBar rb_prof_rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_tutor_picture = itemView.findViewById(R.id.img_tutor_picture);
            this.id_prof_name = itemView.findViewById(R.id.id_prof_name);
            this.id_prof_city = itemView.findViewById(R.id.id_prof_city);
            this.rb_prof_rating = itemView.findViewById(R.id.rb_prof_rating);
        }
    }
}
