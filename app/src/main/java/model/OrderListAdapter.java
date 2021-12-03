package model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.a21q4_app_projekt.R;
import java.util.List;

import activity.DataViewActivity;
import activity.HomeActivity;
import activity.MyOrderActivity;
import activity.ProfActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import fontsUI.cairoTextView;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private List<Order> values;
    private Context context;

    private ProfManager profManager = ProfManager.getInstance();

    public OrderListAdapter(Context context, List<Order> values) {
        this.values = values;
        this.context = context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewRowItem = inflater.inflate(R.layout.row_item_order, parent, false);

        return new ViewHolder(viewRowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Order order = values.get(position);
        final Professors prof = profManager.getProf(order.getProfUID());

        holder.id_orderProf_name.setText(prof.getFirstName() + " " + prof.getLastName());
        holder.img_orderTutor_picture.setImageBitmap(prof.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "test", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), MyOrderActivity.class);
                intent.putExtra("order", order);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img_orderTutor_picture;
        public cairoTextView id_orderProf_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.img_orderTutor_picture = itemView.findViewById(R.id.img_orderTutor_picture);
            this.id_orderProf_name = itemView.findViewById(R.id.id_orderProf_name);
        }
    }
}
