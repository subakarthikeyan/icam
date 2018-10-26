package com.google.android.gms.samples.vision.ocrreader;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class HistoryActivity
        extends Activity
{
    ArrayList<String> appList;
    private ArrayAdapter<String> arrayAdapter;
    Button clear;
    ArrayList<String> com;
    ListView listView;
    MyDB myDB;
    ArrayList<String> textList;
    TextView textView;

    public void clearAll(View paramView)
    {
        this.myDB.getWritableDatabase().execSQL("delete from history");
        this.textList = new ArrayList();
        this.appList = new ArrayList();
        this.com = new ArrayList();
        this.arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.com);
        this.listView.setAdapter(this.arrayAdapter);
        this.textView.setText("Nothing Found...");
    }

    protected void onCreate(final Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_history);
        this.listView = ((ListView)findViewById(R.id.LIST));
        this.textView = ((TextView)findViewById(R.id.TEXT));
        this.clear = ((Button)findViewById(R.id.CLEAR));
        this.textList = new ArrayList();
        this.appList = new ArrayList();
        this.com = new ArrayList();
        this.myDB = new MyDB(this);
       SQLiteDatabase db = this.myDB.getReadableDatabase();
        final SQLiteDatabase db1 = this.myDB.getWritableDatabase();
        Cursor c = ((SQLiteDatabase)db).rawQuery("select * from history", null);
        while (c.moveToNext())
        {
            this.com.add("Target Text: " + c.getString(c.getColumnIndex("stext")) + "\n\nSource App: " + c.getString(c.getColumnIndex("app")));
            this.textList.add(c.getString(c.getColumnIndex("stext")));
            this.appList.add(c.getString(c.getColumnIndex("app")));
        }
        if (this.com.size() > 0) {
            this.clear.setEnabled(true);
            this.arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.com);
            this.listView.setAdapter(this.arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (appList.get(position).equalsIgnoreCase("chrome"))
                    {
                        Intent i = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com/search?q=" + (String)HistoryActivity.this.textList.get(position)));
                        HistoryActivity.this.startActivity(i);
                        return;
                    }
                    if (appList.get(position).equalsIgnoreCase("gmap"))
                    {
                        Intent i = new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.com/maps?q=" + (String)HistoryActivity.this.textList.get(position)));
                        HistoryActivity.this.startActivity(i);
                        return;
                    }
                    Intent i = new Intent("android.intent.action.SEND");
                    i.setType("text/plain");
                    i.putExtra("android.intent.extra.TEXT", (String)HistoryActivity.this.textList.get(position));
                    HistoryActivity.this.startActivity(i);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    db1.execSQL("delete from history where stext='" + (String)HistoryActivity.this.textList.get(position) + "' and app='" + (String)HistoryActivity.this.appList.get(position) + "'");
                    HistoryActivity.this.textList.remove(position);
                    HistoryActivity.this.appList.remove(position);
                    HistoryActivity.this.com.remove(position);
                    arrayAdapter= new ArrayAdapter(HistoryActivity.this, android.R.layout.simple_list_item_1, HistoryActivity.this.com);
                    HistoryActivity.this.listView.setAdapter(HistoryActivity.this.arrayAdapter);
                    if (HistoryActivity.this.com.size() > 0) {
                        HistoryActivity.this.clear.setEnabled(true);
                    }else {

                        Toast.makeText(HistoryActivity.this, "removed", Toast.LENGTH_SHORT).show();

                        textView.setText("Nothing Found...");
                        clear.setEnabled(false);
                    }
                    return true;
                }
            });

        }
      else
        {


            this.textView.setText("Nothing Found...");
            this.clear.setEnabled(false);
        }
    }
}
