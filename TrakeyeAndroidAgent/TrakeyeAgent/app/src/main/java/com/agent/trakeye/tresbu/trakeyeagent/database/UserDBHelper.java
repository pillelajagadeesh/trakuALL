package com.agent.trakeye.tresbu.trakeyeagent.database;

/**
 * Created by sharmaan on 12-05-2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.agent.trakeye.tresbu.trakeyeagent.model.AssetCoordinates;
import com.agent.trakeye.tresbu.trakeyeagent.model.FieldPersonResponse;
import com.agent.trakeye.tresbu.trakeyeagent.model.PinChamber;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ray
 */
public class UserDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TrakeyeAgent.db";
    public static final String CUSTOMER_TABLE_NAME = "customer";
    public static final String ASSET_LOCATION_TABLE_NAME = "assetpath";
    public static final String ISSUE_TABLE_NAME = "issue";
    public static final String PIN_CHAMBER_TABLE_NAME = "pin_chamber";
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_NAME = "name";
    public static final String USER_ID = "id";
    public static final String USER_LOCATION = "address";
    public static final String USER_lATITUDE = "latitude";
    public static final String USER_LONGITUDE = "longitude";
    public static final String USER_LOGSOURCE = "logSource";
    public static final String logTimeAndZone = "logTimeAndZone";
    public static final String CREATETIME = "createTime";
    public static final String USER_NOTE = "note";
    public static final String USER_DESCRIPTION = "description";
    public static final String USER_TYPE = "type";
    public static final String USER_TIME = "time";
    public static final String USER_DATE = "date";
    public static final String USER_BATTERYLEVEL = "batteryLevel";

    public static final int DATABASE_VERSION = 1;
    private HashMap hp;

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub


        // Table to enter Customer infor
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_TABLE_NAME + "( _id INTEGER PRIMARY KEY," + USER_LOCATION + " TEXT," + USER_lATITUDE + " TEXT, "
                + USER_LONGITUDE + " TEXT," + logTimeAndZone + " TEXT," + CREATETIME + " TEXT," + USER_LOGSOURCE + " TEXT," + USER_BATTERYLEVEL + " TEXT" + ");";


        String CREATE_ISSUE_TABLE = "CREATE TABLE " + ISSUE_TABLE_NAME + "(" + USER_NAME + " TEXT," + USER_DESCRIPTION + " TEXT," + USER_NOTE + " TEXT,"
                + USER_LOCATION + " TEXT," + USER_lATITUDE + " TEXT, "
                + USER_LONGITUDE + " TEXT," + USER_TIME + " TEXT," + USER_DATE + " TEXT" + ");";

        String CREATE_PIN_CHAMBER_TABLE = "CREATE TABLE " + PIN_CHAMBER_TABLE_NAME + "(" + USER_NAME + " TEXT," + USER_TYPE + " TEXT,"
                + USER_LOCATION + " TEXT," + USER_lATITUDE + " TEXT, "
                + USER_LONGITUDE + " TEXT," + USER_TIME + " TEXT," + USER_DATE + " TEXT" + ");";

        // Table for Spread Asset Type
        String CREATE_ASSET_PATH_TABLE = "CREATE TABLE " + ASSET_LOCATION_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+ USER_lATITUDE + " TEXT, " + USER_LONGITUDE + " TEXT );";

        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_ISSUE_TABLE);
        db.execSQL(CREATE_PIN_CHAMBER_TABLE);
        db.execSQL(CREATE_ASSET_PATH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS CUSTOMER");
        onCreate(db);
    }

    public boolean insertCustomerDetails(FieldPersonResponse customer) {
//        Log.d("tag", "offline data in db is" + customer.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(logTimeAndZone, customer.getLogTimeAndZone());
        contentValues.put(USER_lATITUDE, customer.getLatitude());
        contentValues.put(USER_LOGSOURCE, customer.getLogSource());
        contentValues.put(USER_LONGITUDE, customer.getLongitude());
        contentValues.put(USER_LOCATION, customer.getAddress());
        contentValues.put(CREATETIME, customer.getCreatedDateTime());
        contentValues.put(USER_BATTERYLEVEL, customer.getBatteryPercentage());
        long rowInserted = db.insert(CUSTOMER_TABLE_NAME, null, contentValues);
        if (rowInserted != -1) {
            return true;
        } else {
            return false;
        }
    }


    public boolean insertPinChamberDetails(PinChamber customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_NAME, customer.getName());
        contentValues.put(USER_lATITUDE, customer.getLatitude());
        contentValues.put(USER_DESCRIPTION, customer.getType());
        contentValues.put(USER_LONGITUDE, customer.getLongitude());
        contentValues.put(USER_DATE, customer.getDate());
        contentValues.put(USER_LOCATION, customer.getLocation());
        contentValues.put(USER_TIME, customer.getTime());
        db.insert(ISSUE_TABLE_NAME, null, contentValues);
        return true;
    }

    public Integer deleteUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        int value = db.delete(USER_TABLE_NAME,
                USER_NAME + " = ? ",
                new String[]{(name)});

        return value;
    }


    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + USER_TABLE_NAME);
    }

    public Integer deleteCustomer(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int value = db.delete(CUSTOMER_TABLE_NAME,
                logTimeAndZone + " = ? ",
                new String[]{id});
//        Log.d("offline value:", "item deleted " + value);
        return value;
    }


    public Integer deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int value = db.delete(CUSTOMER_TABLE_NAME, null, null);
//        Log.d("offline value:", "item deleted " + value);
        return value;
    }



    public ArrayList<FieldPersonResponse> getAllCustomer() {
        ArrayList<FieldPersonResponse> array_list = new ArrayList<FieldPersonResponse>();
//        Log.d("offline value:", "fetch all value method");
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            FieldPersonResponse cust = new FieldPersonResponse();
            cust.setAddress(res.getString(res.getColumnIndex(USER_LOCATION)));
            cust.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(USER_lATITUDE))));
            cust.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(USER_LONGITUDE))));
            cust.setLogSource(res.getString(res.getColumnIndex(USER_LOGSOURCE)));
            cust.setLogTimeAndZone(Long.parseLong(res.getString(res.getColumnIndex(logTimeAndZone))));
            cust.setCreatedDateTime(Long.parseLong(res.getString(res.getColumnIndex(CREATETIME))));
            cust.setBatteryPercentage(Integer.parseInt(res.getString(res.getColumnIndex(USER_BATTERYLEVEL))));
            array_list.add(cust);
            res.moveToNext();
        }

        return array_list;
    }


    public boolean CheckIsDataAlreadyInDBorNot() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + CUSTOMER_TABLE_NAME, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public int numberOfRows() {
        /*SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CUSTOMER_TABLE_NAME);
        return numRows;*/

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + CUSTOMER_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    //-------------------Asset Management Path Method--------------------------

    public boolean insertAssetCoordinates(AssetCoordinates asset) {
//        Log.d("tag", "asset data in db is" + asset.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_lATITUDE, asset.getLatitude());
        contentValues.put(USER_LONGITUDE, asset.getLongitude());
        long rowInserted = db.insert(ASSET_LOCATION_TABLE_NAME, null, contentValues);
        if (rowInserted != -1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<AssetCoordinates> getAllAssetPath() {
        ArrayList<AssetCoordinates> array_list = new ArrayList<>();
//        Log.d("assets value:", "fetch all value method");
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ASSET_LOCATION_TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            AssetCoordinates cust = new AssetCoordinates();
            cust.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(USER_lATITUDE))));
            cust.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(USER_LONGITUDE))));
            array_list.add(cust);
            res.moveToNext();
        }

        return array_list;
    }


    public Integer deleteAllAssetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        int value = db.delete(ASSET_LOCATION_TABLE_NAME, null, null);
//        Log.d("asset value:", "Total Asset Path deleted " + value);
        return value;
    }

    public int numberOfAsset() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + ASSET_LOCATION_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}

