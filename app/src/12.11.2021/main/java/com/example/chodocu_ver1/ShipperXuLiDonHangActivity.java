package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class ShipperXuLiDonHangActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtHoTenNguoiMua, txtDiaChi, txtLienHe, txtTongGiaTriDonHang, txtSellerLabel, txtSellerDiaChi, txtSellerSDT;
    private CheckBox cbLayHangThanhCong, cbDangGiaoHang;
    private Button btnGiaoThanhCong, btnGiaoThatBai, btnBack;
    private String userName, userID, donHangID, nguoiMuaID, nguoiBanID;
    private int loaiDonHang = 0, commission = 0, truTienCheck = 0, hoanTienCheck = 0;
    private long tongGiaTri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_xu_li_don_hang_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtHoTenNguoiMua = (TextView) findViewById(R.id.txtHoTenNguoiMua);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtSellerLabel = (TextView) findViewById(R.id.txtSellerLabel);
        txtSellerDiaChi = (TextView) findViewById(R.id.txtSellerDiaChi);
        txtSellerSDT = (TextView) findViewById(R.id.txtSellerSDT);
        cbLayHangThanhCong = (CheckBox) findViewById(R.id.cbLayHangThanhCong);
        cbDangGiaoHang = (CheckBox) findViewById(R.id.cbDangGiaoHang);
        btnGiaoThatBai = (Button) findViewById(R.id.btnGiaoThatBai);
        btnGiaoThanhCong = (Button) findViewById(R.id.btnGiaoThanhCong);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        cbDangGiaoHang.setOnCheckedChangeListener(giaoHangCheck);
        cbLayHangThanhCong.setOnCheckedChangeListener(layHangThanhCongCheck);
        btnGiaoThatBai.setOnClickListener(giaoThatBaiClick);
        btnGiaoThanhCong.setOnClickListener(giaoThanhCongClick);
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
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();

                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShipperXuLiDonHangActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 1){
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("S??? l?????ng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vn??");
                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                            txtTongGiaTriDonHang.setText("???? thanh to??n");
                        }
                        else{
                            txtTongGiaTriDonHang.setText("S??? ti???n c???n thu: " + String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vn??");
                        }
                        txtDiaChi.setText("?????a ch???: " + snapshot.getValue(DatHang.class).getDiaChi());
                        txtLienHe.setText("Li??n h???: " + snapshot.getValue(DatHang.class).getSoDienThoai());

                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                    txtHoTenNguoiMua.setText("H??? t??n ng?????i nh???n: " + snapshot.getValue(UserData.class).getHoTen());

                                }
                                else if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                    commission = snapshot.getValue(UserData.class).getHoaHong();
                                    txtSellerLabel.setText("H??? t??n ng?????i b??n: " + snapshot.getValue(UserData.class).getHoTen());
                                    txtSellerDiaChi.setText("?????a ch??? l???y h??ng: " + snapshot.getValue(UserData.class).getDiaChi());
                                    txtSellerSDT.setText("Li??n h???: " + snapshot.getValue(UserData.class).getSoDienThoai());
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

                        if(snapshot.getValue(DatHang.class).getTinhTrang() < 5){
                            cbLayHangThanhCong.setChecked(false);
                            cbLayHangThanhCong.setEnabled(true);
                            cbDangGiaoHang.setChecked(false);
                            txtHoTenNguoiMua.setVisibility(View.GONE);
                            txtDiaChi.setVisibility(View.GONE);
                            txtLienHe.setVisibility(View.GONE);
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() < 6){
                            cbDangGiaoHang.setChecked(false);
                            cbDangGiaoHang.setEnabled(true);

                            txtSellerLabel.setVisibility(View.GONE);
                            txtSellerDiaChi.setVisibility(View.GONE);
                            txtSellerSDT.setVisibility(View.GONE);
                        }

                        if(snapshot.getValue(DatHang.class).getTinhTrang() >= 6){
                            btnGiaoThanhCong.setVisibility(View.VISIBLE);
                            btnGiaoThatBai.setVisibility(View.VISIBLE);

                            txtSellerLabel.setVisibility(View.GONE);
                            txtSellerDiaChi.setVisibility(View.GONE);
                            txtSellerSDT.setVisibility(View.GONE);
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

    CompoundButton.OnCheckedChangeListener layHangThanhCongCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbLayHangThanhCong.setEnabled(false);
                                cbDangGiaoHang.setEnabled(true);

                                txtSellerLabel.setVisibility(View.GONE);
                                txtSellerDiaChi.setVisibility(View.GONE);
                                txtSellerSDT.setVisibility(View.GONE);

                                txtHoTenNguoiMua.setVisibility(View.VISIBLE);
                                txtDiaChi.setVisibility(View.VISIBLE);
                                txtLienHe.setVisibility(View.VISIBLE);

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 5, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
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

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                cbLayHangThanhCong.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
                alert.setMessage("L???y h??ng ng?????i b??n th??nh c??ng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener giaoHangCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbDangGiaoHang.setEnabled(false);
                                btnGiaoThanhCong.setVisibility(View.VISIBLE);
                                btnGiaoThatBai.setVisibility(View.VISIBLE);
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 6, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
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

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                cbDangGiaoHang.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
                alert.setMessage("Ti???n h??nh giao ????n h??ng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener giaoThanhCongClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                                        long orderMoney = snapshot.getValue(DatHang.class).getGiaTien();
                                        int soLuongSP = snapshot.getValue(DatHang.class).getSanPham().getSoLuong();
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                    int newSL = snapshot.getValue(UserData.class).getSoSPDaBan() + soLuongSP;
                                                    databaseReference.child("User").child(nguoiBanID).child("iSoSPDaBan").setValue(newSL);
                                                }
                                                else if(snapshot.getValue(UserData.class).getUserID().equals("-MMR0F6xxKcg9TXwvTfX")){
                                                    long newMoney = snapshot.getValue(UserData.class).getMoney() + (orderMoney * commission / 100);
                                                    databaseReference.child("User").child("-MMR0F6xxKcg9TXwvTfX").child("lMoney").setValue(newMoney);
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

                                        DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(),
                                                snapshot.getValue(DatHang.class).getLoaiDonHang(), 7, commission,
                                                snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());

                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);



                                        finish();
                                        intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                    else if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                                        int soLuongSP = snapshot.getValue(DatHang.class).getSanPham().getSoLuong();
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                    int newSL = snapshot.getValue(UserData.class).getSoSPDaBan() + soLuongSP;
                                                    databaseReference.child("User").child(nguoiBanID).child("iSoSPDaBan").setValue(newSL);
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

                                        DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(),
                                                snapshot.getValue(DatHang.class).getLoaiDonHang(), 8, commission,
                                                snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("CommentNeeds").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).removeValue();
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getMoney() - (tongGiaTri * commission / 100);
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(), snapshot.getValue(UserData.class).getShopID(),
                                                            snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
                                                            snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(),
                                                            snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia(), snapshot.getValue(UserData.class).getSoCMND()
                                                            ,snapshot.getValue(UserData.class).getEmail(), snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    truTienCheck++;
                                                }
                                                else if(snapshot.getValue(UserData.class).getUserID().equals("-MMR0F6xxKcg9TXwvTfX")){
                                                    long newMoney = snapshot.getValue(UserData.class).getMoney() + (tongGiaTri * commission / 100);
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(), snapshot.getValue(UserData.class).getShopID(),
                                                            snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
                                                            snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(),
                                                            snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia(), snapshot.getValue(UserData.class).getSoCMND()
                                                            ,snapshot.getValue(UserData.class).getEmail(), snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    hoanTienCheck++;
                                                }
                                                if(truTienCheck != 0 && hoanTienCheck != 0){
                                                    finish();
                                                    intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                                    intent.putExtra("UserName", userName);
                                                    intent.putExtra("UserID", userID);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);
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
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
            alert.setMessage("X??c nh???n giao h??ng th??nh c??ng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };

    View.OnClickListener giaoThatBaiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                                        DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(),
                                                snapshot.getValue(DatHang.class).getLoaiDonHang(), -1, commission,
                                                snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        finish();
                                        intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                    else if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getLoaiDonHang() == 3){
                                        DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(),
                                                snapshot.getValue(DatHang.class).getLoaiDonHang(), -1, commission,
                                                snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("CommentNeeds").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getMoney() - tongGiaTri;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(), snapshot.getValue(UserData.class).getShopID(),
                                                            snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
                                                            snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(),
                                                            snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia(), snapshot.getValue(UserData.class).getSoCMND()
                                                            ,snapshot.getValue(UserData.class).getEmail(), snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    truTienCheck++;
                                                }
                                                else if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getMoney() + tongGiaTri;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(), snapshot.getValue(UserData.class).getShopID(),
                                                            snapshot.getValue(UserData.class).getHoTen(), snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(),
                                                            snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(),
                                                            snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia(), snapshot.getValue(UserData.class).getSoCMND()
                                                            ,snapshot.getValue(UserData.class).getEmail(), snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    hoanTienCheck++;
                                                }
                                                if(truTienCheck != 0 && hoanTienCheck != 0){
                                                    finish();
                                                    intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                                    intent.putExtra("UserName", userName);
                                                    intent.putExtra("UserID", userID);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);
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
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
            alert.setMessage("X??c nh???n h???y ????n do giao h??ng th???t b???i?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();


        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}