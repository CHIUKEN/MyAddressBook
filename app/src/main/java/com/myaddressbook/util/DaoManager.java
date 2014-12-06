package com.myaddressbook.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daogenerator.AddressBook;
import com.daogenerator.AddressBookDao;
import com.daogenerator.DaoMaster;
import com.daogenerator.DaoSession;
import com.daogenerator.Tag;
import com.daogenerator.TagDao;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by K on 2014/12/6.
 */
public class DaoManager {
    private static String Tag=DaoManager.class.getName();
    private AddressBookDao addressBookDao;

    private TagDao tagDao;
    // Context
    Context _context;

    public DaoManager(Context context) {
        this._context = context;
        //建立db
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "addressbookdb", null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        DaoSession session = master.newSession();
        addressBookDao = session.getAddressBookDao();
        tagDao = session.getTagDao();
    }

    //新增通訊錄
    public boolean InsertAddressBook(AddressBook addressBook) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.PeopleName.eq(addressBook.getPeopleName()));
        if (query.list().size() == 0) {
            addressBookDao.insert(addressBook);
            return true;
        }
        return false;
    }

    public boolean InsertAdd(String groupname, String level,String parentNo) {
        QueryBuilder<AddressBook> queryBuilder = getAddressBookQuery();
        queryBuilder.where(AddressBookDao.Properties.LevelNum.eq(level)).orderDesc(AddressBookDao.Properties.CreateDate).limit(1).unique();
        //取得最大的peopleNo
        AddressBook addressBook = queryBuilder.list().get(queryBuilder.list().size() - 1);
        Log.d(Tag,addressBook.getPeopleNo());
        String peopleNo = String.valueOf(Long.parseLong(addressBook.getPeopleNo()) + 10000000);
        //insert
        AddressBook addressBook1=new AddressBook(null,peopleNo,groupname,level,parentNo,groupname,"","","","","","","","","#00DD00",new Date());
        return InsertAddressBook(addressBook1);
    }

    //新增Tag
    public void InsertTag(Tag tag) {
        tagDao.insert(tag);
    }

    //取得層級中的資料
    public List<AddressBook> getAddressBookList(String Level) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(Level));
        return query.list();
    }

    private QueryBuilder<AddressBook> getAddressBookQuery() {
        return addressBookDao.queryBuilder();
    }
}
