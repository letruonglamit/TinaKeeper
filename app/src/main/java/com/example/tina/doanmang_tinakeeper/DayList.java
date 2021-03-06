package com.example.tina.doanmang_tinakeeper;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.tina.doanmang_tinakeeper.adapter.MyDatabaseHelper;
import com.example.tina.doanmang_tinakeeper.adapter.RecyclerDataAdapter;
import com.example.tina.doanmang_tinakeeper.model.Expense;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by TiNa on 06/03/2017.
 */

public class DayList extends Fragment {
    private static final String TAG = "DAYLIST";
    private static final String STATE = "Trang thai";
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private Button btnGetDate;
    private View view;
    private RecyclerView recyclerview;
    private RecyclerDataAdapter adapter;
    private String[] names;
    private TypedArray profile_pics;
    private String[] emails;
    private int[] costs;
    private Calendar cal;
    private java.util.Date date;
    private final List<Expense> expenseList = new ArrayList<Expense>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_list, container, false);

        btnGetDate= (Button) view.findViewById(R.id.button_getdate);

//        cau hinh cho button lay ngay thang nam
        //Set ngày giờ hiện tại khi mới chạy lần đầu
        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;
        //Định dạng kiểu ngày / tháng /năm
        dft=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate=dft.format(cal.getTime());
//        hiển thị lên giao diện
        btnGetDate.setText(strDate);
        btnGetDate.setOnClickListener(showDatePicker);

        //cấu hình database
        try{
            MyDatabaseHelper db = new MyDatabaseHelper(getContext());
            db.createDefaultExpenseIfNeed();//tạo 2 bản ghi mặc định
            List<Expense> list=  db.getAllExpense();
            this.expenseList.addAll(list);
        } catch (Exception e){
            e.printStackTrace();
        }

        //Xử lý nút (+)
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditExpense.class);
                startActivityForResult(intent,MY_REQUEST_CODE);
            }
        });

//        private int id;
//        private String category;
//        private String notes;
//        private double money;
//        private Date date;
//        private int photoID;
//
//        expenseList.add(new Expense(1,"Food", "Eating out",20000, Date.valueOf("2017-03-11")));
//        expenseList.add(new Expense(1,"Food", "Eating out",20000, Date.valueOf("2017-03-11")));
//        expenseList.add(new Expense(1,"Food", "Eating out",20000, Date.valueOf("2017-03-11")));
        //cấu hình cho recyclerview
        recyclerview = (RecyclerView) view.findViewById(R.id.rv_list_day);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        adapter = new RecyclerDataAdapter(getContext(), expenseList);
        recyclerview.setAdapter(adapter);
        //Log.e(STATE,String.valueOf(adapter.getItemCount()));
        // Đăng ký Context menu cho recyclerview.
        registerForContextMenu(this.recyclerview);
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "View Expense");
        menu.add(0, MENU_ITEM_CREATE , 1, "Create Expense");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Expense");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Expense");
    }


    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
                //Sự kiện khi click vào nút Done trên Dialog
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    // Set text cho textView
                    btnGetDate.setText(day + "/" + (month + 1) + "/" + year);
                    //Lưu vết lại ngày mới cập nhật
                    cal.set(year, month, day);
                    date = cal.getTime();
                }
            };
            String s = btnGetDate.getText() + "";
            //Lấy ra chuỗi của textView Date
            String strArrtmp[] = s.split("/");
            int ngay = Integer.parseInt(strArrtmp[0]);
            int thang = Integer.parseInt(strArrtmp[1]) - 1;
            int nam = Integer.parseInt(strArrtmp[2]);
            //Hiển thị ra Dialog
            DatePickerDialog pic = new DatePickerDialog(getContext(), callback, nam, thang, ngay);
            pic.setCanceledOnTouchOutside(true);
            pic.closeOptionsMenu();
            pic.show();
        }
    };

    // Người dùng đồng ý xóa một Expense.
    private void deleteExpense(Expense expense)  {
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        db.deleteExpense(expense);
        this.expenseList.remove(expense);
        // Refresh ListView.
        this.adapter.notifyDataSetChanged();
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item){
//        AdapterView.AdapterContextMenuInfo
//                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        final Expense selectedExpense = (Expense) this.adapter.getItem(info.position);
//
//        if(item.getItemId() == MENU_ITEM_VIEW){
//            //Toast.makeText(getContext(),selectedExpense.(), Toast.LENGTH_LONG).show();
//        }
//        else if(item.getItemId() == MENU_ITEM_CREATE){
//            Intent intent = new Intent(getContext(), AddEditExpense.class);
//
//            // Start AddEditExpenseActivity, có phản hồi.
//            this.startActivityForResult(intent, MY_REQUEST_CODE);
//        }
//        else if(item.getItemId() == MENU_ITEM_EDIT ){
//            Intent intent = new Intent(getContext(), AddEditExpense.class);
//            intent.putExtra("expense", selectedExpense.toString());
//
//            // Start AddEditExpenseActivity, có phản hồi.
//            this.startActivityForResult(intent,MY_REQUEST_CODE);
//        }
//        else if(item.getItemId() == MENU_ITEM_DELETE){
//            // Hỏi trước khi xóa.
//            new AlertDialog.Builder(getContext())
//                    .setMessage(selectedExpense.getMoney()+". Are you sure you want to delete?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            deleteExpense(selectedExpense);
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//        }
//        else {
//            return false;
//        }
//        return true;
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.expenseList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
                List<Expense> list=  db.getAllExpense();
                this.expenseList.addAll(list);
                // Thông báo dữ liệu thay đổi (Để refresh ListView).
                this.adapter.notifyDataSetChanged();
            }
        }
    }
}
