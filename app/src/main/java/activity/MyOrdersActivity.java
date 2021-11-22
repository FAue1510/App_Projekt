package activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import model.Order;

public class MyOrdersActivity extends Activity {

    FirebaseAuth Auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        readOrders(db.collection("orders").whereEqualTo("userUID", Auth.getCurrentUser().getUid()));
    }

    private void readOrders(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        orders.add(new Order(
                                document.get("userUID").toString(),
                                document.get("profUID").toString(),
                                document.get("city").toString(),
                                document.get("street").toString(),
                                document.get("houseNumber").toString(),
                                document.get("plz").toString(),
                                document.get("description").toString()
                        ));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents: ", Toast.LENGTH_LONG);
                }
            }
        });
    }
}
