package com.ssl.metadata.database.observers;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import com.ssl.metadata.database.tables.ProblemChoiceTable;
import com.ssl.metadata.provider.Matcher;
import com.ssl.metadata.provider.MetadataContract;
import com.ssl.support.api.record.UserRecordRequest;
import com.ssl.support.downloader.DownloadTaskParams;
import com.ssl.support.services.DownloadService;
import com.ssl.support.services.UserRecordPostService;

public class UserRecordUpdateObserver extends TableObserver {

    private UriMatcher sUriMatcher = Matcher.Factory.getMatcher();
    private Context context;

    public UserRecordUpdateObserver(Context context) {
        this.context = context;
    }

    @Override
    public void postUpdate(Uri uri, ContentValues values, String selection, String[] selectionArgs, int result) {
        if (result == 1) {
            switch(sUriMatcher.match(uri)) {
                case Matcher.ACTIVITIES_ID:
                    if (values.containsKey(MetadataContract.Activities._RESULT)) {
                        postUserRecordUpdate("Activity", uri.getLastPathSegment(), values);
                    }
                    break;
                case Matcher.PROBLEMS_ID:
                    if (values.containsKey(MetadataContract.Problems._USER_ANSWER)) {
                        postUserRecordUpdate("Problem", uri.getLastPathSegment(), values);
                    }
                    break;
                case Matcher.BOOKS_ID:
                    if (values.containsKey(MetadataContract.Books._PROGRESS)) {
                        postUserRecordUpdate("Book", uri.getLastPathSegment(), values);
                    }
                    break;
                case Matcher.SECTIONS_ID:
                    if (values.containsKey(MetadataContract.Sections._STATUS)) {
                        postUserRecordUpdate("Section", uri.getLastPathSegment(), values);
                    }
                    break;
                default:
            }
        }
    }

    private void postUserRecordUpdate(String itemType, String itemId, ContentValues values) {
        int id = Integer.parseInt(itemId);
        if (id > 0) {
            UserRecordRequest.Builder builder = new UserRecordRequest.Builder(itemType, id);
            if (itemType.equals("Activity")) {
                builder.putParam("status", values.get(MetadataContract.Activities._STATUS));
                builder.putParam("result", values.get(MetadataContract.Activities._RESULT));
            } else if (itemType.equals("Problem")) {
                builder.putParam("answer", values.get(MetadataContract.Problems._USER_ANSWER));
                boolean is_correct = values.getAsBoolean(MetadataContract.Problems._IS_CORRECT);
                builder.putParam("is_correct", is_correct);
            } else if (itemType.equals("Book")) {
                int percent = values.getAsInteger(MetadataContract.Books._PROGRESS);
                builder.putParam("percent", percent);
                builder.putParam("status", (percent == 100) ? "done" : "in_progress");
            } else if (itemType.equals("Section")) {

            } else {
                return;
            }
            postUserRecordUpdateRequest(builder.build());
        }
    }

    private void postUserRecordUpdateRequest(UserRecordRequest request) {
        Intent intent = new Intent(context, UserRecordPostService.class);
        intent.putExtra(UserRecordPostService.USER_RECORD_REQUEST_KEY, request);
        context.startService(intent);
    }
}
