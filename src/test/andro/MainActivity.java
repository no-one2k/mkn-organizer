package test.andro;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity
{
    public static final String FERTERTERTE = "ferterterte";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listView = (ListView)this.findViewById(R.id.listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter (this.getApplicationContext(),android.R.layout.simple_list_item_1);
        arrayAdapter.add("fghgfhfg");
        arrayAdapter.add("fghfghfgh");
        arrayAdapter.add(FERTERTERTE);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String item = (String) arg0.getAdapter().getItem(arg2);
                Toast.makeText(getApplicationContext(), item + " выбран_", Toast.LENGTH_SHORT).show();
                if (FERTERTERTE.equalsIgnoreCase(item)){
                    startActivity(new Intent(getApplicationContext(), CreateActivity.class));
                }
            }
        });
        
    }
}
