package com.example.cnufirstmate.ui.workOrder;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cnufirstmate.MainActivity;
import com.example.cnufirstmate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkOrderFragment extends Fragment {

    private WorkOrderViewModel workOrderViewModel;
    Spinner dropdown;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workOrderViewModel =
                ViewModelProviders.of(this).get(WorkOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_work_order, container, false);

        dropdown = root.findViewById(R.id.reshall_spinner);
        setupSpinner();
        Button fab = root.findViewById(R.id.wosubmit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrder();

            }
        });

        return root;
    }

    //this method submits the order to the nosql database for review by admins
    public void submitOrder() {
        if(isWorkEmpty()){
            Toast.makeText(this.getContext(), "please fill out every field", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> orderMap = new HashMap<>();
        Spinner buildingSpinner = (Spinner) getView().findViewById(R.id.reshall_spinner);
        String buildingText = (String) buildingSpinner.getSelectedItem();

        TextInputLayout room = getView().findViewById(R.id.room);
        String roomText = room.getEditText().getText().toString();
        EditText editText = getView().findViewById(R.id.roomtext);
        editText.setText("", TextView.BufferType.EDITABLE);

        TextInputLayout issue = getView().findViewById(R.id.issue);
        String issueText = issue.getEditText().getText().toString();
        editText = getView().findViewById(R.id.issuetext);
        editText.setText("", TextView.BufferType.EDITABLE);

//        Date currentTime = Calendar.getInstance().getTime();
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this.getContext());
        String personName = acc.getDisplayName();
        String personEmail = acc.getEmail();
        String name = personName;
        String email = personEmail;
        orderMap.put("name", name);
        orderMap.put("email", email);
        String date = String.valueOf(System.currentTimeMillis());
        orderMap.put("date", date);
        orderMap.put("building", buildingText);
        orderMap.put("room", roomText);
        orderMap.put("issue", issueText);
        // Add a new document with a generated ID
         FirebaseAuth mAuth;
         FirebaseUser user;
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db.collection("orders")
                .add(orderMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText( getActivity(), "Submitted Work Order", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public boolean isWorkEmpty(){
        TextInputLayout room = getView().findViewById(R.id.room);
        String roomText = room.getEditText().getText().toString();
        TextInputLayout issue = getView().findViewById(R.id.issue);
        String issueText = issue.getEditText().getText().toString();

        return roomText.isEmpty() || issueText.isEmpty();
    }

    private void setupSpinner(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Santoro");
        spinnerArray.add("York");
        spinnerArray.add("Potomac");
        spinnerArray.add("James River");
        spinnerArray.add("Warwick");
        spinnerArray.add("Rappahannock");
        spinnerArray.add("Alpha Phi House");
        spinnerArray.add("Alpha Sigma Alpha House");
        spinnerArray.add("Phi Mu House");
        spinnerArray.add("Sigma Phi Epsilon House");
        spinnerArray.add("Tyler");
        spinnerArray.add("Taylor");
        spinnerArray.add("Wilson");
        spinnerArray.add("CNU Landing");
        spinnerArray.add("President Hall");
        spinnerArray.add("Madison");
        spinnerArray.add("Jefferson");
        spinnerArray.add("Washington");
        spinnerArray.add("Monroe");
        spinnerArray.add("Harrison");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        try {
            dropdown.setAdapter(adapter);
        }
        catch(Exception e){
            Toast.makeText( getActivity(), "bruh moment spinner error", Toast.LENGTH_LONG).show();
        }
    }
}
