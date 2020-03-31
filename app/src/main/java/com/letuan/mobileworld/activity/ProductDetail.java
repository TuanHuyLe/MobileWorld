package com.letuan.mobileworld.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.model.Order;
import com.letuan.mobileworld.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ProductDetail extends AppCompatActivity {
    Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtNameDetail, txtPriceDetail, txtDescription;
    Spinner spinner;
    Button btnOrder;

    int id = 0;
    String name = "";
    Integer price = 0;
    String image = "";
    String description = "";
    int categoryid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        anhxa();
        actionToolbar();
        getInfomation();
        catchEventSpinner();
        catchEventButtonAddOrder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucartorder:
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void catchEventButtonAddOrder() {
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
                //kiem tra trong gio hang co san pham chua
                //-true- cong them san pham vao san pham da co trong gio hang
                //-false- them san pham moi vao trong gio hang
                boolean exists = false; // flag check ton tai san pham trong gio hang khong
                if (MainActivity.orderList.size() > 0) {
                    for (Order order : MainActivity.orderList) {
                        if (order.getId() == id) {
                            //tinh lai so luong cua san pham
                            order.setSize(order.getSize() + soluong);
                            //set max gia tri cho gio hang la 10
                            if (order.getSize() >= 10) {
                                order.setSize(10);
                            }
                            //tinh lai tong gia cho san pham
                            order.setPriceProduct(Long.valueOf(order.getSize() * price));
                            exists = true;
                        }
                    }
                    if (!exists) {
                        long newPrice = soluong * price;
                        MainActivity.orderList.add(new Order(id, name, newPrice, image, soluong));
                    }
                } else {
                    long newPrice = soluong * price;
                    MainActivity.orderList.add(new Order(id, name, newPrice, image, soluong));
                }
                Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void catchEventSpinner() {
        Integer[] size = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter =
                new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, size);
        spinner.setAdapter(arrayAdapter);
    }

    private void getInfomation() {
        Product product = (Product) getIntent().getSerializableExtra("thongtin");
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
        image = product.getImage();
        description = product.getDescription();
        categoryid = product.getCategoryid();
        txtNameDetail.setText(name);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtPriceDetail.setText("Giá: " + decimalFormat.format(price) + " Đ");
        txtDescription.setText(description);
        Picasso.with(getApplicationContext()).load(image).placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error).into(imgDetail);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbarDetail = findViewById(R.id.toolbardetail);
        imgDetail = findViewById(R.id.imgdetail);
        txtNameDetail = findViewById(R.id.txtnamedetail);
        txtPriceDetail = findViewById(R.id.txtpricedetail);
        txtDescription = findViewById(R.id.txtdescriptiondetail);
        spinner = findViewById(R.id.spinner);
        btnOrder = findViewById(R.id.btnorder);
    }
}
