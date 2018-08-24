package com.amt.latestjokes;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SingleActivity extends AppCompatActivity {

    private WebView webview;
    private SpinKitView progressBar;
    private String data;
    private String shareData;
    private ImageView copy;
    private ImageView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        data = getIntent().getStringExtra("data");

        Document document = Jsoup.parse(data);
        data = document.text();
        shareData = document.text();

        progressBar = findViewById(R.id.spin_kit);
        webview = findViewById(R.id.webview);
        copy = findViewById(R.id.copy);
        share = findViewById(R.id.share);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //webview.getSettings().setSupportZoom(true);
        //webview.getSettings().setBuiltInZoomControls(true);
        //webview.getSettings().setDisplayZoomControls(true);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        webview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webview.setLongClickable(false);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "Text to copy");
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(SingleActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareData);
                startActivity(intent);

            }
        });

        data = data.replace("<p", "<p class=\"text-justify\"");
        data = data.replace("font-size: 14px;", "font-size: 24px;");
        /*data = data.replace("<img class=\"", "<img class=\"img-thumbnail ");
        data = data.replace("width=", "width=\"100%\"");*/
        String head1 = "<head><meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <style>body {font-family: 'verdana';} *{text-align: justify;}</style></head>";
        String text = "<html>" + head1 + "<body style=\"font-family: arial;\"><div class=\"container\"><br>" + data + "</div></body></html>";
        webview.loadDataWithBaseURL("", text, "text/html", "utf-8", "");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact) {
            Intent contact = new Intent(Intent.ACTION_SEND);
            contact.setPackage("com.google.android.gm");
            contact.setData(Uri.parse("email"));
            String[] s = {"immujahidkhan6@gmail.com"};
            contact.putExtra(Intent.EXTRA_EMAIL, s);
            contact.putExtra(Intent.EXTRA_SUBJECT, "Recommend subject Here");
            contact.putExtra(Intent.EXTRA_TEXT, "Recommend Message Here");
            contact.setType("message/rfc822");
            Intent chooser = Intent.createChooser(contact, "Choose Gmail for contact us");
            startActivity(chooser);
        }
        if (id == R.id.rate_us) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }
        if (id == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Latest Jokes  ,Download it from here : https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        return super.onOptionsItemSelected(item);
    }

}