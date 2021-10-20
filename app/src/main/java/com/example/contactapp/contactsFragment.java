package com.example.contactapp;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link contactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class contactsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ArrayList<dataModelOfContact> contactList;
    EditText editText;
    customAdapterOfContacts adapterOfContacts;
    ArrayList<dataModelOfContact> filteredList =new ArrayList<>();
    Button addButton;
    public contactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment contactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static contactsFragment newInstance(String param1, String param2) {
        contactsFragment fragment = new contactsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.listofcontacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactList = new ArrayList<>();
        // filter list
        //filteredList= new ArrayList<>();
        editText = view.findViewById(R.id.searchText);
        addButton = view.findViewById(R.id.addContact);
        // add contact using intent
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(),"hello guys",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                startActivity(intent);
            }
        });

        ContentResolver contentResolver = getContext().getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        // contacts reading
        if (cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                //int img = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(new dataModelOfContact(0,name,number));
            }
        }
        Collections.sort(contactList, new NameSorter());

        // implement searching with text listener

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                filteredList.clear();
                if (s.toString().isEmpty()){
                    recyclerView.setAdapter(new customAdapterOfContacts(contactList,getActivity().getApplicationContext()));
                }
                else{
                    filter(s.toString());
                }

            }
        });

        recyclerView.setAdapter(new customAdapterOfContacts(contactList,getActivity().getApplicationContext()));

        return  view;
    }

    private void filter(String text) {
        for (dataModelOfContact item:contactList)
        {
            if (item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);

            }
        }
        recyclerView.setAdapter(new customAdapterOfContacts(filteredList,getActivity().getApplicationContext()));
    }
   

}