package com.example.module_quanlynhanvien;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "employee.db";
    SQLiteDatabase database;

    ListView listView_NV;
    ArrayList<NhanVien> list_NV;
    AdapterNhanVien adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        readData();
    }

    private void readData(){
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien", null);
//        cursor.moveToFirst();
//        Toast.makeText(this, cursor.getString(1), Toast.LENGTH_SHORT).show();
//        truơcs khi đọc dữ liệu phải clear đi => ko bị trùng dữ liệu
        list_NV.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
//            truyền vào số cột
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            list_NV.add(new NhanVien(id,ten,sdt,anh));
        }
//  set_adaptert
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_person:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addControl(){
        listView_NV = findViewById(R.id.listView_NV);
        list_NV = new ArrayList<>();
        adapter = new AdapterNhanVien(this,list_NV);
        listView_NV.setAdapter(adapter);
    }
}