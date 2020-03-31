package com.letuan.mobileworld.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.adapter.OrderAdapter;
import com.letuan.mobileworld.dialog.NoticeDialog;
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.ultil.CheckWifiValid;

import java.text.DecimalFormat;

public class OrderActivity extends AppCompatActivity {

    ListView listViewOrder;
    TextView txtMessage;
    static TextView txtSumMoney;
    Button btnThanhToan, btnTiepTucMuaHang;
    Toolbar toolbarOrder;
    OrderAdapter orderAdapter;
    public static Long tongtien = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        anhxa();
        actionToolbar();
        checkData();
        eventUltil();
        catchOnItemListViewClicked();
        eventButton();
    }

    private void eventButton() {
        btnTiepTucMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.orderList.size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    CheckWifiValid.ShowToast_Short(getApplicationContext(), "Giỏ hàng trống");
                }
            }
        });
    }

    private void catchOnItemListViewClicked() {
        listViewOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final NoticeDialog noticeDialog = new NoticeDialog(OrderActivity.this);
                noticeDialog.setCancelable(true);
                noticeDialog.setNotification("Xóa sản phẩm khỏi giỏ hàng ?", "Đồng ý", "Hủy", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.btn_ok) {
                            if (MainActivity.orderList.size() <= 0) {
                                txtMessage.setVisibility(View.VISIBLE);
                            } else {
                                MainActivity.orderList.remove(position);
                                orderAdapter.notifyDataSetChanged();
                                eventUltil();
                                if (MainActivity.orderList.size() <= 0) {
                                    txtMessage.setVisibility(View.VISIBLE);
                                } else {
                                    txtMessage.setVisibility(View.INVISIBLE);
                                    orderAdapter.notifyDataSetChanged();
                                    eventUltil();
                                }
                            }
                        }
                        noticeDialog.dismiss();
                    }
                });
                noticeDialog.show();

                return true;
            }
        });
    }

    // bat su kien do len list view
    public static void eventUltil() {
        tongtien = 0L;
        for (Order order : MainActivity.orderList) {
            tongtien += order.getPriceProduct();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtSumMoney.setText(decimalFormat.format(tongtien) + " Đ");
    }

    private void checkData() {
        //ko co data hien thong bao chua co san pham trong gio hang
        if (MainActivity.orderList.size() > 0) {
            orderAdapter.notifyDataSetChanged();
            txtMessage.setVisibility(View.INVISIBLE);
            listViewOrder.setVisibility(View.VISIBLE);
        } else {
            orderAdapter.notifyDataSetChanged();
            txtMessage.setVisibility(View.VISIBLE);
            listViewOrder.setVisibility(View.INVISIBLE);
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarOrder.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        listViewOrder = findViewById(R.id.listvieworder);
        txtMessage = findViewById(R.id.txtmessageorder);
        txtSumMoney = findViewById(R.id.txtsummoney);
        btnThanhToan = findViewById(R.id.btnthanhtoanorder);
        btnTiepTucMuaHang = findViewById(R.id.btntieptucmuahangorder);
        toolbarOrder = findViewById(R.id.toolbarorder);
        orderAdapter = new OrderAdapter(OrderActivity.this, MainActivity.orderList);
        listViewOrder.setAdapter(orderAdapter);
    }
}
