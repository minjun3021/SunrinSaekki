package com.kmj.sunrinsaekki.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kmj.sunrinsaekki.R;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class AddActivity extends AppCompatActivity {
    Toolbar mToolbar;
    Spinner spinner;
    ArrayList<String> categorys;
    ImageView check, addImage;
    ArrayAdapter<String> adapter;
    ArrayList<String> permissions;
    EditText name;
    String category;
    TextView addText;
    boolean isSelected;
    public static FirebaseDatabase database;
    public static DatabaseReference myRef1;
    public static DatabaseReference myRef2;
    Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        isSelected = false;
        name = findViewById(R.id.name_edit);
        mToolbar = findViewById(R.id.add_toolbar);
        spinner = findViewById(R.id.spinner);
        addImage = findViewById(R.id.add_image);
        check = findViewById(R.id.add_check);
        addText = findViewById(R.id.add_text);
        permissions = new ArrayList<>();
        category = "한식";
        categorys = new ArrayList<>();
        categorys.add("한식");
        categorys.add("중식");
        categorys.add("일식");
        categorys.add("분식");
        categorys.add("기타");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorys);
        spinner.setAdapter(adapter);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().replace(" ", "").equals("")) {
                    uploadFile(filePath);
                    database = FirebaseDatabase.getInstance();
                    myRef1 = database.getReference("Restaurants");
                    myRef2 = myRef1.child(name.getText().toString());
                    myRef2.child("category").setValue(category);

                } else {
                    Toast.makeText(AddActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.with(AddActivity.this)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {
                                isSelected = true;
                                filePath = uri;
                                addText.setVisibility(View.GONE);
                                Glide.with(AddActivity.this)
                                        .load(uri)
                                        .fitCenter()
                                        .into(addImage);
                            }
                        });
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categorys.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        checkPermission();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            String[] reqPermissionArray = new String[permissions.size()];
            reqPermissionArray = permissions.toArray(reqPermissionArray);
            ActivityCompat.requestPermissions(this, reqPermissionArray, 888);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length; i++) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다", Toast.LENGTH_SHORT).show();
                checkPermission();
            }

        }


    }

    private void uploadFile(Uri filePath) {
        if (filePath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://sunrinsaekki.appspot.com").child("images/" + name.getText().toString());
            storageRef.putFile(filePath)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "음식점 등록 완료!", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
