package com.example.cnufirstmate.ui.workOrder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cnufirstmate.R;

import java.util.ArrayList;
import java.util.List;

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

        return root;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.logout:
//                logOut();
//                break;
//        }
//        return true;
//    }
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
