package com.gpets.gpetsapi.infrastructure.firebase.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RealtimeDbService {

    private DatabaseReference ref(String path) {
        return FirebaseDatabase.getInstance().getReference(path);
    }


    public <T> T get(String path, Class<T> clazz) throws Exception {
        CompletableFuture<T> future = new CompletableFuture<>();

        ref(path).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                future.complete(snapshot.getValue(clazz));
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future.get();
    }


    public <T> T get(String path, GenericTypeIndicator<T> type) throws Exception {
        CompletableFuture<T> future = new CompletableFuture<>();

        ref(path).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                future.complete(snapshot.getValue(type));
            }

            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future.get();
    }

    public void set(String path, Object value) {
        ref(path).setValueAsync(value);
    }

    public void update(String path, Object value) {
        ref(path).updateChildrenAsync((java.util.Map<String, Object>) value);
    }

    public DatabaseReference push(String path) {
        return ref(path).push();
    }
}
