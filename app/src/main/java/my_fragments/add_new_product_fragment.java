package my_fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackathon10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_new_product_fragment extends Fragment {

    private View myView;
    private EditText  productName,productPrice,productTag,productInitialStock,productType;
    private TextView selectImage;
    private Button submittbtn;
    private CircleImageView productImage;

    private FirebaseAuth mauth;
    private DatabaseReference rootRef,retailer_product_ref;
    private String currentuserId,saveCurrentDate,saveCurrentTime;
    public static double product_id = 0001;
    private static final int GalleryPix =1;

    private Uri imageUri , resultUri;
    private Dialog mDialog;
    String downloadUrl;



    public add_new_product_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       myView= inflater.inflate(R.layout.fragment_add_new_product_fragment, container, false);

       initialize(myView);
       submittbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               String mProductName = productName.getText().toString();
               String mProductPrice = productPrice.getText().toString();
               String mProductTag = productTag.getText().toString();
               String mProductInitialStock = productInitialStock.getText().toString();
               String mProductType = productType.getText().toString();
               saveAddNewProductInfo(mProductType,mProductName,mProductPrice,mProductTag,mProductInitialStock);
           }
       });

//       selectImage.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               selectProductImage();
//           }
//       });

       return  myView;
    }

//    private void selectProductImage() {
//        Intent obj = new Intent();
//        obj.setType("image/*");
//        obj.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(obj , GalleryPix);
//    }

    private void saveAddNewProductInfo(String mProductType,String mProductName,
                                       String mProductPrice,
                                       String mProductTag, String mProductInitialStock)
    {
        if (TextUtils.isEmpty(mProductType))
        {
            productType.setError("Enter Product Type");
            return;
        }
        else
        if (TextUtils.isEmpty(mProductName))
        {
            productName.setError("Enter Product Name");
            return;
        }
        else  if (TextUtils.isEmpty(mProductPrice))
        {
            productPrice.setError("Enter Product Price");
            return;
        }
        else  if (TextUtils.isEmpty(mProductTag))
        {
            productTag.setError("Enter Product Tag");
            return;
        }
        else  if (TextUtils.isEmpty(mProductInitialStock))
        {
            productInitialStock.setError("Enter ProductInitialStock");
            return;
        }
        else
        {
             DatabaseReference tempRef = retailer_product_ref
                    .child(mProductType). child(currentuserId);


            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            saveCurrentDate = currentDate.format(Calendar.getInstance().getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            saveCurrentTime = currentTime.format(Calendar.getInstance().getTime());
            String randomKey = currentuserId + saveCurrentDate+ saveCurrentTime;


            Map map = new HashMap();
            map.put("product_type",mProductType);
            map.put("product_name",mProductName);
            map.put("product_id",product_id);
            map.put("product_price",mProductPrice);
            map.put("product_item_stock",mProductInitialStock);
            map.put("product_image"," ");
            map.put("date",saveCurrentDate);
            map.put("time",saveCurrentTime);

            product_id++;

            tempRef.child(randomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful())
                    {
                        //  productName,productPrice,productTag,productInitialStock,productType
                productName.setText("");
                productPrice.setText("");
                productTag.setText("");
                productInitialStock.setText("");
                productType.setText("");
                        Toast.makeText(getContext(),
                                "products added", Toast.LENGTH_SHORT).show();

                        // disaapear the fragment here


                    }
                    else
                    {
                        Toast.makeText(getContext(), "Product couldnt upload", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode== GalleryPix && resultCode == RESULT_OK
//                && data!=null  && data.getData()!=null) {
//            imageUri = data.getData();
//            productImage.setImageURI(imageUri);
//
//            if(resultCode==RESULT_OK)
//            {
//                  saveImageToDatabaseStorage(imageUri);
//            }
//
//        }
//
//
//
//    }

//    private void saveImageToDatabaseStorage(Uri imageUri) {
//
//        StorageReference rootRef = FirebaseStorage.getInstance().getReference();
//       final  StorageReference filePath = rootRef.child(currentuserId+".jpg");
//
//        filePath.putFile(resultUri).addOnCompleteListener
//                (new OnCompleteListener<UploadTask.TaskSnapshot>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                        if(task.isSuccessful()) {
//
//                            mDialog.dismiss();
//
//                            //  getting the image url and saving it to the
//                            //  firebase database and confirming it
//
//                            filePath.getDownloadUrl().addOnSuccessListener
//                                    (new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//
//                                            // uri returns the url of the image
//                                            downloadUrl = uri.toString();
//
//                                            mDialog.dismiss();
//
//                                            System.out.println("download url is  :"+ downloadUrl );
//
//                                        }
//                                    }) ;
//                        }
//                    }
//                });
//
//
//    }


    private void initialize(View myView)
    {
        productName = (EditText) myView.findViewById(R.id.id_product_name);
        productPrice = (EditText) myView.findViewById(R.id.id_price);
        productTag = (EditText) myView.findViewById(R.id.id_tags);
        productInitialStock = (EditText) myView.findViewById(R.id.id_initial_stock);
        productType =  (EditText) myView.findViewById(R.id.id_product_type);
        selectImage =(TextView) myView.findViewById(R.id.id_select_image_btn);
        productImage =(CircleImageView) myView.findViewById(R.id.id_product_image) ;
        submittbtn = (Button) myView.findViewById(R.id.id_submit_btn_add_frag);

        mauth=FirebaseAuth.getInstance();
        currentuserId=mauth.getCurrentUser().getUid();
        rootRef=FirebaseDatabase.getInstance().getReference();
        retailer_product_ref = rootRef.child("All_retailer_product");

        mDialog = new ProgressDialog(getContext());
    }



}
