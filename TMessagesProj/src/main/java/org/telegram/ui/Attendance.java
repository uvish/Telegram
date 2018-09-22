package org.telegram.ui;import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.StyleableRes;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.ui.Attendance.Browser;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;

public class Attendance extends Activity {
    String rollno,password;
    WebView web_view;
LinearLayout layout;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       web_view =new WebView(Attendance.this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setTheme(R.style.Theme_TMessages);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                setTaskDescription(new ActivityManager.TaskDescription(null, null, Theme.getColor(Theme.key_actionBarDefault) | 0xff000000));
            } catch (Exception e) {

            }
        }
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }


        layout=new LinearLayout(Attendance.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));





        AlertDialog.Builder ad=new AlertDialog.Builder(Attendance.this);
        ad.setTitle("Login");
       // ad.setIcon(R.drawable.ic_login);
        ad.setNeutralButton("CANCEL", null);
        ad.setNegativeButton("More", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

       LinearLayout login=new LinearLayout(Attendance.this);
        login.setOrientation(LinearLayout.VERTICAL);
        EditText roll=new EditText(Attendance.this);
        roll.setHint("Roll no.");
        roll.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);

        EditText pwd=new EditText(Attendance.this);
        pwd.setHint("Password");
        pwd.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);

        login.addView(roll);
        login.addView(pwd);

        ad.setView(login);
        ad.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if(roll.getText().toString().length()==15){

                  loadPage(roll.getText().toString(),pwd.getText().toString());
                     }
            else {
                   StyleableToast.makeText(Attendance.this,"Check Roll Number",Toast.LENGTH_LONG,R.style.myToast).show();}
        }

   });


        AlertDialog add=ad.create();
        add.show();



        setContentView(layout)  ;







    }

    public class Browser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView web, String url) {


            web.evaluateJavascript(
                    "document.getElementById('userid').value='"+rollno+"';"
                            +
                    "document.getElementById('pwd').value='"+password+"';" +
                            "document.getElementsByClassName('ps-button')[0].click();"
                    ,null);
            web.evaluateJavascript("var iframe = document.getElementById('ptifrmtgtframe');var doc = iframe.contentDocument? iframe.contentDocument: iframe.contentWindow.document;doc.forms['STDNT_ATTEND_TERM'].elements['CLASS_ATND_SRCH_CAMPUS_ID'].value='"+rollno+"';doc.forms['STDNT_ATTEND_TERM'].elements['#ICSearch'].click();",null);
        }
    }

    void loadPage(String roll, String passwd)
    {
        rollno=roll;
        password=passwd;
        web_view.setWebViewClient(new Browser());
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setUseWideViewPort(true);
        web_view.getSettings().setBuiltInZoomControls(true);


        web_view.loadUrl("http://115.112.99.254:37520/psp/ps/EMPLOYEE/SA/c/MANAGE_ACADEMIC_RECORDS.STDNT_ATTEND_TERM.GBL");

        web_view.getSettings().setJavaScriptEnabled(true);
        layout.addView(web_view);
        ViewTreeObserver viewTreeObserver  = web_view.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = web_view.getMeasuredHeight();
                if( height != 0 ){
                    web_view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });


    }


}
