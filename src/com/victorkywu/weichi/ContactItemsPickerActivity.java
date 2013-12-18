package com.victorkywu.weichi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

public class ContactItemsPickerActivity extends Activity {

	private ArrayList<ContactItem> whoContactItems;
	private ContactItemsListApdapter contactItemsListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.v("vwu", "ContactItemsPickerActivity - onCreate");		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_items_picker);

		whoContactItems = readContacts();		
		Intent data = getIntent();
		if (data.hasExtra("whoIndices")) {
			ArrayList<Integer> whoIndices = data.getIntegerArrayListExtra("whoIndices");
			for (Integer whoIndex : whoIndices) {
				whoContactItems.get(whoIndex).isPicked = true;
			}
		}
				
		contactItemsListAdapter = new ContactItemsListApdapter(this, whoContactItems);
		ListView listViewContacts = (ListView) findViewById(R.id.listView_contacts);
		listViewContacts.setAdapter(contactItemsListAdapter);					
	}
	
	@Override
	protected void onResume() {
		
		Log.v("vwu", "ContactItemsPickerActivity - onResume");
		super.onResume();
	}
		
	@Override
	protected void onPause() {
		
		Log.v("vwu", "ContactItemsPickerActivity - onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		
		Log.v("vwu", "ContactItemsPickerActivity - onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		
		Log.v("vwu", "ContactItemsPickerActivity - onDestroy");
		super.onDestroy();
	}

	@Override
	public void finish() {

		Log.v("vwu", "ContactItemsPickerActivity - finish");
		ArrayList<String> pickedDisplayNames = new ArrayList<String>();
		ArrayList<String> pickedPhoneNumbers = new ArrayList<String>();
		ArrayList<Integer> pickedIndices = new ArrayList<Integer>();
		for (int index = 0; index < whoContactItems.size(); index++) {
			ContactItem contactItem = whoContactItems.get(index);
			if (contactItem.isPicked) {
				pickedDisplayNames.add(contactItem.displayName);
				pickedPhoneNumbers.add(contactItem.phoneNumber);
				pickedIndices.add(index);
			}
		}
										
		Intent data = new Intent();		
		data.putStringArrayListExtra("pickedDisplayNames", pickedDisplayNames);
		data.putStringArrayListExtra("pickedPhoneNumbers", pickedPhoneNumbers);
		data.putIntegerArrayListExtra("pickedIndices", pickedIndices);
		data.putStringArrayListExtra("pickedPhoneNumbers", pickedPhoneNumbers);
		setResult(Activity.RESULT_OK, data);
		super.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private ArrayList<ContactItem> readContacts() {
		ArrayList<ContactItem> contactItems = new ArrayList<ContactItem>();

		ContentResolver contentResolver = getContentResolver();

		Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
		String[] contactsProjection = new String[] {
			BaseColumns._ID,
			ContactsContract.Contacts.DISPLAY_NAME, 
		};
		String contactsSelection = 
			ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'" + " AND " + 
			ContactsContract.Contacts.IN_VISIBLE_GROUP + "='1'";
		String contactsSortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
		Cursor cursorContacts = contentResolver.query(contactsUri, contactsProjection, contactsSelection, null, contactsSortOrder);
		int displayNameColumnIndex = cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		int contactIdColumnIndex = cursorContacts.getColumnIndex(BaseColumns._ID);

		while (cursorContacts.moveToNext()) {

			String displayName = cursorContacts.getString(displayNameColumnIndex);
			String contactId = cursorContacts.getString(contactIdColumnIndex);

			Uri phonesUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String[] phonesProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER };
			String phonesSelection = 
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId + " AND " + 
				ContactsContract.CommonDataKinds.Phone.TYPE + " = " + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
			Cursor cursorPhones = contentResolver.query(phonesUri, phonesProjection, phonesSelection, null, null);
			int numberColumnIndex = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

			while (cursorPhones.moveToNext()) {

				String phoneNumber = cursorPhones.getString(numberColumnIndex);
				contactItems.add(new ContactItem(displayName, phoneNumber, false));
			}
			cursorPhones.close();

		} // while (cursorContacts.moveToNext())
		cursorContacts.close();
		return contactItems;
	}
}