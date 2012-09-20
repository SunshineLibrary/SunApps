package com.ssl.curriculum.math.utils;

import android.database.Cursor;

import java.util.Iterator;

/**
 * @author Bowen Sun
 * @version 1.0
 */

public class IterableCursor implements Iterable<Cursor> {

    private Cursor mCursor;

    public IterableCursor(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public Iterator<Cursor> iterator() {
        return new CursorIterator(mCursor);
    }

    private class CursorIterator implements Iterator<Cursor> {

        private boolean isValid, isFirst;
        private Cursor mCursor;

        public CursorIterator(Cursor cursor) {
            isValid = cursor != null && cursor.moveToFirst();
            if (isValid) {
                mCursor = cursor;
                isFirst = true;
            }
        }

        @Override
        public boolean hasNext() {
            if (!isValid) {
                return false;
            }

            if (!isFirst && mCursor.isLast()) {
                mCursor.close();
                return false;
            }

            return true;
        }

        @Override
        public Cursor next() {
            if (isFirst) {
               isFirst = false;
               return mCursor;
            } else {
               mCursor.moveToNext();
               return mCursor;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
