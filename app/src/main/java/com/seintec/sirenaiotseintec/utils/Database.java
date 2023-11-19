package com.seintec.sirenaiotseintec.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Database {
    public static CompletableFuture<Boolean> deleteFromDatabase(String reference) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        myref.removeValue().addOnCompleteListener((task) -> {
            future.complete(true);
        }).addOnFailureListener((exception) -> {
            future.completeExceptionally(exception);
        });

        return future;
    }
    public static  <T> CompletableFuture<Boolean> saveInformationDatabase(String reference, T data, String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        myref.child(id).setValue(data).addOnCompleteListener((task) -> {
            future.complete(true);
        }).addOnFailureListener((exception) -> {
            future.completeExceptionally(exception);
        });

        return future;
    }

    public static <T> CompletableFuture<ArrayList<T>> findOnDataBase(String reference, String clave, String data, Class<T> type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<ArrayList<T>> future = new CompletableFuture<>();

        myref.orderByChild(clave).equalTo(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> data = new ArrayList<>();
                try {
                    for (DataSnapshot dataIn : dataSnapshot.getChildren()) {
                        data.add(dataIn.getValue(type));
                    }
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
                future.complete(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public static CompletableFuture<Boolean> referenceContains (String reference) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
    public static <T> CompletableFuture<ArrayList<T>> findOnDataBaseBool(String reference, String clave, boolean data, Class<T> type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<ArrayList<T>> future = new CompletableFuture<>();

        myref.orderByChild(clave).equalTo(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> data = new ArrayList<>();
                for (DataSnapshot dataIn : dataSnapshot.getChildren()) {
                    data.add(dataIn.getValue(type));
                }
                future.complete(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
    public static <T> CompletableFuture<T>  getInformationDatabase(String reference, Class<T> type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(reference);
        CompletableFuture<T> future = new CompletableFuture<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot.getValue(type));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        };

        ref.addListenerForSingleValueEvent(valueEventListener);

        return future;
    }
    public static <T> CompletableFuture<ArrayList<T>>  getInformationDatabaseList(String reference, Class<T> type) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference(reference);
        CompletableFuture<ArrayList<T>> future = new CompletableFuture<>();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<T> data = new ArrayList<>();
                for (DataSnapshot dataIn : dataSnapshot.getChildren()) {
                    data.add(dataIn.getValue(type));
                }
                future.complete(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        };

        myref.addListenerForSingleValueEvent(valueEventListener);


        return future;
    }
}
