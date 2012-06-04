package test.andro;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 *
 * @author noone
 */
public class RunInterfaceImpl implements RunInterface{
    
    private Activity activit;

    public RunInterfaceImpl(Activity activit) {
        this.activit = activit;
    }
    
    

    public void runCalendarActivity(View v) {
        runActivity (SimpleCalendarViewActivity.class);
    }

    public void runDiagramActivity(View v) {
        runActivity (DiagramActivity.class);
    }

    public void runListActivity(View v) {
        runActivity (MainActivity.class);
    }

    private void runActivity(Class aClass) {
        if (!aClass.equals(activit.getClass())){
        Intent intent = new Intent(activit, aClass);
        
        activit.startActivityIfNeeded(intent, 0);
        }
    }
    
}
