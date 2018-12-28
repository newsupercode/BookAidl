// IOnNewBookArrivedListener.aidl
package com.example.administrator.ipcapplication;
import com.example.administrator.ipcapplication.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void OnNewBookArrivedListener(in Book book);
}
