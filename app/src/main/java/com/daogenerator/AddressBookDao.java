package com.daogenerator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.daogenerator.AddressBook;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ADDRESS_BOOK.
*/
public class AddressBookDao extends AbstractDao<AddressBook, Long> {

    public static final String TABLENAME = "ADDRESS_BOOK";

    /**
     * Properties of entity AddressBook.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PeopleNo = new Property(1, String.class, "PeopleNo", false, "PEOPLE_NO");
        public final static Property PeopleName = new Property(2, String.class, "PeopleName", false, "PEOPLE_NAME");
        public final static Property LevelNum = new Property(3, String.class, "LevelNum", false, "LEVEL_NUM");
        public final static Property ParentNo = new Property(4, String.class, "ParentNo", false, "PARENT_NO");
        public final static Property FileName = new Property(5, String.class, "FileName", false, "FILE_NAME");
        public final static Property PeopleTel = new Property(6, String.class, "PeopleTel", false, "PEOPLE_TEL");
        public final static Property PeoplePhone = new Property(7, String.class, "PeoplePhone", false, "PEOPLE_PHONE");
        public final static Property PeopleEmail = new Property(8, String.class, "PeopleEmail", false, "PEOPLE_EMAIL");
        public final static Property PeopleNote = new Property(9, String.class, "PeopleNote", false, "PEOPLE_NOTE");
        public final static Property TagId1 = new Property(10, String.class, "TagId1", false, "TAG_ID1");
        public final static Property Tag1Name = new Property(11, String.class, "Tag1Name", false, "TAG1_NAME");
        public final static Property TagId2 = new Property(12, String.class, "TagId2", false, "TAG_ID2");
        public final static Property Tag2Name = new Property(13, String.class, "Tag2Name", false, "TAG2_NAME");
        public final static Property DisplayColor = new Property(14, String.class, "DisplayColor", false, "DISPLAY_COLOR");
        public final static Property Sort = new Property(15, String.class, "Sort", false, "SORT");
        public final static Property CreateDate = new Property(16, java.util.Date.class, "CreateDate", false, "CREATE_DATE");
    };


    public AddressBookDao(DaoConfig config) {
        super(config);
    }
    
    public AddressBookDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ADDRESS_BOOK' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PEOPLE_NO' TEXT," + // 1: PeopleNo
                "'PEOPLE_NAME' TEXT," + // 2: PeopleName
                "'LEVEL_NUM' TEXT," + // 3: LevelNum
                "'PARENT_NO' TEXT," + // 4: ParentNo
                "'FILE_NAME' TEXT," + // 5: FileName
                "'PEOPLE_TEL' TEXT," + // 6: PeopleTel
                "'PEOPLE_PHONE' TEXT," + // 7: PeoplePhone
                "'PEOPLE_EMAIL' TEXT," + // 8: PeopleEmail
                "'PEOPLE_NOTE' TEXT," + // 9: PeopleNote
                "'TAG_ID1' TEXT," + // 10: TagId1
                "'TAG1_NAME' TEXT," + // 11: Tag1Name
                "'TAG_ID2' TEXT," + // 12: TagId2
                "'TAG2_NAME' TEXT," + // 13: Tag2Name
                "'DISPLAY_COLOR' TEXT," + // 14: DisplayColor
                "'SORT' TEXT," + // 15: Sort
                "'CREATE_DATE' INTEGER);"); // 16: CreateDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ADDRESS_BOOK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AddressBook entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String PeopleNo = entity.getPeopleNo();
        if (PeopleNo != null) {
            stmt.bindString(2, PeopleNo);
        }
 
        String PeopleName = entity.getPeopleName();
        if (PeopleName != null) {
            stmt.bindString(3, PeopleName);
        }
 
        String LevelNum = entity.getLevelNum();
        if (LevelNum != null) {
            stmt.bindString(4, LevelNum);
        }
 
        String ParentNo = entity.getParentNo();
        if (ParentNo != null) {
            stmt.bindString(5, ParentNo);
        }
 
        String FileName = entity.getFileName();
        if (FileName != null) {
            stmt.bindString(6, FileName);
        }
 
        String PeopleTel = entity.getPeopleTel();
        if (PeopleTel != null) {
            stmt.bindString(7, PeopleTel);
        }
 
        String PeoplePhone = entity.getPeoplePhone();
        if (PeoplePhone != null) {
            stmt.bindString(8, PeoplePhone);
        }
 
        String PeopleEmail = entity.getPeopleEmail();
        if (PeopleEmail != null) {
            stmt.bindString(9, PeopleEmail);
        }
 
        String PeopleNote = entity.getPeopleNote();
        if (PeopleNote != null) {
            stmt.bindString(10, PeopleNote);
        }
 
        String TagId1 = entity.getTagId1();
        if (TagId1 != null) {
            stmt.bindString(11, TagId1);
        }
 
        String Tag1Name = entity.getTag1Name();
        if (Tag1Name != null) {
            stmt.bindString(12, Tag1Name);
        }
 
        String TagId2 = entity.getTagId2();
        if (TagId2 != null) {
            stmt.bindString(13, TagId2);
        }
 
        String Tag2Name = entity.getTag2Name();
        if (Tag2Name != null) {
            stmt.bindString(14, Tag2Name);
        }
 
        String DisplayColor = entity.getDisplayColor();
        if (DisplayColor != null) {
            stmt.bindString(15, DisplayColor);
        }
 
        String Sort = entity.getSort();
        if (Sort != null) {
            stmt.bindString(16, Sort);
        }
 
        java.util.Date CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindLong(17, CreateDate.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AddressBook readEntity(Cursor cursor, int offset) {
        AddressBook entity = new AddressBook( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // PeopleNo
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // PeopleName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // LevelNum
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ParentNo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // FileName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // PeopleTel
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // PeoplePhone
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // PeopleEmail
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // PeopleNote
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // TagId1
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // Tag1Name
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // TagId2
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // Tag2Name
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // DisplayColor
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // Sort
            cursor.isNull(offset + 16) ? null : new java.util.Date(cursor.getLong(offset + 16)) // CreateDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AddressBook entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPeopleNo(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPeopleName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLevelNum(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setParentNo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFileName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPeopleTel(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPeoplePhone(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPeopleEmail(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPeopleNote(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setTagId1(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTag1Name(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setTagId2(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setTag2Name(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setDisplayColor(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setSort(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setCreateDate(cursor.isNull(offset + 16) ? null : new java.util.Date(cursor.getLong(offset + 16)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AddressBook entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AddressBook entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
