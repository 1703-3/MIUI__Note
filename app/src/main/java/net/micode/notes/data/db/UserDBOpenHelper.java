package net.micode.notes.data.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.micode.notes.data.model.User;

import java.util.ArrayList;


public class UserDBOpenHelper extends SQLiteOpenHelper {
    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    private final SQLiteDatabase db;

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * db = getReadableDatabase();
     */
    public UserDBOpenHelper(Context context){
        super(context,"db_test",null,1);
        db = getReadableDatabase();
    }

    /**
     *  创建数据表
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    /**
     * 自定义的增删改查方法
     * add()
     * delete()
     * update()
     * getAllData()
     */
    public void add(String name,String password){
        db.execSQL("INSERT INTO user (name,password) VALUES(?,?)",new Object[]{name,password});
    }
    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }



    /**
     * 使用游标类 Cursor 完成对数据库查询的工作
     * 第一个参数："表名"，中间5个：null，最后是查询出来内容的排序方式："name DESC"
     *
     */
    public ArrayList<User> getAllData(){

        ArrayList<User> list = new ArrayList<>();
        Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            //按照用户名，密码构造新用户
            list.add(new User(name,password));
        }
        return list;
    }
}

