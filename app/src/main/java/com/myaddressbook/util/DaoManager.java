package com.myaddressbook.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;

import com.daogenerator.AddressBook;
import com.daogenerator.AddressBookDao;
import com.daogenerator.DaoMaster;
import com.daogenerator.DaoSession;
import com.daogenerator.Tag;
import com.daogenerator.TagDao;
import com.myaddressbook.ColorGenerator;
import com.myaddressbook.Model.Contacts;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import de.greenrobot.dao.query.Query;
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
                .orderDesc(AddressBookDao.Properties.Sort).limit(1).unique();
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
        int sort = Integer.parseInt(addressBook.getSort()) + 1;
        //insert
        AddressBook addressBook1 = new AddressBook(null, peopleNo, groupname, level, parentNo, groupname, "", "", "", "", "", "", "", "", "#00DD00", String.valueOf(sort), new Date());
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
            long peopleId;
            int sort = 1000;

            if (addressBook == null) {
                peopleId = Long.parseLong(parentNo) + 1;
                sort++;
            } else {
                peopleId = Long.parseLong(addressBook.getPeopleNo());
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
    public void updatePeopleTag(AddressBook addressbook) {
        //TODO:UPDATE TAG BY USER
    }

    //
    public void updateSingleAddressbook(AddressBook addressBook) {
        addressBookDao.update(addressBook);
    }

    //新增Tag
    public boolean insertTag(String tagName) {
        Tag old = tagDao.queryBuilder().limit(1).unique();
        Tag newTag = null;
        try {
            if (old == null) {
                newTag = new Tag(null, "1", tagName, "1000", "#3f51b5");
            } else {
                newTag = new Tag(null, String.valueOf(old.getId() + 1), tagName, String.valueOf(Integer.parseInt(old.getSort()) + 1), "#3f51b5");

            }
            tagDao.insert(newTag);
            return true;
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
            return false;
        }
    }

    //取得全部的tag
    public List<Tag> getAllTag() {
        return tagDao.queryBuilder().orderAsc(TagDao.Properties.Sort).list();
    }

    //取得tagid的集合
    public List<AddressBook> getAddressBookByTag(String tagid) {
        List<AddressBook> addressBooks = new ArrayList<AddressBook>();
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        //Query q= query.where(AddressBookDao.Properties.LevelNum.eq(4)).build();
        //query.or(AddressBookDao.Properties.TagId1.eq(tagid),
//                AddressBookDao.Properties.TagId2.eq(tagid));
        query.where(AddressBookDao.Properties.TagId1.eq(tagid)).build();
        addressBooks.addAll(query.list());
        query.where(AddressBookDao.Properties.TagId2.eq(tagid)).build();
        addressBooks.addAll(query.list());
        return addressBooks;
    }

    public void updateSingleTag(Tag tag) {
        tagDao.update(tag);
    }

    //更改標籤排序
    public void updateTagSort(List<Tag> tagList) {
        for (int i = 0; i < tagList.size(); i++) {
            tagDao.update(tagList.get(i));
        }
    }

    //取得層級中的資料
    public List<AddressBook> getAddressBookList(int Level, String parentNo) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(Level), AddressBookDao.Properties.ParentNo.eq(parentNo)).orderAsc(AddressBookDao.Properties.Sort);
        return query.list();
    }

    //取得第四層全部資料
    public List<AddressBook> getall() {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(4)).orderDesc(AddressBookDao.Properties.ParentNo);
        return query.list();
    }

    public boolean queryIsExist(String name) {
        QueryBuilder<AddressBook> query = addressBookDao.queryBuilder();
        query.where(AddressBookDao.Properties.LevelNum.eq(4), AddressBookDao.Properties.PeopleName.eq(name));
        query.buildCount().count();
        return query.buildCount().count() > 0 ? true : false;
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

    //更改資料
    public void updateDataByCreateNewGroup(List<AddressBook> addressBookList, String parentNo) {
        for (int i = 0; i < addressBookList.size(); i++) {
            addressBookList.get(i).setParentNo(parentNo);
            addressBookDao.update(addressBookList.get(i));

        }
    }

    public String getParentNameByParentName(String parentname) {
        String parentName = null;
        parentName = getAddressBookQuery().where(AddressBookDao.Properties.PeopleName.eq(parentname)).list().get(0).getPeopleName();
        return parentName;
    }

    private QueryBuilder<AddressBook> getAddressBookQuery() {
        return addressBookDao.queryBuilder();
    }

    //篩選群組
    public List<AddressBook> filterGroup(int Level, String parentNo, String strQuery) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(Level), AddressBookDao.Properties.ParentNo.eq(parentNo),
                AddressBookDao.Properties.PeopleName.like(strQuery)).orderAsc(AddressBookDao.Properties.Sort);
        return query.list();
    }

    //篩選標籤
    public List<Tag> filerTag(String str) {
        QueryBuilder<Tag> query = tagDao.queryBuilder();
        query.where(TagDao.Properties.TagName.eq(str));
        return query.list();
    }

    //快速搜尋
    public List<AddressBook> searchAll(String strQuery) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.LevelNum.eq(4), AddressBookDao.Properties.PeopleName.like(strQuery)).orderDesc(AddressBookDao.Properties.PeopleNo);
        return query.list();
    }

    //更改群組排序
    public void updateDataForSort(List<AddressBook> addressBookList) {
        for (int i = 0; i < addressBookList.size(); i++) {
            addressBookDao.update(addressBookList.get(i));
        }
    }

    //刪除資料
    public void deleteList(List<AddressBook> addressBookList) {
        for (int i = 0; i < addressBookList.size(); i++) {
            addressBookDao.delete(addressBookList.get(i));
        }
    }

    //重新設定
    public void deleteAll() {
        addressBookDao.deleteAll();
        tagDao.deleteAll();
        AddressBook addressBook0 = new AddressBook(null, "1000000000", "未分類", 1, "", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
        AddressBook addressBook1 = new AddressBook(null, "1010000000", "未分類", 2, "1000000000", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
        AddressBook addressBook2 = new AddressBook(null, "1010010000", "未分類", 3, "1010000000", "", "", "", "", "", "", "", "", "", "#770077", "1000", new Date());
        addressBookDao.insert(addressBook0);
        addressBookDao.insert(addressBook1);
        addressBookDao.insert(addressBook2);
        InsertAdd("家庭", 1, "");
        InsertAdd("工作", 1, "");
    }

    //刪除群組
    public void deletedGroup(AddressBook addressBook) {
        String peopleId = addressBook.getPeopleNo();
        //刪除當前的群組
        Query query = addressBookDao.queryBuilder().where(AddressBookDao.Properties.LevelNum.eq(addressBook.getLevelNum()),
                AddressBookDao.Properties.PeopleNo.eq(addressBook.getPeopleNo())).build();

        List<AddressBook> result = query.list();
        deleteList(result);

        int level = addressBook.getLevelNum() + 1;
        Query queryNext = addressBookDao.queryBuilder().where(AddressBookDao.Properties.LevelNum.eq(level),
                AddressBookDao.Properties.ParentNo.eq(peopleId)).build();

        List<AddressBook> next = queryNext.list();
        Log.d("db select data:", "=level=" + level + "=peopleId()=" + peopleId + " query.list().size():" + queryNext.list().size());

        for (int i = 0; i < next.size(); i++) {
            //TODO 繼續往下刪...
            deletedGroup(next.get(i));
        }
        deleteList(next);

    }

    public int getParentNoLevel(String ParentNo) {
        QueryBuilder<AddressBook> query = getAddressBookQuery();
        query.where(AddressBookDao.Properties.PeopleNo.eq(ParentNo)).build();
        return query.list().get(0).getLevelNum();
    }

    public List<AddressBook> getAllData() {

        return addressBookDao.loadAll();
    }
}
