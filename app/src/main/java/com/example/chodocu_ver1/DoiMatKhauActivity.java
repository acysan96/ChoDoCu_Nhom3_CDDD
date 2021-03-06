package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DoiMatKhauActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userDataArrayList;
    private Button btnPasswordSave, btnBack;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Intent intent;
    private String sOldPassword, sNewPassword, sConfirmPassword, sUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.doi_matkhau_layout);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnPasswordSave = findViewById(R.id.btnPasswordSave);

        userDataArrayList = new ArrayList<>();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userDataArrayList.add(snapshot.getValue(UserData.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack.setOnClickListener(backClick);
        btnPasswordSave.setOnClickListener(saveClick);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");

        }
    }

    public boolean oldPasswordCheck(ArrayList<UserData> userDataArrayList, String sUserName, String sPassword){
        for(UserData userData : userDataArrayList){
            if(userData.getUserName().equals(sUserName) && userData.getPassword().equals(sPassword)){
                return true;
            }
        }
        return false;
    }

    View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sOldPassword = edtOldPassword.getText().toString();
            sNewPassword = edtNewPassword.getText().toString();
            sConfirmPassword = edtConfirmPassword.getText().toString();
            if(sOldPassword.isEmpty()){
                edtOldPassword.setError("B???n ch??a nh???p m???t kh???u c??!");
            }
            else if (sNewPassword.isEmpty()) {
                edtNewPassword.setError("B???n ch??a nh???p m???t kh???u m???i!");
            }
            else if(sNewPassword.length() < 6) {
                edtNewPassword.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!");
            }
            else if(sNewPassword.equals(sOldPassword)) {
                edtNewPassword.setError("M???t kh???u m???i ph???i kh??c m???t kh???u c??!");
            }
            else if (sConfirmPassword.isEmpty()) {
                edtConfirmPassword.setError("B???n ch??a x??c nh???n m???t kh???u!");
            }
            else if(sConfirmPassword.length() < 6) {
                edtConfirmPassword.setError("M???t kh???u ph???i c?? ??t nh???t 6 k?? t???!");
            }
            else if(!sNewPassword.equals(sConfirmPassword)) {
                edtConfirmPassword.setError("M???t kh???u x??c nh???n ch??a ch??nh x??c!");
            }
            else if(oldPasswordCheck(userDataArrayList, sUserName, edtOldPassword.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("B???n nh???p m???t kh???u c?? ch??a ch??nh x??c!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else{
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                user.updatePassword(sNewPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DoiMatKhauActivity.this, "?????i password th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPassword().equals(sOldPassword)){
                                                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which){
                                                            case DialogInterface.BUTTON_POSITIVE:
                                                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),
                                                                        snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
                                                                        snapshot.getValue(UserData.class).getDiaChi(), sConfirmPassword, snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia()
                                                                        , snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail()
                                                                        ,snapshot.getValue(UserData.class).getCmndMatTruoc(),snapshot.getValue(UserData.class).getPermission(),
                                                                        snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                                        snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), snapshot.getValue(UserData.class).getMoney());
                                                                databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                                                intent = new Intent(DoiMatKhauActivity.this, DangNhapActivity.class);
                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                finish();
                                                                startActivity(intent);
                                                                //Toast.makeText(DoiMatKhauActivity.this,  "C???p nh???t m???t kh???u th??nh c??ng!", Toast.LENGTH_SHORT).show();
                                                                break;
                                                            case DialogInterface.BUTTON_NEGATIVE:
                                                                return;

                                                        }
                                                    }
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                                builder.setMessage("B???n mu???n ?????i m???t kh???u?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();

                                            }
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });
//                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                        if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPassword().equals(sOldPassword)){
//                            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which){
//                                        case DialogInterface.BUTTON_POSITIVE:
//                                            UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),
//                                                    snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
//                                                    snapshot.getValue(UserData.class).getDiaChi(), sConfirmPassword, snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia()
//                                                    , snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getPermission(),
//                                                    snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
//                                                    snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), snapshot.getValue(UserData.class).getMoney());
//                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
//
//                                            intent = new Intent(DoiMatKhauActivity.this, DangNhapActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            finish();
//                                            startActivity(intent);
//                                            Toast.makeText(DoiMatKhauActivity.this,  "C???p nh???t m???t kh???u th??nh c??ng!", Toast.LENGTH_SHORT).show();
//                                            break;
//                                        case DialogInterface.BUTTON_NEGATIVE:
//                                            return;
//
//                                    }
//                                }
//                            };
//                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                            builder.setMessage("B???n mu???n ?????i m???t kh???u?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DoiMatKhauActivity.this.finish();
//            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 1){
//                        Intent intent = new Intent(v.getContext(), UserMainActivity.class);
//                        intent.putExtra("UserName", sUserName);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        finish();
//                        startActivity(intent);
//                    }
//                    else if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 0 || snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 2){
//                        Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
//                        intent.putExtra("UserName", sUserName);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        finish();
//                        startActivity(intent);
//                    }
//                    else if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 3 || snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 4){
//                        Intent intent = new Intent(v.getContext(), ShipperMainActivity.class);
//                        intent.putExtra("UserName", sUserName);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        finish();
//                        startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }
    };
}