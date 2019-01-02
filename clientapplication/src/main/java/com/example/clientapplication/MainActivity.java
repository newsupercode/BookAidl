package com.example.clientapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ipcapplication.Book;
import com.example.administrator.ipcapplication.IBookManager;
import com.example.administrator.ipcapplication.IOnNewBookArrivedListener;

import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = "BookManagerActivity";
    private IBookManager bookManager;
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "received new book:" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private ServiceConnection mService = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.e(TAG, "onServiceConnected=====");
            bookManager = IBookManager.Stub.asInterface(service);
            try {
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
                Log.e(TAG, "query book list:" + list.toString());
                Book newBook = new Book("android进阶", 3);
                bookManager.addBook(newBook);
                Log.e(TAG, "add book:" + newBook);
                List<Book> newList = bookManager.getBookList();
                Log.e(TAG, "query book list:" + newList.toString());
                bookManager.registerListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManager = null;
            Log.e(TAG, "binder died.");
        }
    };

    private IOnNewBookArrivedListener mNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void OnNewBookArrivedListener(Book book) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
        }
    };
    private TextView tvBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvBind = findViewById(R.id.tvBind);
        tvBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService();

            }
        });

    }

    private void bindService() {
        Log.e(TAG, "bindService=========");
        Intent intent = new Intent("com.example.administrator.ipcapplication.BookService");
//        intent.setAction("com.example.administrator.ipcapplication.BookService");
        intent.setPackage("com.example.administrator.ipcapplication");

//        intent.setComponent(new ComponentName("com.example.administrator.ipcapplication", "com.example.administrator.ipcapplication.BookService"));
        startService(intent);

        boolean isBind = this.getApplicationContext().bindService(intent, mService, Context.BIND_AUTO_CREATE);
        Log.e(TAG,"isBind===="+isBind);

//        if(mService==null) {
//            Log.e(TAG,"mService==null");
//        }else {
//            Log.e(TAG,"mService!=null");
//        }
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()) {
            Log.e(TAG, "unregister listener:" + mNewBookArrivedListener);
            try {
                bookManager.unregisterListener(mNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mService);
        super.onDestroy();
    }

}
