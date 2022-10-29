package code.camp.databasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.sqLiteDatabase", null, 1);
        System.out.println("Been here");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " +
                CUSTOMER_TABLE +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CUSTOMER_NAME + " TEXT, " +
                CUSTOMER_AGE + " INT, " +
                ACTIVE_CUSTOMER + " BOOL)";

        sqLiteDatabase.execSQL(createTableStatement);

        System.out.println("Been here on create");
    }

    // upgrade on database (eg new tables or new fields)
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /*
    *Intents are a messaging architecture for sending /receiving transactional commands and data.
    *
    * Content providers are an abstract interface to stored data for create,update,
    * delete and sync operations.*/


    public  boolean addOne(CustomerModel customerModel)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CUSTOMER_NAME, customerModel.getName());
        cv.put(CUSTOMER_AGE, customerModel.getAge());
        cv.put(ACTIVE_CUSTOMER, customerModel.isActive());

        long result = sqLiteDatabase.insert(CUSTOMER_TABLE, null ,cv);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public boolean DeleteOne(CustomerModel customerModel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String qqueryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + ID + " = " + customerModel.getId();
        Cursor cursor = db.rawQuery(qqueryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public List<CustomerModel> getEveryone()
    {
        List<CustomerModel> returnList = new ArrayList<>();
        String queryString = "SELECT * from " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst())
        {
            // loop through the cursor (result set) and create new customer obj. Put them into the return list
            do{
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerID,customerName, customerAge, customerActive);
                returnList.add(newCustomer);
            }while(cursor.moveToNext());
        }
        else
        {

        }
        cursor.close();
        db.close();
        return returnList;

    }

}
