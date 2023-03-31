package ua.edu.sumdu.asyncpageload;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class ParseAddress extends AsyncTaskLoader<String> {
    private final String mAddress;

    public ParseAddress(@NonNull Context context, String address) {
        super(context);
        mAddress = address;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mAddress);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
