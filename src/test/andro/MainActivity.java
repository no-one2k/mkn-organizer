package test.andro;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    public static final String FERTERTERTE = "ferterterte";
    private SqlTasksAdapter adapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ListView listView = getListView();//(ListView) this.findViewById(R.id.listview);
        adapter = new SqlTasksAdapter(this);
        setListAdapter(adapter);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                runEditActivity(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                runEditActivity(arg2);
            }
        });
        //listView.setAdapter(adapter);
//        listView.setClickable(true);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                runEditActivity(arg2);
//            }
//
//           
//        });
//        
    }

    private void runEditActivity(int position) {
        Task item = adapter.getItem(position);
        Toast.makeText(getApplicationContext(), item + " выбран_", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("EDITING", item.getId());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        runEditActivity(position);
    }
}
