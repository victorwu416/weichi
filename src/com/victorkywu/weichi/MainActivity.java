package com.victorkywu.weichi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Activity thisActivity = this;
		 
	private static final int PICK_CONTACTS_REQUEST_CODE = 0;
						
	private ArrayList<String> whoDisplayNames;
	private ArrayList<String> whoPhoneNumbers;
	private ArrayList<Integer> whoIndices;
		
	private String[] mealValues;
	private String[] whenValues;
	private String[] mealPhrases;		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.v("vwu", "MainActivity - onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);			
		
		mealValues = getResources().getStringArray(R.array.array_meal_values);
		whenValues = getResources().getStringArray(R.array.array_when_values);
		mealPhrases = getResources().getStringArray(R.array.array_meal_phrases);							
		
		setupEditTextWho(); 		
		setupSpinnerMeal();
		setupSpinnerWhen();
		setupEditTextWhere();
		setupSpinnerCurrency();
		setupSeekBarCost();
		setupButtonSendMessage();
						
		updateMessage();
	}
		
	@Override
	protected void onResume() {
		
		Log.v("vwu", "MainActivity - onResume");
		super.onResume();
	}
		
	@Override
	protected void onPause() {
		
		Log.v("vwu", "MainActivity - onPause");
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		
		Log.v("vwu", "MainActivity - onStop");
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		
		Log.v("vwu", "MainActivity - onDestroy");
		super.onDestroy();
	}
		
	private void setupEditTextWho() {
		final EditText editTextWho = (EditText) findViewById(R.id.editText_who);		
		editTextWho.setOnClickListener(new EditText.OnClickListener() {
			@Override
			public void onClick(View view) {	
				
				Intent intentContactsPicker = new Intent(thisActivity, ContactItemsPickerActivity.class);
				if ((whoIndices != null) && (whoIndices.size() != 0)) {
					intentContactsPicker.putIntegerArrayListExtra("whoIndices", whoIndices);
				}
				startActivityForResult(intentContactsPicker, PICK_CONTACTS_REQUEST_CODE);							
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
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateMessage();
			}
			@Override
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
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateMessage();
			}
			@Override
			public void onNothingSelected (AdapterView<?> parent) {	}
		});
	}
	
	private void setupEditTextWhere() {
		EditText editTextWhere = (EditText) findViewById(R.id.editText_where);
		editTextWhere.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }
			@Override
			public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }
			@Override
			public void afterTextChanged(Editable editable) {
				updateMessage();
			}
		});
	}	

	private void setupSpinnerCurrency() {
		
		Spinner spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);		
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
	            this, R.array.array_currency_values, R.layout.layout_spinner);
		spinnerAdapter.setDropDownViewResource(R.layout.layout_spinner);
		spinnerCurrency.setAdapter(spinnerAdapter);
		
		spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				updateMessage();
			}
			@Override
			public void onNothingSelected (AdapterView<?> parent) {	}
		});
	}	
	
	private void setupSeekBarCost() {
		
		SeekBar seekBarCost = (SeekBar) findViewById(R.id.seekBar_cost);
		seekBarCost.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
												
				updateMessage();
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
		});
	}
	
	private void setupButtonSendMessage() {
		final Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
		buttonSendMessage.setEnabled(false);				
		buttonSendMessage.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View view) {	
				
				if ( (whoPhoneNumbers == null) || (whoPhoneNumbers.size() == 0) ) {
					Toast.makeText(getApplicationContext(), getString(R.string.pick_contacts), Toast.LENGTH_SHORT).show();
					Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
					buttonSendMessage.setEnabled(false);
					return;
				}
				
				TextView textViewMessage = (TextView) findViewById(R.id.textView_message);
				String updatedMessage = textViewMessage.getText().toString();
		        
		        // Set the phone number separator as a semicolon. 
		        // If Samsung, use a comma: http://stackoverflow.com/questions/9721714/android-passing-multiple-numbers-to-sms-intent
		        String phoneNumberSeparator = "; ";						
		        if(android.os.Build.MANUFACTURER.toLowerCase().contains("samsung")){ 
		        	phoneNumberSeparator = ", ";
		        }
		        String uriSend= "smsto:";
		        for (String whoPhoneNumber : whoPhoneNumbers) {
		        	uriSend = uriSend + whoPhoneNumber + phoneNumberSeparator;
		        }		        
		        //uriSend= "smsto:12345; 67890";
		        
		        Log.v("vwu", "uriSend is:" + uriSend);
		        Log.v("vwu", "updatedMessage is:" + updatedMessage);
		        
		        Intent intentSendMessage = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriSend));
		        intentSendMessage.putExtra("sms_body", updatedMessage);
		        intentSendMessage.putExtra("compose_mode", true);		      		        
		        startActivity(intentSendMessage);
			}
		});		
	}
	
	private void updateMessage() {							
		Spinner spinnerMeal = (Spinner) findViewById(R.id.spinner_meal);
		int selectedMealId = (int) spinnerMeal.getSelectedItemId();								
		
		Spinner spinnerWhen = (Spinner) findViewById(R.id.spinner_when);
		int selectedWhenId = (int) spinnerWhen.getSelectedItemId();																		
		int mealPhrasesId = (selectedMealId * whenValues.length) + selectedWhenId;			
		String mealPhrase = mealPhrases[mealPhrasesId];
		
		EditText editTextWhere = (EditText) findViewById(R.id.editText_where);
		String wherePhrase = "";
		if (editTextWhere.getText().length() != 0) {
			wherePhrase = getResources().getString(R.string.where_phrase, editTextWhere.getText());
		}
		
		
		Spinner spinnerCurrency = (Spinner) findViewById(R.id.spinner_currency);
		String currency = (String) spinnerCurrency.getSelectedItem();		
		SeekBar seekBarCost = (SeekBar) findViewById(R.id.seekBar_cost);						
		String[] costPhrases = {};				
		if (currency.equals("¥")) {			
			costPhrases = getResources().getStringArray(R.array.array_rmb_cost_phrases);						
		} else if (currency.equals("$")) {
			costPhrases = getResources().getStringArray(R.array.array_dollar_cost_phrases);
		} 
		String costPhrase = "";
		if (costPhrases.length > 0) {	
			int numberTicks = costPhrases.length;				
			int costPhraseIndex = (int) Math.round((1.0 * seekBarCost.getProgress() / seekBarCost.getMax()) * numberTicks);
			costPhraseIndex = Math.min(costPhraseIndex,  costPhrases.length-1);
			costPhrase = costPhrases[costPhraseIndex];
		}
		
		String updatedMessage = mealPhrase + " " + wherePhrase + " " + costPhrase;
		
		TextView textViewMessage = (TextView) findViewById(R.id.textView_message);
		textViewMessage.setText(updatedMessage);									
	}
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		Log.v("vwu", "MainActivity - onActivityResult");
				
		if (requestCode == PICK_CONTACTS_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				
				Log.v("vwu", "PICK_CONTACTS_REQUEST_CODE - RESULT_OK");
				
				whoDisplayNames = data.getStringArrayListExtra("pickedDisplayNames");
				whoPhoneNumbers = data.getStringArrayListExtra("pickedPhoneNumbers");
				whoIndices = data.getIntegerArrayListExtra("pickedIndices");
												
				for (String whoDisplayName : whoDisplayNames) {
					Log.v("vwu", "whoDisplayName = " + whoDisplayName);
				}
				for (String whoPhoneNumber : whoPhoneNumbers) {
					Log.v("vwu", "whoPhoneNumber = " + whoPhoneNumber);
				}
				for (Integer whoIndex : whoIndices) {
					Log.v("vwu", "whoIndex = " + whoIndex);
				}
							                    
                final EditText editTextWho = (EditText) findViewById(R.id.editText_who);
                editTextWho.setText("");
                int pickedContactsSize = Math.min(whoDisplayNames.size(), whoPhoneNumbers.size());                                               
                String textContact;
                for (int index = 0; index < pickedContactsSize; index++) {
                	textContact = whoDisplayNames.get(index) + ": " + whoPhoneNumbers.get(index);
                	if (editTextWho.getText().toString().equals("")) {
                		editTextWho.setText(textContact);
                	} else {
                		editTextWho.setText(editTextWho.getText() + " | " + textContact);	
                	}                	
                }                
                Button buttonSendMessage = (Button) findViewById(R.id.button_send_message);
                if (pickedContactsSize != 0) {
                	buttonSendMessage.setEnabled(true);
                } else {
                	buttonSendMessage.setEnabled(false);
                }
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
