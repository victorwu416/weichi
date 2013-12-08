package com.victorkywu.weichi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Activity thisActivity = this;
	
	private static final int PICK_CONTACT_REQUEST_CODE = 0; 
					
	private String whoPhoneNumber = null;
	
	private ArrayList<String> whoDisplayNames;
	private ArrayList<String> whoPhoneNumbers;
	
	private ArrayList<ContactItem> whoContactItems;
	
	private String[] mealValues;
	private String[] whenValues;
	private String[] mealPhrases;		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		Log.v("vwu", "oncreate");
		
		mealValues = getResources().getStringArray(R.array.array_meal_values);
		whenValues = getResources().getStringArray(R.array.array_when_values);
		mealPhrases = getResources().getStringArray(R.array.array_meal_phrases);							
		
		setupEditTextWho(); 
		setupEditTextMulti();
		setupSpinnerMeal();
		setupSpinnerWhen();
		setupEditTextWhere();		
		setupButtonSendMessage();
						
		updateMessage();
		//readContacts();
	}
		
	private void setupEditTextWho() {
		final EditText editTextWho = (EditText) findViewById(R.id.editText_who);			
		editTextWho.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View view) {				
				Intent intentPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);								
				intentPickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
				startActivityForResult(intentPickContact, PICK_CONTACT_REQUEST_CODE);
			}
		});
	}

	private void setupEditTextMulti() {
		final EditText editTextMutli = (EditText) findViewById(R.id.editText_multi);
		
		Log.v("vwu", "clicked into multi.");				
				
		editTextMutli.setOnClickListener(new EditText.OnClickListener() {
			public void onClick(View view) {																
				Intent intentContactsPicker = new Intent(thisActivity, ContactItemsPickerActivity.class);								
				startActivity(intentContactsPicker);							
			}
		});
	}	
	
	private void setupSpinnerMeal() {
		Spinner spinnerMeal = (Spinner) findViewById(R.id.spinner_meal);
				
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
		            this, R.array.array_meal_values, R.layout.layout_spinner);
		spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner);
		spinnerMeal.setAdapter(spinnerAdapter);
		
		spinnerMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateMessage();
			}
			public void onNothingSelected (AdapterView<?> parent) {	}
		});
	}
	
	private void setupSpinnerWhen() {
		Spinner spinnerWhen = (Spinner) findViewById(R.id.spinner_when);
		
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
	            this, R.array.array_when_values, R.layout.layout_spinner);
		spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner);
		spinnerWhen.setAdapter(spinnerAdapter);
		
		spinnerWhen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateMessage();
			}
			public void onNothingSelected (AdapterView<?> parent) {	}
		});
	}
	
	// Update the message as the text is changed. Seems to be inefficient. Potentially this should be fixed.
	private void setupEditTextWhere() {
		EditText editTextWhere = (EditText) findViewById(R.id.editText_where);
		editTextWhere.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }
			public void afterTextChanged(Editable editable) {
				updateMessage();
			}
		});
	}	
	
	private void setupButtonSendMessage() {
		final Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
		buttonSendMessage.setEnabled(false);				
		buttonSendMessage.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View view) {	
				
				if (whoPhoneNumber == null) {
					Toast.makeText(getApplicationContext(), getString(R.string.pick_a_send_contact), Toast.LENGTH_SHORT).show();
					Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
					buttonSendMessage.setEnabled(false);
					return;
				}
				
				TextView textViewMessage = (TextView) findViewById(R.id.textView_message);
				String updatedMessage = textViewMessage.getText().toString();
								
//		        String uriSend= "smsto:"+ "12345;67890";
		        String uriSend= "smsto:" + whoPhoneNumber;
		        Intent intentSendMessage = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriSend));
		        intentSendMessage.putExtra("sms_body", updatedMessage);
		        intentSendMessage.putExtra("compose_mode", true);		      		        
		        startActivity(intentSendMessage);
		        
//				String separator = "; ";						
//				  if(android.os.Build.MANUFACTURER.toLowerCase().contains("samsung")){
//				    separator = ", ";
//				  }
				  // set the numbers string with the use of 'separator'
			}
		});
		
	}
	
	private void updateMessage() {							
		Spinner spinnerMeal = (Spinner) findViewById(R.id.spinner_meal);
		int selectedMealId = (int) spinnerMeal.getSelectedItemId();								
		
		Spinner spinnerWhen = (Spinner) findViewById(R.id.spinner_when);
		int selectedWhenId = (int) spinnerWhen.getSelectedItemId();								
										
		int mealPhrasesId = (selectedMealId * whenValues.length) + selectedWhenId;
		EditText editTextWhere = (EditText) findViewById(R.id.editText_where);
		String updatedMessage;
		if (editTextWhere.getText().length() == 0) {
			updatedMessage = mealPhrases[mealPhrasesId];
		} else {
			updatedMessage = mealPhrases[mealPhrasesId] + " " + getResources().getString(R.string.where_phrase, editTextWhere.getText());
		}

		TextView textViewMessage = (TextView) findViewById(R.id.textView_message);
		textViewMessage.setText(updatedMessage);									
	}
	
	private void readContacts() {
		whoDisplayNames = new ArrayList<String>();
		whoPhoneNumbers = new ArrayList<String>();
		whoContactItems = new ArrayList<ContactItem>();
		
		Cursor cursorContacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"); 
		while (cursorContacts.moveToNext()) {
			String contactId = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts._ID));
			
			//Log.v("vwu", "contact Id is " + contactId);
			
			String hasPhoneNumber = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			
			// Get contacts only of those that have phone number(s).
			if ( hasPhoneNumber.equals("1") || Boolean.parseBoolean(hasPhoneNumber) ) {
				String displayName = cursorContacts.getString(cursorContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				
				// Log.v("vwu", "displayName is " + displayName);
				
				// Get phone numbers of this contact, that are mobile phone numbers.
				Cursor cursorPhoneNumbers = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null); 
				
				while (cursorPhoneNumbers.moveToNext()) {							
					int phoneNumberType = cursorPhoneNumbers.getInt(cursorPhoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					boolean isMobile = ( (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) || 
								         (phoneNumberType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE) );
					if (isMobile) {
						String phoneNumber = cursorPhoneNumbers.getString(cursorPhoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						//Log.v("vwu", "displayName | phoneNumber = " + displayName + " | " + phoneNumber);						
						whoDisplayNames.add(displayName);
						whoPhoneNumbers.add(phoneNumber);
						
						whoContactItems.add(new ContactItem(displayName, phoneNumber, false));
					}									          
				}
				cursorPhoneNumbers.close();						
			}														
		} // while (cursorContacts.moveToNext())
		cursorContacts.close();		
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == PICK_CONTACT_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
                Uri pickedContact = intent.getData();               
                                
                Cursor cursor = getContentResolver().query(pickedContact, null, null, null, null);
                cursor.moveToFirst();

                int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                
                String pickedPhoneNumber = "";
                String pickedDisplayName = "";                
                pickedPhoneNumber = cursor.getString(phoneNumberIndex);
                pickedDisplayName = cursor.getString(displayNameIndex);
                
                final EditText editTextWho = (EditText) findViewById(R.id.editText_who);
                editTextWho.setText(getString(R.string.text_contact, pickedDisplayName, pickedPhoneNumber));
                
                whoPhoneNumber = pickedPhoneNumber; 
                
                Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
                buttonSendMessage.setEnabled(true);
            }
        }		
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/

}
