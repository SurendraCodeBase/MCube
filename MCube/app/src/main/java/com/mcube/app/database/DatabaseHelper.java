package com.mcube.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.mcube.app.R;
import com.mcube.app.util.McubeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class DatabaseHelper extends SQLiteOpenHelper{

    String TAG = "DatabaseHelper";

    final static int DB_VERSION = 1;
    final static String DATABASE_NAME = "mcube.db";

    Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String s;
        try {
            McubeUtils.debugLog("Running DB table creation");
            InputStream in = context.getResources().openRawResource(R.raw.sql_queries);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i=0; i<statements.getLength(); i++) {
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                db.execSQL(s);
            }
        }
        catch (Throwable t) {
            McubeUtils.errorLog(t.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion!=oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        }
    }
}
