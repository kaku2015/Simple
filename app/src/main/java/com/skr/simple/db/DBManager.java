package com.skr.simple.db;

import android.content.Context;

import com.skr.simple.App;
import com.skr.simple.BuildConfig;
import com.skr.simple.db.entity.DaoMaster;
import com.skr.simple.db.entity.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;


/**
 * @author hyw
 * @since 2016/11/23
 */
public class DBManager {
    private static DBManager sDBManager;
    private DaoSession mDaoSession;
//    private final String KEY_SQL = "xxxxxxxxx";

    private DBManager(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DBConstants.DB_DATA_GATHER);
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        // 数据库加密
//        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getEncryptedWritableDb(KEY_SQL));
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        // 在 QueryBuilder 类中内置两个 Flag 用于方便输出执行的 SQL 语句与传递参数的值
        QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
        QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
    }

    public static DBManager getInstance(Context context) {
        if (sDBManager == null) {
            sDBManager = new DBManager(context);
        }
        return sDBManager;

    }

    public static DBManager getInstance() {
        if (sDBManager == null) {
            sDBManager = new DBManager(App.getAppContext());
        }
        return sDBManager;

    }
}
