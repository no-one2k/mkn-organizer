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
        adapter = new SqlTasksAdapter(this);
        setListAdapter(adapter);
    }

    public void runEditActivity(int position) {
        setSelection(position);
        Task item = adapter.getItem(position);
        Toast.makeText(getApplicationContext(), item + " выбран_", Toast.LENGTH_SHORT).show();
        Long id=item.getId();

        Intent intent = new Intent(getApplicationContext(), CreateActivity.class);
        intent.putExtra("EDITING", id.intValue());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        runEditActivity(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.onDestroy();
    }
    
    
    
    
    
    
}
