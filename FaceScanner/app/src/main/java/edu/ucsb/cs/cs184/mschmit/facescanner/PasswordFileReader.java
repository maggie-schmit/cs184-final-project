package edu.ucsb.cs.cs184.mschmit.facescanner;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//taken from https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android with some edits
public class PasswordFileReader {
    static final String filename="password_hash.txt";
    public static void writeFile(Context context,String hash) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            osw.write(hash);
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFile(Context context) {
        String hash = "";
        try {
            InputStream is = context.openFileInput(filename);
            if (is!=null) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String receiveString = "";
                StringBuilder sb = new StringBuilder();
                while ( (receiveString = br.readLine()) != null ) {
                    sb.append(receiveString);
                }
                is.close();
                hash = sb.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }
}
