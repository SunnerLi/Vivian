package vivian.sunner.vivian;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by sunner on 2016/8/8.
 * This class can reorganize the order of the content in the textView
 */
public class RollTextViewHandler {
    List<String> q = new ArrayList<String>();
    final int size = 5;

    // Add the new String
    public void add(String string) {
        if (q.size() == size) {
            List<String> copy = q;
            q = new ArrayList<>();
            for (int i=1; i<size; i++)
                q.add(copy.get(i));
        }
        q.add(string);
    }

    // Return the content of TextView
    public String show() {
        String string = "";
        for (int i = 0; i < q.size(); i++){
            string += q.get(i);
            string += "\n\n";
        }
        return string;
    }
}
