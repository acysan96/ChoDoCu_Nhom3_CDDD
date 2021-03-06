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
import android.widget.EditText;
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

public class ShipperLichSuChiTietDonGiaoActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP, imgShipper;
    private TextView txtMaVanDon, txtTenSP, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtShipperUserName, txtHoTenShipper, txtSDTShipper, txtDiaChiShipper;
    private EditText edtHoTenNguoiMua, edtDiaChi, edtLienHe;
    private Button btnBack;
    private Intent intent;
    private String userName, donHangID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_lich_su_chi_tiet_don_giao);

        btnBack = (Button) findViewById(R.id.btnBack);
        edtHoTenNguoiMua = (EditText) findViewById(R.id.edtHoTenNguoiMua);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtMaVanDon = (TextView) findViewById(R.id.txtMaVanDon);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtShipperUserName = (TextView) findViewById(R.id.txtShipperUserName);
        txtHoTenShipper = (TextView) findViewById(R.id.txtHoTenShipper);
        txtSDTShipper = (TextView) findViewById(R.id.txtSDTShipper);
        txtDiaChiShipper = (TextView) findViewById(R.id.txtDiaChiShipper);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        imgShipper = (ImageView) findViewById(R.id.imgShipper);

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){

            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");

            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){

                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShipperLichSuChiTietDonGiaoActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtMaVanDon.setText("M?? v???n ????n: " + snapshot.getValue(DatHang.class).getDonHangID());
                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){
                            txtTenSP.setText("T??n s???n ph???m: " + snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText("T??n s???n ph???m: " + snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                            txtPhuongThucThanhToan.setText("Giao h??ng COD");
                        }
                        else if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                            txtPhuongThucThanhToan.setText("Thanh to??n E-Wallet");
                        }
                        txtTongGiaTriDonHang.setText("T???ng ti???n: " + snapshot.getValue(DatHang.class).getGiaTien() + "vnd");
                        txtSoLuongSP.setText("S??? l?????ng: " + snapshot.getValue(DatHang.class).getSanPham().getSoLuong() + " x ");
                        txtGiaSP.setText(snapshot.getValue(DatHang.class).getSanPham().getGiaTien() + "vnd");
                        String nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        String shipperID = snapshot.getValue(DatHang.class).getShipperID();
                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                    edtHoTenNguoiMua.setText(snapshot.getValue(UserData.class).getHoTen());
                                    edtDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                                    edtLienHe.setText(snapshot.getValue(UserData.class).getSoDienThoai());
                                }
                                else if(snapshot.getValue(UserData.class).getUserID().equals(shipperID)){
                                    if(!snapshot.getValue(UserData.class).getImage().isEmpty()){
                                        storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(ShipperLichSuChiTietDonGiaoActivity.this).load(uri).into(imgShipper);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    txtShipperUserName.setText("Shipper user name: " + snapshot.getValue(UserData.class).getUserName());
                                    txtHoTenShipper.setText("H??? t??n: " + snapshot.getValue(UserData.class).getHoTen());
                                    txtSDTShipper.setText("S??? ??i???n tho???i: " + snapshot.getValue(UserData.class).getSoDienThoai());
                                    txtDiaChiShipper.setText("?????a ch???: " + snapshot.getValue(UserData.class).getDiaChi());

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
            intent = new Intent(v.getContext(), ShipperLichSuDonGiaoActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}