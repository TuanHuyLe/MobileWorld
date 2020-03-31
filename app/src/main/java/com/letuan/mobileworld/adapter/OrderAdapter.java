package com.letuan.mobileworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letuan.mobileworld.R;
import com.letuan.mobileworld.activity.OrderActivity;
import com.letuan.mobileworld.model.Order;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends BaseAdapter {
    Context context;
    List<Order> orderList;
    private ViewHolder viewHolder = null;
    private int size = 0;
    private Long sum = 0l;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView txtNameOrder, txtPriceOrder;
        public ImageView imgOrder;
        public Button btnMinus, btnPlus, btnValue;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.line_order, null);
            viewHolder.txtNameOrder = view.findViewById(R.id.txtnameorder);
            viewHolder.txtPriceOrder = view.findViewById(R.id.txtpriceorder);
            viewHolder.imgOrder = view.findViewById(R.id.imgorder);
            viewHolder.btnMinus = view.findViewById(R.id.btnminus);
            viewHolder.btnPlus = view.findViewById(R.id.btnplus);
            viewHolder.btnValue = view.findViewById(R.id.btnvalue);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Order order = (Order) getItem(position);
        viewHolder.txtNameOrder.setText(order.getNameProduct());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtPriceOrder.setText(decimalFormat.format(order.getPriceProduct()) + " Đ");
        Picasso.with(context).load(order.getImageProduct()).placeholder(R.drawable.no_image_icon)
                .error(R.drawable.error).into(viewHolder.imgOrder);
        viewHolder.btnValue.setText(order.getSize() + "");

        if (order.getSize() >= 10) {
            viewHolder.btnPlus.setEnabled(false);
        }
        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = (Order) getItem(position);
                size = order.getSize();
                order.setSize(size + 1);
                viewHolder.btnMinus.setEnabled(true);
                sum = (order.getPriceProduct() / size) * order.getSize();
                order.setPriceProduct(sum);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtPriceOrder.setText(decimalFormat.format(sum) + " Đ");
                viewHolder.btnValue.setText(order.getSize() + "");
                OrderActivity.eventUltil();
                if (size > 8) {
                    viewHolder.btnPlus.setEnabled(false);
                }
            }
        });

        if (order.getSize() <= 1) {
            viewHolder.btnMinus.setEnabled(false);
        }
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = (Order) getItem(position);
                size = order.getSize();
                order.setSize(size - 1);
                viewHolder.btnPlus.setEnabled(true);
                sum = (order.getPriceProduct() / size) * order.getSize();
                order.setPriceProduct(sum);
                DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                viewHolder.txtPriceOrder.setText(decimalFormat.format(sum) + " Đ");
                viewHolder.btnValue.setText(order.getSize() + "");
                OrderActivity.eventUltil();
                if (size < 3) {
                    viewHolder.btnMinus.setEnabled(false);
                }
            }
        });

        return view;
    }
}
