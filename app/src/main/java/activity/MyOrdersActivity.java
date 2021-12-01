package activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Vector;

import Utility.NetworkChangeListener;
import fontsUI.cairoTextView;
import model.Order;
import model.OrderListAdapter;

public class MyOrdersActivity extends Activity {

    cairoTextView id_myOrders_TextView;

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    private RecyclerView recyclerView;
    private OrderListAdapter adapter;

    List<Order> orders;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id_myOrders_TextView = findViewById(R.id.id_myOrders_TextView);

        orders = new Vector<>();

        readOrders(db.collection("order").whereEqualTo("userUID", Auth.getCurrentUser().getUid()));

        recyclerView = findViewById(R.id.rvOrders);
        recyclerView.setHasFixedSize(false);    //erhöht etwas die Performance

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    private void readOrders(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    orders.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        orders.add(new Order(
                                document.get("userUID").toString(),
                                document.get("profUID").toString(),
                                document.get("street").toString(),
                                document.get("houseNumber").toString(),
                                document.get("plz").toString(),
                                document.get("city").toString(),
                                document.get("date").toString(),
                                document.get("description").toString()
                        ));
                        adapter = new OrderListAdapter(getApplicationContext(), orders);
                        recyclerView.setAdapter(adapter);
                        if(orders.size() == 1){
                            id_myOrders_TextView.setText(orders.size() + " Bestellung");
                        }else{
                            id_myOrders_TextView.setText(orders.size() + " Bestellungen");
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents: ", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
