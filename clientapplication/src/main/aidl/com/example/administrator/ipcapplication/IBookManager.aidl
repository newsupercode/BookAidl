// IBookManager.aidl
package com.example.administrator.ipcapplication;
import  com.example.administrator.ipcapplication.Book;
import  com.example.administrator.ipcapplication.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
           void addBook(in Book book);
           void registerListener(IOnNewBookArrivedListener listener);
           void unregisterListener(IOnNewBookArrivedListener listener);
}
