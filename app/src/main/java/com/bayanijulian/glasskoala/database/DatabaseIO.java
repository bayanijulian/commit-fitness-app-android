package com.bayanijulian.glasskoala.database;


import java.util.List;

public class DatabaseIO {
    private static final String TAG = DatabaseIO.class.getSimpleName();

    public interface ListListener<T> {
        void onComplete(List<T> data);
    }

    public interface SingleListener<T> {
        void onComplete(T data);
    }
}
