package com.ssl.support.utils;

import java.io.*;

public class IOUtils {
    private static final int BUFFER_SIZE = 4096;

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copyByteStream(input, output, null);
    }

    public static void copyByteStream(InputStream input, OutputStream output, ProgressUpdater updater) throws IOException{
        if(input == null ) {
            throw new IOException("Null Input");
        } else if(output == null) {
            throw new IOException("Null Output");
        } else {
            IOException exception = null;
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int count, offset = 0;
                long total = 0;

                while ((count = input.read(buffer, offset, buffer.length - offset)) > 0) {
                    output.write(buffer, offset, count);

                    total += count;
                    offset = (offset + count) % buffer.length;
                    if (updater != null) {
                        updater.onProgressUpdate(total);
                    }
                }
            } catch (IOException e) {
                exception = e;
            } finally {
                closeSafely(input);
                closeSafely(output);
                if (exception != null) {
                    throw exception;
                }
            }
        }
    }

    public static void copyCharacterStream(Reader reader, Writer writer) throws IOException{
        if(reader == null ) {
            throw new IOException("Null reader");
        } else if(writer == null) {
            throw new IOException("Null writer");
        } else {
            IOException exception = null;
            try {
                char[] buffer = new char[BUFFER_SIZE];
                int count, offset = 0;
                long total = 0;

                while ((count = reader.read(buffer, offset, buffer.length - offset)) > 0) {
                    writer.write(buffer, offset, count);

                    total += count;
                    offset = (offset + count) % buffer.length;
                }
            } catch (IOException e) {
                exception = e;
            } finally {
                closeSafely(reader);
                closeSafely(writer);
                if (exception != null) {
                    throw exception;
                }
            }
        }
    }

    public static void closeSafely(InputStream input) {
        try {
            input.close();
        } catch (IOException closeException) {}
    }

    public static void closeSafely(OutputStream output) {
        try {
            output.close();
        } catch (IOException closeException) {}
    }

    public static void closeSafely(Reader reader) {
        try {
            reader.close();
        } catch (IOException closeException) {}
    }

    public static void closeSafely(Writer writer) {
        try {
            writer.close();
        } catch (IOException closeException) {}
    }

    public static abstract class ProgressUpdater {
        public abstract void onProgressUpdate(long totalBytesProcessed);
    }
}
