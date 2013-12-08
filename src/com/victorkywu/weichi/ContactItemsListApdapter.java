package com.victorkywu.weichi;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ContactItemsListApdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<ContactItem> contactItems;
	
	public ContactItemsListApdapter(Context context, ArrayList<ContactItem> contactItems) {		
		this.context = context;
		this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.contactItems = contactItems;
	}
	
    @Override
    public int getCount() {
        return contactItems.size();
    }
    
    @Override
    public Object getItem(int position) {
        return contactItems.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_contact, parent, false);
        }

        ContactItem contactItem = (ContactItem) getItem(position);

        ((TextView) view.findViewById(R.id.tvDescr)).setText(contactItem.displayName);
        ((TextView) view.findViewById(R.id.tvPrice)).setText(contactItem.phoneNumber + "");        

        CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);        
        //cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(contactItem.isChecked);
        return view;
    }    
}