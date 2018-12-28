package com.example.administrator.ipcapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import com.example.administrator.ipcapplication.Book;
import com.example.administrator.ipcapplication.IOnNewBookArrivedListener;
import com.example.administrator.ipcapplication.IBookManager;
public class BookService  extends Service {
    private CopyOnWriteArrayList<Book> mBooklist=new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners=new RemoteCallbackList<>();
    private AtomicBoolean mIsServiceDestoryed =new AtomicBoolean();

    //创建BInder对象
    public Binder mBinder=new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooklist;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooklist.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.register(listener);

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBooklist.add(new Book("Android",1));
        mBooklist.add(new Book("Ios",2));
        new Thread(new serviceWork()).start();
    }
    //    //线程类。在每休眠5秒后，会自动添加一本书，
    // 并通过OnNewBookArrived()方法通知所有观察者。
    public class serviceWork implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBooklist.size() + 1;
                Book newBook = new Book("new Book #" + bookId,bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        }

        private void onNewBookArrived(Book book) throws RemoteException {
            mBooklist.add(book);
            int beginBroadcast = mListeners.beginBroadcast();
            Log.e("onNewBookArrived","registener listener size:" + beginBroadcast);
            for(int i = 0; i < beginBroadcast; i++) {
                IOnNewBookArrivedListener broadcastItem = mListeners.getBroadcastItem(i);
                if(broadcastItem!=null) {
                    broadcastItem.OnNewBookArrivedListener(book);

                }
            }
            mListeners.finishBroadcast();
        }

    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();

    }
}

