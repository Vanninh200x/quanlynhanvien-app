package com.example.module_quanlynhanvien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterNhanVien extends BaseAdapter{
    Activity context;
    List<NhanVien> nhanVienList;

    public AdapterNhanVien(Activity context, List<NhanVien> nhanVienList) {
        this.context = context;
        this.nhanVienList = nhanVienList;
    }

    @Override
    public int getCount() {
        return nhanVienList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        ImageView imageView_Hinh;
        TextView textView_Id, textView_Ten, textView_SDT;
        Button button_Xoa, button_Sua;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
//        Bài 3
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = inflater.inflate(R.layout.listview_row, null);
//        ImageView image_hinh = row.findViewById(R.id.imageView_Hinhdaidien);
//        TextView textView_Id = row.findViewById(R.id.textView_Id);
//        TextView textView_Ten = row.findViewById(R.id.textView_Ten);
//        TextView textView_SDT = row.findViewById(R.id.textView_SDT);
//        Button button_Xoa = row.findViewById(R.id.button_Xoa);
//        Button button_Sua = row.findViewById(R.id.button_Sua);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_row, null);
            holder.imageView_Hinh = view.findViewById(R.id.imageView_Hinhdaidien);
            holder.textView_Id = view.findViewById(R.id.textView_Id);
            holder.textView_Ten = view.findViewById(R.id.textView_Ten);
            holder.textView_SDT = view.findViewById(R.id.textView_SDT);
            holder.button_Sua = view.findViewById(R.id.button_Sua);
            holder.button_Xoa = view.findViewById(R.id.button_Xoa);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }


        NhanVien nhanVien = nhanVienList.get(i);
        holder.textView_Id.setText(nhanVien.getId() + "");
        holder.textView_Ten.setText(nhanVien.getTen());
        holder.textView_SDT.setText(nhanVien.getSdt());

        Bitmap bitmap_Hinh = BitmapFactory.decodeByteArray(nhanVien.getAnh(), 0, nhanVien.getAnh().length);
        holder.imageView_Hinh.setImageBitmap(bitmap_Hinh);


//      Bài 4  Tạo AC sửa xóa ( sau )
        holder.button_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID", nhanVien.getId());
                context.startActivity(intent);
            }
        });

// Bài 9: xóa
        holder.button_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn không!");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(nhanVien.getId());
                    }


                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

    private void delete(int idNhanVien) {
        SQLiteDatabase database = Database.initDatabase(context, "employee.db");
        database.delete("NhanVien", "ID = ? ", new String[]{idNhanVien + ""});
        nhanVienList.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien ", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] anh = cursor.getBlob(3);

            nhanVienList.add(new NhanVien(id,ten,sdt,anh));
        }
        notifyDataSetChanged();
    }
}
