package com.example.admin.appsaldos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 15/09/2017.
 */
public class basedados extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String nomebase = "contactsManager";

    // Contacts table name
    private static final String tabela = "contacts";
// era aqui o erro
    // Contacts Table Columns names

    /////////////////////////// O ERRRRRRRRRRRRRRRRRRRRROOOOOOOOOOOOOOOOOOO DA AQUI devido a alateraçao aqui ter outra coisa na cache
    private static final String KEY_ID = "id";
    private static final String KEY_utilizador = "utilizador";
    private static final String KEY_pass = "pass";
    private static final String KEY_Escola = "escola";

    public basedados(Context context) {
        super(context, nomebase, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + tabela + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_utilizador + " TEXT,"
                + KEY_pass + " TEXT," + KEY_Escola + ")";
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

       // apaga a tabela se exstir

        // Cria a tabela
        onCreate(db);

    }
/// vamos aceder a nossa class utilizador

    void addUser(User user) {
      SQLiteDatabase db = this.getWritableDatabase();
 ContentValues values = new ContentValues();
      values.put(KEY_utilizador, user.getUser()); // Contact Name
       values.put(KEY_pass, user.getPass()); // Contact Phone
        values.put(KEY_Escola, user.getEscola());
    db.insert(tabela, null, values);
      db.close(); // Closing database connection
     }


    public User getUser(int id) {
     SQLiteDatabase db = this.getReadableDatabase();

       Cursor cursor = db.query(tabela, new String[] { KEY_ID,
                KEY_utilizador, KEY_pass , KEY_Escola}, KEY_ID + "=?",
      new String[] { String.valueOf(id) }, null, null, null, null);
       if (cursor != null)
  cursor.moveToFirst();

 User user = new User(Integer.parseInt(cursor.getString(0)),
cursor.getString(1), cursor.getString(2), (cursor.getString(3)));
   // return contact
    return user;
    }
    public boolean checkUser(int id) {

        // array of columns to fetch
        String[] columns = {
                KEY_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_ID+ " = ?";

        // selection arguments
        String[] selectionArgs = {String.valueOf(id)};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(tabela, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /// ver se é preciso chamar o construtor utilzador

}
