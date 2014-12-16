package com.myaddressbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.CheckBox;
import com.myaddressbook.Model.Contacts;
import com.myaddressbook.R;
import com.myaddressbook.app.AppController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 2014/12/16.
 */
public class ContactsAdapter extends ArrayAdapter<Contacts> {
    private List<Contacts> contactsList;

    public ContactsAdapter(Context context, ArrayList<Contacts> contacts) {
        super(context, 0, contacts);
        this.contactsList = new ArrayList<Contacts>();
        this.contactsList = AppController.getInstance().getContactsList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item
        final Contacts contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ContactViewHolder viewholder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_newpeople, parent, false);
            viewholder=new ContactViewHolder(view);
            view.setTag(viewholder);
        }else{
            viewholder= (ContactViewHolder) view.getTag();
        }

        String email = "";
        String phone = "";
        if (contact.emails.size() > 0 && contact.emails.get(0) != null) {
            email=contact.emails.get(0).address;
            contact.setContactsEmail(email);
        }
        if (contact.numbers.size() > 0 && contact.numbers.get(0) != null) {
            phone=contact.numbers.get(0).number;
            contact.setContactsPhone(phone);
        }
        viewholder.build(contact.getContactsName(),phone,email,contactsList.contains(contact));

        viewholder.checkbox.setOncheckListener(new CheckBox.OnCheckListener() {
                @Override
                public void onCheck(boolean isChecked) {
                    if (isChecked) {
                        contactsList.add(contact);
                    } else {
                        contactsList.remove(contact);
                    }
                    notifyDataSetChanged();
                }
        });


        return view;

    }

    public List<Contacts> getSelectedItems() {
        return contactsList;
    }
    class ContactViewHolder{
        TextView txt_name;
        TextView txt_phone;
        TextView txt_email;
        public CheckBox checkbox;
        public ContactViewHolder(View view){
            this.txt_name=(TextView)view.findViewById(R.id.txt_name);
            this.txt_phone=(TextView)view.findViewById(R.id.txt_phone);
            this.txt_email=(TextView)view.findViewById(R.id.txt_email);
            this.checkbox=(CheckBox)view.findViewById(R.id.selected);
            txt_email.setText("");
            txt_phone.setText("");

        }
        public void build(String name,String phone,String email,boolean selected){
            txt_email.setText(email);
            txt_name.setText(name);
            txt_phone.setText(phone);
            checkbox.setChecked(selected);
        }
    }
}
