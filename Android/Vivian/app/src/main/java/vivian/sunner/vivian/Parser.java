package vivian.sunner.vivian;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sunner on 2016/7/11.
 */
public class Parser {
    List<String> en = new LinkedList<String>();
    List<String> ch = new LinkedList<String>();
    public static int[] index = new int[4];

    // Load the word from .pair file
    public void load() {
        // Implement later
    }

    // Generate the index of next word
    public void generate() {
        for (int i = 0; i < 4; i++)
            index[i] = (int) (Math.random() * (en.size()) - 1);
    }

    // Give the correct english word
    public String getEn(int i) {
        return en.get(index[i]);
    }

    // Give the correct chinese word
    public String getch(int i) {
        return ch.get(index[i]);
    }

    // Testing Function
    public void testLoad() {
        en.add("apple");
        en.add("bus");
        en.add("car");
        en.add("bird");
        en.add("egg");
        ch.add("蘋果");
        ch.add("公車");
        ch.add("車");
        ch.add("鳥");
        ch.add("雞蛋");
    }

    // Validate if the two string is the same
    public boolean isSame(String str1, String str2) {
        if (str1.length() != str2.length())
            return false;
        else {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) != str2.charAt(i))
                    return false;
            }
            return true;
        }
    }

    public String readFromFile() {
        String ret = "";
        try {
            FileInputStream fis = new FileInputStream(new File("/sdcard/vivian.pair"));

            if (fis != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while (true) {
                    stringBuilder.append(receiveString + "\n");
                    receiveString = bufferedReader.readLine();
                    if (receiveString == null)
                        break;
                    en.add(receiveString);
                    receiveString = bufferedReader.readLine();
                    if (receiveString == null)
                        break;
                    ch.add(receiveString);
                    Log.e("讀檔", receiveString);
                }

                fis.close();
                ret = stringBuilder.toString();
            }
            Log.e("讀檔", "finish");

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        Log.e("讀檔", "finish");
        return ret;
    }
}
