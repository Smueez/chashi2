package com.example.chashi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtilClass {
    private static DatabaseReference databaseReference;
    private static FirebaseDatabase database;
    public final int WRITE_SUCCESSFUL=1,FAILED_SAME_CODE=2;



    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            getDatabase();
            databaseReference = database.getReference();
        }
        return databaseReference;
    }

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();

        }
        return database;
    }

}
