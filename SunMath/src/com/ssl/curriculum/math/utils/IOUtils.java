package com.ssl.curriculum.math.utils;

import java.io.*;

public class IOUtils {
    private static final int BUFFER_SIZE = 4096;

   
    /**
     * transfer all data from in to out, without closing them.
     * progress notification will be sent during copying
     * @param in
     * @param out
     * @throws IOException
     */
    public static long transfer(InputStream in, OutputStream out, ProgressUpdater listener) throws IOException{
		byte[] buf = new byte[BUFFER_SIZE];
		int len = in.read(buf);
		long all = 0;
		while (len>0) {
			out.write(buf,0,len);
			all += len;
			listener.onProgressUpdate(all);
			len = in.read(buf);
		}
		out.flush();
		return all;
	}
    
    /**
     * transfer all data from in to out, without closing them.
     * @param in
     * @param out
     * @throws IOException
     */
    public static long transfer(InputStream in, OutputStream out) throws IOException{
		byte[] buf = new byte[BUFFER_SIZE];
		int len = in.read(buf);
		long all = 0;
		while (len>0) {
			out.write(buf,0,len);
			all += len;
			len = in.read(buf);
		}
		out.flush();
		return all;
	}
    
    /**
     * transfer all data from in to out, then close them.
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, null);
    }

    public static void copy(InputStream input, OutputStream output, ProgressUpdater updater) throws IOException{
        if(input == null ) {
            throw new NullPointerException("Null Input");
        } else if(output == null) {
            throw new NullPointerException("Null Output");
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

    public static String readString(InputStream input) throws IOException {
        if(input == null ) {
            throw new IOException("Null Input");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder builder = new StringBuilder();

        IOException exception = null;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            exception = e;
        } finally {
            closeSafely(input);
            if (exception != null) {
                throw exception;
            }
        }

        return builder.toString();
    }

    private static void closeSafely(InputStream input) {
        try {
            input.close();
        } catch (IOException closeException) {}
    }

    private static void closeSafely(OutputStream output) {
        try {
            output.close();
        } catch (IOException closeException) {}
    }

    public static abstract class ProgressUpdater {
        public abstract void onProgressUpdate(long totalBytesProcessed);
    }
}
