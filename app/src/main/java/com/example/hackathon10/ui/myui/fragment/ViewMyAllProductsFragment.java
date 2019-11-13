package com.example.hackathon10.ui.myui.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hackathon10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMyAllProductsFragment extends Fragment {


    private View view;
    ListView listView;
    String []myProducts;
    ArrayList<String> list = new ArrayList<>();

    DatabaseReference cuurent_user_ref;


    int i , j;
    public ViewMyAllProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_my_all_products, container, false);

        listView = view.findViewById(R.id.id_all_data_list_view);

        getAllTheValues();


        return view;
    }

    private void getAllTheValues() {

        DatabaseReference all_ret = FirebaseDatabase.getInstance()
                .getReference("All_retailer_product");
        final String currentUserId = FirebaseAuth.getInstance()
                .getCurrentUser().getUid();

        cuurent_user_ref = all_ret.child(currentUserId);

        DatabaseReference temp = all_ret.child(currentUserId);

        temp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists())
                {

                    Iterable<DataSnapshot> it= dataSnapshot.getChildren();

                    Iterator<DataSnapshot> itr  =it.iterator();

                    System.out.println("before while");
                    while (itr.hasNext())
                    {
                        System.out.println("inside while");
                        DataSnapshot snapshot = itr.next();
                        System.out.println("keys are " + snapshot.getKey());
                        list.add(snapshot.getKey());
                    }

                    myProducts = new String[list.size()];
                    for( i = 0 ; i < myProducts.length ; i++)
                    {
                        myProducts[i] = list.get(i);
                        System.out.println("products array "+ myProducts[i]);
                    }

                    String temp2;
                    for( i = 0 ; i<myProducts.length ; i++)
                    {
                        temp2 = myProducts[i];
                        System.out.println("value of temp 2  =="+ temp2);
                        // storig thr product tyoe like bike  , pen
                        DatabaseReference x =FirebaseDatabase.getInstance()
                                .getReference("All_retailer_product")
                                .child(currentUserId).child(temp2);

                        x.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Iterable<DataSnapshot> it= dataSnapshot.getChildren();

                                Iterator<DataSnapshot> itr  =it.iterator();

                                ArrayList<String>list2 = new ArrayList<>();
                                String []uniqID;

                                while (itr.hasNext())
                                {
                                    DataSnapshot snapshot = itr.next();
                                    System.out.println("keys are please find" + snapshot.getKey());
                                    list2.add(snapshot.getKey());
                                }


                                uniqID = new String[list2.size()];
                                for( j = 0 ; j<uniqID.length ; j++)
                                {
                                    uniqID[j] = list2.get(i);
                                    // uniq id of the bikes or scooty

                                    String w = myProducts[j];
                                    String z = uniqID[j];
                                    fetchData(w , z);

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    myProducts = new String[list.size()];
                    for( i = 0 ; i < myProducts.length ; i++)
                    {
                        myProducts[i] = list.get(i);
                        System.out.println("products array "+ myProducts[i]);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void fetchData(String bike, String uniqId) {

        DatabaseReference temp = FirebaseDatabase.getInstance().getReference("All_retailer_product")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .child(bike).child(uniqId);

        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("product_name").getValue().toString();
                    System.out.println("product name is "+ name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
