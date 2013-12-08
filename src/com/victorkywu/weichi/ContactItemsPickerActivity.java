package com.victorkywu.weichi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

public class ContactItemsPickerActivity extends Activity {
	
	ArrayList<ContactItem> whoContactItems;
	ContactItemsListApdapter contactItemsListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_items_picker);
		
		readContacts();
		contactItemsListAdapter = new ContactItemsListApdapter(this, whoContactItems);
		
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(contactItemsListAdapter); 
	}
	
	private void readContacts() {		
		whoContactItems = new ArrayList<ContactItem>();
		
		ContentResolver contentResolver = getContentResolver();
		
		Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        String[] contactsProjection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,                
        };
        String contactsSelection = 
        		ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'" + " AND " + 
        		ContactsContract.Contacts.IN_VISIBLE_GROUP + "='1'";        
        String contactsSortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";				
		Cursor cursorContacts = contentResolver.query(contactsUri, contactsProjection, contactsSelection, null, contactsSortOrder);		
		int displayNameColumnIndex = cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		int contactIdColumnIndex = cursorContacts.getColumnIndex(ContactsContract.Contacts._ID);
		
		while (cursorContacts.moveToNext()) {
			
			String displayName = cursorContacts.getString(displayNameColumnIndex);						
			String contactId = cursorContacts.getString(contactIdColumnIndex);
							
			Uri phonesUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String[] phonesProjection = new String[] { 
					ContactsContract.CommonDataKinds.Phone.NUMBER 
			};
			String phonesSelection = 
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+ contactId + " AND " + 
					ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;			
			Cursor cursorPhones = contentResolver.query(phonesUri, phonesProjection, phonesSelection, null, null); 
			int numberColumnIndex = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			
			while (cursorPhones.moveToNext()) {							
			
				String phoneNumber = cursorPhones.getString(numberColumnIndex);
				whoContactItems.add(new ContactItem(displayName, phoneNumber, false));
			}
			cursorPhones.close();						
														
		} // while (cursorContacts.moveToNext())
		cursorContacts.close();		
	}
}