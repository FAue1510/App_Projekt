package activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;

import com.example.a21q4_app_projekt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Utility.NetworkChangeListener;
import database.ProfSQLiteOpenHelper;
import model.Order;
import model.OrderListAdapter;
import model.Professors;

public class MyOrderActivity extends Activity {

    private RatingBar ratingBar;
    private Button rate;
    private ArrayList ratings;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Order order;
    OrderListAdapter orderListAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;

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
        setContentView(R.layout.activity_my_order);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ratingBar = findViewById(R.id.rb_prof_rating_order);
        rate = findViewById(R.id.b_rate);
        ratings = new ArrayList();

        if(getIntent().getExtras() != null){
            order = (Order) getIntent().getSerializableExtra("order");
        }

    }

    public void rateOnClick(View view){
        if (!order.isRated()){
            documentReference = db.collection("professors").document(order.getProfUID());
            readData(documentReference);
            ratings.add(ratingBar.getRating());
            documentReference.update("valuation", FieldValue.arrayUnion(ratings.toArray()));
            order.setRated(true);
        }
    }

    private void readData(DocumentReference documentReference) {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                        ratings.add(document.getData());
                    else{

                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}