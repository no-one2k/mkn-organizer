package test.andro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 *
 * @author noone
 */
public class DiagramActivity extends Activity implements RunInterface{
    private RunInterface runInterfac;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        runInterfac=new RunInterfaceImpl(this);
        // ToDo add your GUI initialization code here        
    }
    
     public void runListActivity(View v) {
        runInterfac.runListActivity(v);
    }

    public void runDiagramActivity(View v) {
        runInterfac.runDiagramActivity(v);
    }

    public void runCalendarActivity(View v) {
        runInterfac.runCalendarActivity(v);
    }
}
