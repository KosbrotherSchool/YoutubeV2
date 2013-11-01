package com.kosbrother.youtubev2;


import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.services.youtube.YouTubeScopes;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;


public class MainActivity extends Activity {
	
	private String[] mNamesArray;
    private String mEmail;
	
    private AccountManager mAccountManager;
    private static final String SCOPE = "oauth2:" + YouTubeScopes.YOUTUBE;
    
    private static final String TAG = "MainActivity";
    
    static final int REQUEST_CODE_RECOVER_FROM_AUTH_ERROR = 1001;
    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNamesArray = getAccountNames();
		mEmail = mNamesArray[0];
		
		new GetSuggestChannelsTask(MainActivity.this, mEmail, SCOPE).execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private String[] getAccountNames() {
        mAccountManager = AccountManager.get(this);
        Account[] accounts = mAccountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = accounts[i].name;
        }
        return names;
    }
	
	public void showErrorDialog(final int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Dialog d = GooglePlayServicesUtil.getErrorDialog(
                  code,
                  MainActivity.this,
                  REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
              d.show();
            }
        });
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RECOVER_FROM_AUTH_ERROR) {
            handleAuthorizeResult(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
	
	private void handleAuthorizeResult(int resultCode, Intent data) {
        if (data == null) {
//            show("Unknown error, click the button again");
            return;
        }
        if (resultCode == RESULT_OK) {
            Log.i(TAG, "Retrying");
//            getTask(this, mEmail, SCOPE, REQUEST_CODE_RECOVER_FROM_AUTH_ERROR).execute();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
//            show("User rejected authorization.");
            return;
        }
//        show("Unknown error, click the button again");
    }
	
}
