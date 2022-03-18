package com.example.module_quanlynhanvien;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "employee.db";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    int id =-1;

    Button button_Chon, button_Chup, button_Luu, button_Huy;
    EditText editText_ten, editText_sdt;
    ImageView imageView_HinhDD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        addControl();
        addEvents();
        initUI();


    }

    private void addEvents(){
        button_Chon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        button_Chup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        button_Luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        button_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
// bài 6
    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }
// Bài 7
    private void update(){
        String ten = editText_ten.getText().toString();
        String sdt = editText_sdt.getText().toString();
        byte[]  anh = getByteArrayFromImageView(imageView_HinhDD);

        ContentValues contentValues = new ContentValues();
//        PUT dữ liệu
        contentValues.put("Ten", ten);
        contentValues.put("SDT",sdt);
        contentValues.put("Hinh",anh);

        SQLiteDatabase database = Database.initDatabase(this, "employee.db");
        database.update("NhanVien", contentValues, "ID = ?",new String[]{id +""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, BoutputStream);
        byte[] byteArray = BoutputStream.toByteArray();
        return byteArray;
    }

//    bài 6
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageURI = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageURI);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView_HinhDD.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView_HinhDD.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initUI(){
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE ID = ?",new String[]{id +""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        byte[] anh = cursor.getBlob(3);
//        decode byte => bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(anh,0, anh.length);
        imageView_HinhDD.setImageBitmap(bitmap);
        editText_ten.setText(ten);
        editText_sdt.setText(sdt);

    }


    private void addControl(){
        button_Chon = findViewById(R.id.button_chonhinh);
        button_Chup = findViewById(R.id.button_chuphinh);
        button_Luu = findViewById(R.id.button_luu_up);
        button_Huy = findViewById(R.id.button_Huy_up);

        editText_ten = findViewById(R.id.editText_PersonName);
        editText_sdt = findViewById(R.id.editText_phoneNumber);
        imageView_HinhDD = findViewById(R.id.imageView_HDD_sua);
    }
}