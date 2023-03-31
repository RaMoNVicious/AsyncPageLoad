package ua.edu.sumdu.asyncpageload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner mProtocol;


    private EditText mTxtUrl;

    private EditText mTxtPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProtocol = findViewById(R.id.protocol);
        mTxtUrl = findViewById(R.id.txt_url);
        mTxtPage = findViewById(R.id.txt_page);
    }

    public void onLoadClicked(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(
                        view.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );

        if (!isConnected()) {
            Toast.makeText(
                    this,
                    getString(R.string.connection_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String queryUrl = mProtocol.getSelectedItem().toString() + mTxtUrl.getText().toString();

        if (!URLUtil.isValidUrl(queryUrl)) {
            Toast.makeText(
                    this,
                    getString(R.string.address_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("queryString", queryUrl);
        getSupportLoaderManager().restartLoader(0, bundle, new LoaderManager.LoaderCallbacks() {
            @NonNull
            @Override
            public Loader onCreateLoader(int id, @Nullable Bundle args) {
                String queryString = "";

                if (args != null) {
                    queryString = args.getString("queryString");
                }

                return new ParseAddress(getApplicationContext(), queryString);
            }

            @Override
            public void onLoadFinished(@NonNull Loader loader, Object data) {
                mTxtPage.setText((String) data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader loader) {

            }
        });
    }

    private Boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr != null) {
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}