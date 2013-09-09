package com.appkey.example;

import com.appkey.sdk.AppKeyChecker;
import com.appkey.sdk.AppKeyCheckerCallback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

/**
 * MainActivity of the AppKey Example project
 * 
 * The purpose of this project is to provide an AppKey integration example.  This simple activity
 * simulates the hub activity of your application.  The AppKey check is patterned after the Google
 * License Verification Library (LVL), but less complicated and thus simpler to implement.
 *  
 * @author jimvitek
 */
public class MainActivity extends Activity { 

    private TextView mTextViewAppKeyCheckResult;
    private ImageView mImageViewKeylock;
    private Button mButtonWizard;
    private int mDontAllowReason=0;
    private AppKeyChecker mAppKeyChecker;

    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 

        mImageViewKeylock=(ImageView)findViewById(R.id.imageViewKeylock);
        mTextViewAppKeyCheckResult=(TextView)findViewById(R.id.textViewAppKeyCheckResult);
        mButtonWizard=(Button)findViewById(R.id.buttonWizard);
        mButtonWizard.setOnClickListener(ButtonWizardListener);

        /* AppKeyChecker - Check whether or not AppKey is enabled
         * 
         * Patterned after Google's LVL - http://developer.android.com/google/play/licensing/adding-licensing.html#impl-lc
         * 
	     * @param context a Context
	     * @param appID AppKey.com assigned AppID of the calling application
	     * @param analyticsEnabled if true, and if the calling app has INTERNET permission, the SDK will send user interactions
	     *        events along the installation funnel for the purpose of measuring & optimizing conversion. Events are tracked 
	     *        via Google Analytics using an anonymous UDID.
        */
       mAppKeyChecker = new AppKeyChecker(MainActivity.this, "7", true);  //TODO: Replace 7 with your appId from AppKey.com
    }
    
    @Override
    protected void onResume() {  
        super.onResume();
        mTextViewAppKeyCheckResult.setText("Check in progress");
        mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_gray);

        /* 
         * AppKey check from .onResume() to ensure it is performed each time the app is used.
         * The checker will automatically discard excess calls, so don't worry if it is called 
         * several times per session.
         */
        mAppKeyChecker.checkAccess(new AppKeyCallback()); 
    }

    class AppKeyCallback implements AppKeyCheckerCallback {

        @Override
        public void allow() { 
            /*
             * Insert code to enable premium functionality below
             */
            mTextViewAppKeyCheckResult.setText("App Unlocked");
            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_green);
            mButtonWizard.setEnabled(false);
        }

        @Override
        public void dontAllow(int reason) { 
            /*
            * Insert code to disable premium functionality below
            */
            mTextViewAppKeyCheckResult.setText("App Locked");
            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_red);
            mButtonWizard.setEnabled(true);
            mDontAllowReason=reason;  //Save the reason for the call to promptUser
        }
    }
    
    View.OnClickListener ButtonWizardListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

		    /**
		     * Prompt the user to install AppKey
		     * 
		     * @param activity an Activity
		     * @param reason AppKeyCheckerCallback reason code used to personalize message for current state
		     * @param benefit Description of what will be unlocked for AppKey users
		     */
			mAppKeyChecker.promptUser(MainActivity.this, mDontAllowReason, "[Awesome features]");
		}
	};
}
