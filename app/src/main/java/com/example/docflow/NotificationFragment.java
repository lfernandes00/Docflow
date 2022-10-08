package com.example.docflow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements NotificationAdapter.ItemClickListener, View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ImageButton backBtn;
    RecyclerView recyclerView;
    ArrayList<NotificationDataModel> dataholder;
    // TODO: Rename and change types of parameters

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataholder= new ArrayList<>();

        NotificationDataModel ob1 = new NotificationDataModel("Tem documentos para aprovar", "07/01/2022 15:24:45");
        dataholder.add(ob1);

        NotificationDataModel ob2 = new NotificationDataModel("Tem documentos para rever", "10/01/2022 11:20:00");
        dataholder.add(ob2);

        recyclerView.setAdapter(new NotificationAdapter(dataholder, this));

        backBtn.setOnClickListener(this);

        return view;
    }


    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.backBtn:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                break;
        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onItemClick(NotificationDataModel NotificationDataModel) {
        Log.d("msg", "delete notification");
    }
}