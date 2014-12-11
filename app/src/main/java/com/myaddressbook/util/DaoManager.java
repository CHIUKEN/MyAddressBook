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
import com.myaddressbook.ColorGenerator;
import com.myaddressbook.Model.Contacts;

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


        if (query.list().size() == 0) {
            Log.d(Tag, "Insert:---getPeopleNo:" + addressBook.getPeopleNo() + " getParentNo: " + addressBook.getParentNo() + " getPeopleName:" + addressBook.getPeopleName());
            addressBookDao.insert(addressBook);
            return true;
        } else {
            Log.d(Tag, "No Insert  getPeopleNo:" + addressBook.getPeopleNo() + " getParentNo: " + addressBook.getParentNo() + " getPeopleName:" + addressBook.getPeopleName());
            return false;

        }
    }

    //建立群組
    public boolean InsertAdd(String groupname, int level, String parentNo) {
        QueryBuilder<AddressBook> queryBuilder = getAddressBookQuery();
        queryBuilder.where(AddressBookDao.Properties.LevelNum.eq(level))
                .orderDesc(AddressBookDao.Properties.CreateDate).limit(1).unique();
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
        //insert
        AddressBook addressBook1 = new AddressBook(null, peopleNo, groupname, level, parentNo, groupname, "", "", "", "", "", "", "", "", "#00DD00", String.valueOf(size), new Date());
        boolean isSuccess = InsertAddressBook(addressBook1);

        if (isSuccess) {
            if (level == 1) {
                //新增二三層
                String parent = insertChildren(2, peopleNo);
                insertChildren(3, parent);
            }
            if (level == 2) {
                //新增第三層
                insertChildren(3, peopleNo);
            }
        }

        return isSuccess;
    }


    public String insertChildren(int level, String parentNo) {
        QueryBuilder<AddressBook> queryBuilder = getAddressBookQuery();
        queryBuilder.where(AddressBookDao.Properties.LevelNum.eq(level), AddressBookDao.Properties.ParentNo.eq(parentNo))
                .orderDesc(AddressBookDao.Properties.CreateDate).limit(1).unique();
        long childId = 0;
        //在該群組內已有子項目
        if (queryBuilder.list().size() > 0) {
            if (level == 2) {
                childId = Long.parseLong(queryBuilder.list().get(0).getPeopleNo()) + 10000000;
            } else {
                childId = Long.parseLong(queryBuilder.list().get(0).getPeopleNo()) + 10000;
            }
        } else {
            if (level == 2) {
                childId = Long.parseLong(parentNo) + 10000000;
            } else {
                childId = Long.parseLong(parentNo) + 10000;
            }
        }
        AddressBook addressBook = new AddressBook(null, String.valueOf(childId), "未分類", level, parentNo, "未分類", "", "", "", "", "", "", "", "", "#770077", "1", new Date());
        InsertAddressBook(addressBook);
        return addressBook.getPeopleNo();
    }

    /*
    * 新增名單
    *
    * */
    public void InsertPeopleList(String parentNo, String parentName, List<Contacts> contactsList) {
        for (int i = 0; i < contactsList.size(); i++) {
            AddressBook addressBook = getAddressBookQuery().where(AddressBookDao.Properties.LevelNum.eq(4),
                    AddressBookDao.Properties.ParentNo.eq(parentNo)).orderAsc(AddressBookDao.Properties.Sort).limit(1).unique();
            int peopleId;
            int sort = 1;

            if (addressBook == null) {
                peopleId = Integer.parseInt(parentNo) + 1;
                sort++;
            } else {
                peopleId = Integer.parseInt(addressBook.getPeopleNo());
                sort = Integer.parseInt(addressBook.getSort());
            }
            Contacts contacts = contactsList.get(i);
            AddressBook addressbook = new AddressBook(null, String.valueOf(peopleId + 1), contacts.getContactsName(), 4, parentNo, parentName,
                    "tel", contacts.getContactsPhone(), contacts.getContactsEmail(), "", "", "", "", "",
                    String.valueOf(ColorGenerator.DEFAULT.getRandomColor()), String.valueOf(sort), new Date());
            addressBookDao.insert(addressbook);
            Log.d(Tag, "getPeopleNo------" + addressbook.getPeopleNo() + "-----getParentNo-----" + addressbook.getParentNo() + "-----PeopleName-----" +
                    addressbook.getPeopleName() + "----getSort-----" + addressbook.getSort());
        }
    }

    //更新tag
    public void UpdatePeopleTag(AddressBook addressbook) {
        //TODO:UPDATE TAG BY USER
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

    //取得全部資料
    public List<AddressBook> getall() {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        return addressBookDao.loadAll();
    }

    //取得父項中文名稱
    public AddressBook getParentNameByParentNo(String parentNo) {
        String parentName = null;
        AddressBook addressBook = new AddressBook();
        try {
            addressBook = getAddressBookQuery().where(AddressBookDao.Properties.PeopleNo.eq(parentNo)).limit(1).unique();
        } catch (Exception ex) {
            Log.e(Tag, ex.getMessage());
            return addressBook;
        }
        return addressBook;

    }

    public String getParentNameByParentName(String parentname) {
        String parentName = null;
        parentName = getAddressBookQuery().where(AddressBookDao.Properties.PeopleName.eq(parentname)).list().get(0).getPeopleName();
        return parentName;
    }

    private QueryBuilder<AddressBook> getAddressBookQuery() {
        return addressBookDao.queryBuilder();
    }
}
