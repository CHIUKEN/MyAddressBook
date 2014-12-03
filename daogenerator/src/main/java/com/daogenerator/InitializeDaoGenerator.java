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
        entity.addStringProperty("LevelNum");
        entity.addStringProperty("ParentNo");
        entity.addStringProperty("FileName");
        entity.addStringProperty("PeopleTel");
        entity.addStringProperty("PeoplePhone");
        entity.addStringProperty("PeopleEmail");
        entity.addStringProperty("PeopleNote");
        entity.addStringProperty("TagId");
        entity.addStringProperty("TagName");
        entity.addDateProperty("CreateDate");
        //entity.addIntProperty("");


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
