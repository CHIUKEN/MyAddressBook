package com.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Created by K on 2014/12/3.
 */
public class InitializeDaoGenerator {
    public static void main(String[] args) throws Exception {
        //第一個為DB版本號，第二個為PageName
        Schema schema = new Schema(1, "com.daogenerator");

        addAddressBook(schema);
        addTag(schema);
        //addCustomerOrder(schema);
        //自動生成項目檔案路徑
        new DaoGenerator().generateAll(schema,"./app/src/main/java");
    }
    //建立DB table 及欄位
    private static void addAddressBook(Schema schema) {
        //table名稱
        Entity entity = schema.addEntity("AddressBook");
        //欄位
        //DB自增值，KEY
        entity.addIdProperty().autoincrement().primaryKey();
        entity.addStringProperty("PeopleNo");
        entity.addStringProperty("PeopleName");
        entity.addIntProperty("LevelNum");
        entity.addStringProperty("ParentNo");
        entity.addStringProperty("ParentName");
        entity.addStringProperty("PeopleTel");
        entity.addStringProperty("PeoplePhone");
        entity.addStringProperty("PeopleEmail");
        entity.addStringProperty("PeopleNote");
        entity.addStringProperty("TagId1");
        entity.addStringProperty("Tag1Name");
        entity.addStringProperty("TagId2");
        entity.addStringProperty("Tag2Name");
        entity.addStringProperty("DisplayColor");
        entity.addStringProperty("Sort");
        entity.addDateProperty("CreateDate");
        //entity.addIntProperty("");


    }
    private static void addTag(Schema schema){
        //table名稱
        Entity entity = schema.addEntity("Tag");
        //欄位
        //DB自增值，KEY
        entity.addIdProperty().autoincrement().primaryKey();
        entity.addStringProperty("TagId");
        entity.addStringProperty("TagName");
        entity.addStringProperty("Sort");
        entity.addStringProperty("TagDisplayColor");

    }
    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);
        //一對多
        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }
}
