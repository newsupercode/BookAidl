package com.example.administrator.ipcapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    //Parcelable利用Parcel out 将数据存储到内存中，然后通过Parcel in 从内存中获取数据。
    //AIDL只能传送继承Parcelable接口的类
    private String mBookName;
    private int mBookId;

    public Book(String mBookName, int mBookId) {
        this.mBookName = mBookName;
        this.mBookId = mBookId;
    }

    public String getmBookName() {
        return mBookName;
    }

    public void setmBookName(String mBookName) {
        this.mBookName = mBookName;
    }

    public int getmBookId() {
        return mBookId;
    }

    public void setmBookId(int mBookId) {
        this.mBookId = mBookId;
    }
    /*
    返回当前对象的描素内容，如果含有文件描述符（什么叫文件描述符）则返回1，否则返回0，一般都返回0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        //  Parcel out :系统提供的输出流，将成员变量存储到内存中。
        //  int flags:0或1，1表示当前对象需要作为返回值保存（不明白），基本上所有情况都为0,
        out.writeInt(mBookId);
        out.writeString(mBookName);

    }
    public void readFromParcel(Parcel in){
        mBookId=in.readInt();
        mBookName=in.readString();
    }
    public static final Parcelable.Creator<Book> CREATOR= new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);////获取输入流，反序列化对象
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];//
        }
    };

    private Book(Parcel in){
        mBookId=in.readInt();
        mBookName=in.readString();
    }

}
