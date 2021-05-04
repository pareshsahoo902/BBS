package com.release.bbsbillapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemreyceler;
    private Button generateBill , addItem,createPDF;
    private EditText itemName , weigth , quantity , price,customerName,customerContact , discount;
    private LinearLayout tableLay,itemLay,customerLay;
    private ItemAdapter adapter;
    private List<ItemsModel> itemsModelsList;
    private Bitmap bmp ,scaledBmp;
    int pageWIdth = 1200;

    Date date;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        itemName=findViewById(R.id.itemName);
        weigth=findViewById(R.id.itemDesc);
        quantity=findViewById(R.id.itemQuantity);
        price=findViewById(R.id.itemTotal);
        tableLay=findViewById(R.id.table);
        itemLay=findViewById(R.id.item_lay);
        customerLay=findViewById(R.id.customerDetailLay);
        itemreyceler=findViewById(R.id.entryRecycler);
        addItem=findViewById(R.id.addItem);
        customerName=findViewById(R.id.customerName);
        customerContact=findViewById(R.id.customerContact);
        discount=findViewById(R.id.discount);
        createPDF=findViewById(R.id.createPDF);
        generateBill=findViewById(R.id.genarateBill);


        itemreyceler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        itemreyceler.hasFixedSize();

        itemsModelsList = new ArrayList<>();

        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.coverpdf);
        scaledBmp = Bitmap.createScaledBitmap(bmp,555,172,false);

        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (price.getText().toString().trim().length()>0 && itemName.getText().toString().trim().length()>0){
                    addItem.setEnabled(true);
                }else {
                    addItem.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (itemName.getText().toString().trim().length()>0 && price.getText().toString().trim().length()>0){
                    addItem.setEnabled(true);
                }else {
                    addItem.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemsModel model = new ItemsModel(itemName.getText().toString().trim(),weigth.getText().toString().trim(),quantity.getText().toString().trim(),
                        Double.parseDouble(price.getText().toString().trim()));
                tableLay.setVisibility(View.VISIBLE);
                generateBill.setEnabled(true);
                itemsModelsList.add(model);
                updaetAdapter();
            }
        });

        generateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerLay.setVisibility(View.VISIBLE);
                itemLay.setVisibility(View.INVISIBLE);
                generateBill.setVisibility(View.INVISIBLE);

            }
        });

        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customerName.getText().toString().trim().length()<1){
                    Toast.makeText(MainActivity.this, "Enter a Customer Name!", Toast.LENGTH_SHORT).show();
                }else {
                    generatePDF();
                }
            }
        });

        adapter = new ItemAdapter(itemsModelsList,getApplicationContext());
        itemreyceler.setAdapter(adapter);



    }

    private void generatePDF() {
        date = new Date();

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        PdfDocument mypdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint invoiceText = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200,2010,1).create();
        PdfDocument.Page page1 = mypdfDocument.startPage(pageInfo);
        Canvas canvas =page1.getCanvas();

        canvas.drawBitmap(scaledBmp,0,0,paint);

        invoiceText.setTextAlign(Paint.Align.CENTER);
        invoiceText.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        invoiceText.setTextSize(50);
        canvas.drawText("Invoice",pageWIdth/2 ,230,invoiceText );

        paint.setColor(Color.parseColor("#161616"));
        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("Customer Name : "+customerName.getText().toString(),10,300,paint);
        canvas.drawText("Contact       : +91"+customerContact.getText().toString(),10,350,paint);

        paint.setTextAlign(Paint.Align.RIGHT);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        canvas.drawText("Date :  "+dateFormat.format(date),pageWIdth-10,300,paint);

        dateFormat = new SimpleDateFormat("HH:mm a");
        canvas.drawText("TIme :  "+dateFormat.format(date),pageWIdth-10,350,paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRect(10,430,pageWIdth-10,500,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);


        canvas.drawText("sl.no",20,470,paint);
        canvas.drawText("Item name",150,470,paint);
        canvas.drawText("Weight",540,470,paint);
        canvas.drawText("Quantity",720,470,paint);
        canvas.drawText("Price",950,470,paint);

        double total = 0.0;
        int height =550;

        if (itemsModelsList.size()!=0){

            for (int i=0;i<itemsModelsList.size();i++){
                canvas.drawText(String.valueOf(i+1)+".",20,height,paint);
                canvas.drawText(itemsModelsList.get(i).getItemName(),150,height,paint);
                canvas.drawText(itemsModelsList.get(i).getWeight()+"kg",545,height,paint);
                canvas.drawText(itemsModelsList.get(i).getQuantity(),725,height,paint);
                canvas.drawText("₹"+String.valueOf(itemsModelsList.get(i).getPrice()),950,height,paint);
                height+=50;
                total+=itemsModelsList.get(i).getPrice();
            }
        }

        canvas.drawLine(pageWIdth-500,height+80,pageWIdth-10,height+80,paint);
        canvas.drawText("Total : ",pageWIdth-470,height+130,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("₹"+String.valueOf(total),pageWIdth-20,height+130,paint);

        double discAmount =0.0;

        if (discount.getText().toString().length()>0){
            discAmount = Double.parseDouble(discount.getText().toString());
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Discount : ",pageWIdth-470,height+170,paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("(-) ₹"+String.valueOf(discount.getText().toString()),pageWIdth-20,height+170,paint);
        }

        paint.setColor(Color.parseColor("#008ECC"));


        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(50f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Total : ",pageWIdth-480,height+250,paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("₹"+String.valueOf(total-discAmount),pageWIdth-20,height+250,paint);


        mypdfDocument.finishPage(page1);
        File pdffile = new File(Environment.getExternalStorageDirectory(),"/"+customerName.getText().toString()+"Invoice.pdf");

        try {
            mypdfDocument.writeTo(new FileOutputStream(pdffile));
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("paresh",e.toString());
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

        mypdfDocument.close();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Toast.makeText(this, "Generating Pdf", Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+customerName.getText().toString()+"Invoice.pdf");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }

    private void updaetAdapter() {

        adapter.updateList(itemsModelsList);
        itemName.setText("");
        weigth.setText("");
        quantity.setText("");
        price.setText("");

    }
}