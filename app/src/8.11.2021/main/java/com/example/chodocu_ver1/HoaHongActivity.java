package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.HoaHong;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HoaHongActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private int commissionShop, commissionUser, commissionShipper;
    private Intent intent;
    private Button btnBack, btnChangeShop, btnChangeUser, btnChangeShipper;
    private EditText edtCommissionShop, edtCommissionUser, edtCommissionShipper;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.hoahong_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnChangeShop = (Button) findViewById(R.id.btnChangeShop);
        btnChangeUser = (Button) findViewById(R.id.btnChangeUser);
        edtCommissionShop = (EditText) findViewById(R.id.edtCommissionShop);
        edtCommissionUser = (EditText) findViewById(R.id.edtCommissionUser);
        btnChangeShipper = (Button) findViewById(R.id.btnChangeShipper);
        edtCommissionShipper = (EditText) findViewById(R.id.edtCommissionShipper);


        btnBack.setOnClickListener(backClick);
        btnChangeShop.setOnClickListener(changeShopClick);
        btnChangeUser.setOnClickListener(changeUserClick);
        btnChangeShipper.setOnClickListener(changeShipperClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");

        }

        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(HoaHong.class).getId().equals("-MKyZZdaQ3ucidlxPkUV"))
                {
                    edtCommissionShop.setText(String.valueOf(snapshot.getValue(HoaHong.class).getShopCommission()));
                    edtCommissionUser.setText(String.valueOf(snapshot.getValue(HoaHong.class).getUserCommission()));
                    edtCommissionShipper.setText(String.valueOf(snapshot.getValue(HoaHong.class).getShipperCommission()));
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

    View.OnClickListener changeShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionShipper.getText().toString().isEmpty()){
                edtCommissionShipper.setError("Kh??ng ???????c ????? tr???ng!");
            }
            else if (Integer.valueOf(edtCommissionShipper.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Kh??ng ???????c nh???p nh??? h??n 0 ho???c b???ng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
            else {
                commissionShipper = Integer.valueOf(edtCommissionShipper.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("shipperCommission").setValue(commissionShipper);
            }
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getPermission() == 3) {

                        databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionShipper);
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("S???a ?????i th??nh c??ng").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
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
    };

    View.OnClickListener changeShopClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionShop.getText().toString().isEmpty()){
                edtCommissionShop.setError("Kh??ng ???????c ????? tr???ng!");
            }
            else if (Integer.valueOf(edtCommissionShop.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Kh??ng ???????c nh???p nh??? h??n 0 ho???c b???ng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) <= Integer.valueOf(edtCommissionShop.getText().toString())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Hoa H???ng Shop ph???i nh??? h??n hoa h???ng User").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else {
                commissionShop = Integer.valueOf(edtCommissionShop.getText().toString());
                commissionUser = Integer.valueOf(edtCommissionUser.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("shopCommission").setValue(commissionShop);
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.getValue(UserData.class).getShopID().isEmpty()) {
                            databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionUser);
                        }
                        else {
                            String shopID = snapshot.getValue(UserData.class).getShopID();
                            String userID = snapshot.getValue(UserData.class).getUserID();
//                            databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionShop);
                            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(CuaHang.class).getShopID().equals(shopID) && snapshot.getValue(CuaHang.class).getTinhTrangShop() != -1) {
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                databaseReference.child("User").child(userID).child("iCommission").setValue(commissionShop);
                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                alert.setMessage("S???a ?????i th??nh c??ng").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                }).show();
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
                                    else {
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                databaseReference.child("User").child(userID).child("iCommission").setValue(commissionUser);
                                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                                alert.setMessage("S???a ?????i th??nh c??ng").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                }).show();
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
    };

    View.OnClickListener changeUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionUser.getText().toString().isEmpty()){
                edtCommissionUser.setError("Kh??ng ???????c ????? tr???ng!");
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Kh??ng ???????c nh???p nh??? h??n 0 ho???c b???ng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) < Integer.valueOf(edtCommissionShop.getText().toString())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Hoa H???ng User ph???i l???n h??n hoa h???ng Shop").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else {
                commissionUser = Integer.valueOf(edtCommissionUser.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("userCommission").setValue(commissionUser);
            }
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getShopID().isEmpty()) {
                        databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionUser);
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("S???a ?????i th??nh c??ng").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
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
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}