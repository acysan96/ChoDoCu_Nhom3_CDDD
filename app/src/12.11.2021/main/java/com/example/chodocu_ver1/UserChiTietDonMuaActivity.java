package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserChiTietDonMuaActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private TextView txtTenSP, txtMaVanDon, txtSoLuongSP, txtGiaSP, txtSellerUserName, txtNgayMuaHang, txtPhuongThucThanhToan, txtDiaChi, txtLienHe, txtTongGiaTriDonHang;
    private ImageView imgSP, imgSellerUser;
    private Button btnBack;
    private String userID, userName, orderID;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_chi_tiet_don_mua_layout);

        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtMaVanDon = (TextView) findViewById(R.id.txtMaVanDon);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtSellerUserName = (TextView) findViewById(R.id.txtSellerUserName);
        txtNgayMuaHang = (TextView) findViewById(R.id.txtNgayMuaHang);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        imgSellerUser = (ImageView) findViewById(R.id.imgSellerUser);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            orderID = getIntent().getExtras().getString("OrderID");

            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(orderID)){
                        String nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        if(snapshot.getValue(DatHang.class).getTinhTrang() != -1){
                            txtTongGiaTriDonHang.setText("S??? ti???n ???? thanh to??n: " + String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vn??");
                        }
                        else{
                            txtTongGiaTriDonHang.setText("????n ???? h???y");
                        }

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 1){
                            txtPhuongThucThanhToan.setText("Thanh to??n tr???c ti???p!");
                        }
                        else if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                            txtPhuongThucThanhToan.setText("Thanh to??n COD!");
                        }
                        else if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                            txtPhuongThucThanhToan.setText("Thanh to??n qua v?? ??i???n t???!");
                        }

                        txtMaVanDon.setText("M?? v???n ????n: " + snapshot.getValue(DatHang.class).getDonHangID());

                        txtNgayMuaHang.setText("Ng??y order: " + snapshot.getValue(DatHang.class).getNgayTaoDonHang());

                        txtSoLuongSP.setText("S??? l?????ng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + "x");

                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserChiTietDonMuaActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtGiaSP.setText(Long.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vn??");
                        txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP());

                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(UserChiTietDonMuaActivity.this).load(uri).into(imgSellerUser);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    txtDiaChi.setText("?????a ch???: " + snapshot.getValue(UserData.class).getDiaChi());
                                    txtLienHe.setText("Li??n h???: " + snapshot.getValue(UserData.class).getSoDienThoai());
                                    txtSellerUserName.setText(snapshot.getValue(UserData.class).getHoTen());
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

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), User_LichSu_GiaoDichActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}