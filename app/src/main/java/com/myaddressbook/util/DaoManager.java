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
import java.util.logging.Level;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by K on 2014/12/6.
 */
public class DaoManager {
    private static String Tag = DaoManager.class.getName();
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
        query.where(AddressBookDao.Properties.PeopleName.eq(addressBook.getPeopleName()),
                AddressBookDao.Properties.LevelNum.eq(addressBook.getLevelNum()),
                AddressBookDao.Properties.ParentNo.eq(addressBook.getParentNo()));
        Log.d(Tag, "getPeopleNo:"+addressBook.getPeopleNo() + " getParentNo: " + addressBook.getParentNo() + " getPeopleName:" + addressBook.getPeopleName());

        if (query.list().size() == 0) {
            Log.d(Tag, "Insert:---getPeopleNo:"+addressBook.getPeopleNo() + " getParentNo: " + addressBook.getParentNo() + " getPeopleName:" + addressBook.getPeopleName());
            addressBookDao.insert(addressBook);
            return true;
        }else {
            Log.d(Tag, "getPeopleNo:"+addressBook.getPeopleNo() + " getParentNo: " + addressBook.getParentNo() + " getPeopleName:" + addressBook.getPeopleName());
            return false;

        }
    }

    //建立群組
    public boolean InsertAdd(String groupname, int level, String parentNo) {
        QueryBuilder<AddressBook> queryBuilder = getAddressBookQuery();
        queryBuilder.where(AddressBookDao.Properties.LevelNum.eq(level)).orderDesc(AddressBookDao.Properties.CreateDate).limit(1).unique();
        //取得最大的peopleNo
        int size = queryBuilder.list().size();
        AddressBook addressBook = queryBuilder.list().get(size - 1);
        //Log.d(Tag, addressBook.getPeopleNo());
        int id = 0;
        //建立第一層群組
        if (level == 1) {
            id = 1000000000;
            //建立第二層群組
        } else if (level == 2) {
            id = 10000000;
            //建立第三層群組
        } else if (level == 3) {
            id = 10000;
        }
        String peopleNo = String.valueOf(Long.parseLong(addressBook.getPeopleNo()) + id);

        if (level == 1) {
            //新增二三層
            String parent = InsertChildren(2, peopleNo);
            InsertChildren(3, parent);
        }
        if (level == 2) {
            //新增第三層
            InsertChildren(3, peopleNo);
        }

        //insert
        AddressBook addressBook1 = new AddressBook(null, peopleNo, groupname, level, parentNo, groupname, "", "", "", "", "", "", "", "", "#00DD00", String.valueOf(size), new Date());
        return InsertAddressBook(addressBook1);
    }

    private String InsertChildren(int level, String parentNo) {
        QueryBuilder<AddressBook> queryBuilder = getAddressBookQuery();
        queryBuilder.where(AddressBookDao.Properties.LevelNum.eq(level)).orderDesc(AddressBookDao.Properties.CreateDate).limit(1).unique();
        //取得最大的peopleNo
        int size = queryBuilder.list().size();
        //取得該層級最後的id

        AddressBook addressBook = queryBuilder.list().get(size - 1);

        int id = 0;
        //建立第一層群組
        if (level == 1) {
            id = 1000000000;
            //建立第二層群組
        } else if (level == 2) {
            id = 10000000;
            //建立第三層群組
        } else if (level == 3) {
            id = 10000;
        }
        String peopleNo = String.valueOf(Long.parseLong(addressBook.getPeopleNo()) + id);
        //insert
        AddressBook addressBook1 = new AddressBook(null, peopleNo, "不分類", level, parentNo, "不分類", "", "", "", "", "", "", "", "", "#770077", String.valueOf(size), new Date());
        InsertAddressBook(addressBook1);
        return peopleNo;
    }


    //新增Tag
    public void InsertTag(Tag tag) {
        tagDao.insert(tag);
    }

    //取得層級中的資料
    public List<AddressBook> getAddressBookList(int Level, String parentNo) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(Level), AddressBookDao.Properties.ParentNo.eq(parentNo)).orderAsc(AddressBookDao.Properties.Sort);
        return query.list();
    }

    public List<AddressBook> getall() {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        return addressBookDao.loadAll();
    }


    private QueryBuilder<AddressBook> getAddressBookQuery() {
        return addressBookDao.queryBuilder();
    }
}
