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

public class UserCodEWalletOrderActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private EditText edtUserName, edtDiaChi, edtLienHe;
    private TextView txtTenSP, txtMaVanDon, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtNameLabel, txtDiaChi, txtShipperLabel, txtShipperName, txtShipperPhone;
    private Button btnBack;
    private Intent intent;
    private String userName, userID, donHangID, nguoiBanID, nguoiMuaID, productID;
    private int loaiDonHang, sellerCommission;
    private long tongGiaTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.vidientu_cod_user);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtMaVanDon = (TextView) findViewById(R.id.txtMaVanDon);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtNameLabel = (TextView) findViewById(R.id.txtNameLabel);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtShipperLabel = (TextView) findViewById(R.id.txtShipperLabel);
        txtShipperPhone = (TextView) findViewById(R.id.txtShipperPhone);
        txtShipperName = (TextView) findViewById(R.id.txtShipperName);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");

            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();
                        productID = snapshot.getValue(DatHang.class).getSanPham().getiD();
                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserCodEWalletOrderActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtMaVanDon.setText("M?? v???n ????n: " + snapshot.getValue(DatHang.class).getDonHangID());

                        if(snapshot.getValue(DatHang.class).getTinhTrang() == 0){
                            txtShipperLabel.setText("T??nh tr???ng: Ch??? x??c nh???n");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 1){
                            txtShipperLabel.setText("T??nh tr???ng: ??ang ????ng g??i");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 2){
                            txtShipperLabel.setText("T??nh tr???ng: ????ng g??i ho??n t???t");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 3){
                            txtShipperLabel.setText("T??nh tr???ng: Ch??? v???n chuy???n");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 3){
                            txtShipperLabel.setText("T??nh tr???ng: Shipper ??ang l???y h??ng");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 3){
                            txtShipperLabel.setText("T??nh tr???ng: Shipper l???y h??ng th??nh c??ng");
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() == 3){
                            txtShipperLabel.setText("T??nh tr???ng: ??ang giao h??ng");

                        }

                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("S??? l?????ng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vn??");
                        txtTongGiaTriDonHang.setText("T???ng ti???n: " + String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vn??");

                        String diaChi = snapshot.getValue(DatHang.class).getDiaChi();
                        String lienHe = snapshot.getValue(DatHang.class).getSoDienThoai();

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 1){
                            txtPhuongThucThanhToan.setText("Thanh to??n tr???c ti???p!");

                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                        sellerCommission = snapshot.getValue(UserData.class).getHoaHong();
                                        edtUserName.setText(snapshot.getValue(UserData.class).getHoTen());
                                        edtDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                                        edtLienHe.setText(snapshot.getValue(UserData.class).getSoDienThoai());
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
                        else if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2 || snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                            if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                                txtPhuongThucThanhToan.setText("Thanh to??n COD!");

                            }
                            else{
                                txtPhuongThucThanhToan.setText("Thanh to??n E-Wallet!");
                            }

                            txtDiaChi.setText("?????a ch??? giao h??ng: ");
                            txtNameLabel.setText("H??? t??n ng?????i mua: ");
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                        edtUserName.setText(snapshot.getValue(UserData.class).getHoTen());
                                        edtDiaChi.setText(diaChi);
                                        edtLienHe.setText(lienHe);
                                    }
                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                        sellerCommission = snapshot.getValue(UserData.class).getHoaHong();
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
            intent = new Intent(v.getContext(), DonMuaActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}