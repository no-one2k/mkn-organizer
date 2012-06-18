package test.andro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;

public class SqlTasksAdapter extends BaseAdapter {

    private static final String DB_NAME = "tasks_db.sqlite3";
    private static final String TABLE_NAME = "tasks";
    private static final int DB_VESION = 9;
    private static final String KEY_ID = "_id";
    private static final int ID_COLUMN = 0;
    private static final String KEY_NAME = "name";
    private static final int NAME_COLUMN = 1;
    private static final String KEY_START = "start";
    private static final int START_COLUMN = 2;
    private static final String KEY_FINISH = "_finish";
    private static final int FINISH_COLUMN = 3;
    private static final String KEY_DURATION = "duration";
    private static final int DURATION_COLUMN = 4;
    private static final String KEY_PRIORITY = "priority";
    private static final int PRIORITY_COLUMN = 5;
    private static final String KEY_COMMENT = "comment";
    private static final int COMMENT_COLUMN = 6;
    private static final String KEY_ENDED = "ended";
    private static final int ENDED_COLUMN = 7;
    
    
    private Cursor cursor;
    private SQLiteDatabase database;
    private DbOpenHelper dbOpenHelper;
    private Context context;
    private Date filterDate=null;
    private boolean showAll = false;
    private boolean sortOnlyByDate = false;
    
    private static SqlTasksAdapter instance=null;

    public static SqlTasksAdapter getInstance(Context context) {
        if (instance==null){
            instance=new SqlTasksAdapter(context);
        }
        if (!instance.database.isOpen()){
            instance.database = instance.dbOpenHelper.getWritableDatabase();
        }
        return instance;
    }
    
    

    private SqlTasksAdapter(Context context) {
        super();
        this.context = context;
        init();
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        refresh();
    }

    public boolean isSortOnlyByDate() {
        return sortOnlyByDate;
    }

    public void setSortOnlyByDate(boolean sortOnlyByDate) {
        this.sortOnlyByDate = sortOnlyByDate;
        refresh();
    }

    public Date getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(Date filterDate) {
        this.filterDate = filterDate;
        refresh();
    }
    
    

    @Override
    public long getItemId(int position) {
        Task taskOnPosition = getItem(position);
        return taskOnPosition.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parrent) {
        View view;
        if (null == convertView) {
            view = View.inflate(context, R.layout.list_item,
                    null);
        } else {
            view = convertView;
        }
        final Task item = getItem(position);
        TextView taskTextView = (TextView) view.findViewById(R.id.taskname_view);
        taskTextView.setText(item.getName());
        int priorColor=R.color.back;
        switch (item.getPriority()) {
            case Important: {
                priorColor=Color.YELLOW;// R.color.yellow;
            }
            break;
            case VeryImportant: {
                priorColor=Color.RED;
            }
            break;
            case NotImportant: {
                priorColor=Color.BLUE;
            }
            break;
        }
        taskTextView.setTextColor(priorColor);
        TextView startTextView = (TextView) view.findViewById(R.id.start_view);
        startTextView.setText(Task.getDateTimeFormat().format(item.getStartDate()));
        TextView finishTextView = (TextView) view.findViewById(R.id.finish_view);
        finishTextView.setText(Task.getDateTimeFormat().format(item.getFinishDate()));
        CheckBox endedBox = (CheckBox) view.findViewById(R.id.ended_chb);
        endedBox.setChecked(!item.isEnded());
        endedBox.setEnabled(item.getStartDate().compareTo(Calendar.getInstance().getTime())>=0);
        return view;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }
    
    public void updateEnded (){
        try {
            ContentValues val = new ContentValues();
            val.put(KEY_ENDED, 1);
            String filter = KEY_FINISH + " < '" + Task.getDateTimeFormat().format(Calendar.getInstance().getTime())+"'"
                    + " AND "+KEY_ENDED+" = 0 ";
            int updated = database.update(TABLE_NAME, val, filter, null);
            Log.d("updENDED", "updated = "+updated);
        } catch (Exception e) {
            Log.e("updENDED", e.getMessage(),e);
        }
    }

    public Task getTaskByID(long id) {
        for (int i = 0; i < cursor.getCount(); i++) {
            Task item = getItem(i);
            if (id == item.getId()) {
                return item;
            }
        }
        return null;
    }
    
    public Task getFirstTask (){
        String[] columnsToTake = {KEY_ID, KEY_NAME, KEY_START, KEY_FINISH, KEY_DURATION, KEY_PRIORITY, KEY_COMMENT, KEY_ENDED};
        String sorting=KEY_START+ " DESC ";
        String filter=KEY_ENDED +" = 0 AND "+
                KEY_START+" > '" + Task.getDateTimeFormat().format(Calendar.getInstance().getTime())+"'";
        try {
            Cursor query = database.query(TABLE_NAME, columnsToTake, filter, null, null, null, sorting);
            if (query.moveToFirst()){
                return cursorToTask(query);
            }else{
                return null;
            }
        } catch (Exception e) {
            Log.e("ttttt", e.getMessage(),e);
            return null;
        }
    }

    @Override
    public Task getItem(int position) {
        if (cursor.moveToPosition(position)) {
            Task taskOnPositon = cursorToTask(cursor);
            return taskOnPositon;
        } else {
            throw new CursorIndexOutOfBoundsException(
                    "Cant move cursor on postion");
        }
    }

    private Task cursorToTask(Cursor curs) {
        long id = curs.getLong(ID_COLUMN);
        String name = curs.getString(NAME_COLUMN);
        int duration = curs.getInt(DURATION_COLUMN);
        String finish = curs.getString(FINISH_COLUMN);
        String start = curs.getString(START_COLUMN);
        TaskPriority priority = TaskPriority.getByOrdinal(curs.getInt(PRIORITY_COLUMN));
        String comment = curs.getString(COMMENT_COLUMN);
        boolean ended = curs.getInt(ENDED_COLUMN) > 0;
        Task taskOnPositon = new Task(id, name);
        taskOnPositon.setStartDate(start);
        taskOnPositon.setFinishDate(finish);
        taskOnPositon.setDurationInMinutes(duration);
        taskOnPositon.setPriority(priority);
        taskOnPositon.setComment(comment);
        taskOnPositon.setEnded(ended);
        return taskOnPositon;
    }

    public Cursor getAllEntries() {
        String[] columnsToTake = {KEY_ID, KEY_NAME, KEY_START, KEY_FINISH, KEY_DURATION, KEY_PRIORITY, KEY_COMMENT, KEY_ENDED};
        String sorting=null;
        if (isSortOnlyByDate()){
            sorting=KEY_ENDED+" ASC, "+KEY_START+ " DESC ";
        }else{
            sorting=KEY_ENDED+" ASC, "+KEY_PRIORITY+" , "+KEY_START+ " DESC ";
        }
        try {
            return database.query(TABLE_NAME, columnsToTake, makeFilter(), null, null, null, sorting);
        } catch (Exception e) {
            Log.e("ttttt", e.getMessage(),e);
            return null;
        }
    }

    private String makeFilter() {
        String result=null;
        if (getFilterDate()!=null){
            result = KEY_START+" like '%"+Task.getDateFormat().format(getFilterDate())+"%'";
        }
        if (!isShowAll()){
            String endedFilter=KEY_ENDED+" = 0";
            result=(result==null)?endedFilter: result+" AND "+endedFilter;
        }
        return result;
    }

    public long addItem(Task task) {
        ContentValues values = taskToValues(task);
        values.remove(KEY_ID);
        long id = database.insert(TABLE_NAME, null, values);
        task.setId(id);
        refresh();
        return id;
    }

    private ContentValues taskToValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_START, Task.getDateTimeFormat().format(task.getStartDate()));
        values.put(KEY_FINISH, Task.getDateTimeFormat().format(task.getFinishDate()));
        values.put(KEY_DURATION, task.getDurationInMinutes());
        values.put(KEY_PRIORITY, task.getPriority().ordinal());
        values.put(KEY_COMMENT, task.getComment());
        values.put(KEY_ENDED, task.isEnded() ? 1 : 0);
        return values;
    }

    public boolean removeItem(Task taskToRemove) {

        boolean isDeleted = (database.delete(TABLE_NAME, KEY_ID + "=?",
                new String[]{Long.toString(taskToRemove.getId())})) > 0;
        refresh();
        return isDeleted;
    }

    public boolean updateItem(long id, Task newTask) {
        ContentValues values = taskToValues(newTask);
        values.remove(KEY_ID);
        boolean isUpdated = (database.update(TABLE_NAME, values, KEY_ID + "=?",
                new String[]{id + ""})) > 0;
        newTask.setId(id);
        return isUpdated;
    }

    public void onDestroy() {
        dbOpenHelper.close();
    }

    public void refresh() {
        cursor = getAllEntries();
        notifyDataSetChanged();
    }

    private void init() {
        dbOpenHelper = new DbOpenHelper(context, DB_NAME, null, DB_VESION);
        try {
            database = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(this.toString(), "Error while getting database " + e.getMessage());
            throw new Error("The end");
        }
        cursor = getAllEntries();
        if (cursor.getCount() == 0) {
            addInitValues();
        }
        updateEnded();
    }

    private void addInitValues() {
        Task task = new Task(1, "tasktask1");
        Date time = Calendar.getInstance().getTime();
        Date yesterday=(Date) time.clone();
        yesterday.setDate(time.getDate()-1);
        task.setStartDate(yesterday);
        task.setFinishDate(yesterday);
        task.setDurationInMinutes(60);
        task.setPriority(TaskPriority.Important);
        task.setComment("dfgdfgdfgdf");
        task.setEnded(false);
        addItem(task);
        task = new Task(1, "tasktask2");
        task.setStartDate(time);
        task.setFinishDate(time);
        task.setDurationInMinutes(30);
        task.setPriority(TaskPriority.VeryImportant);
        task.setComment("dfgdfgdfgdf");
        task.setEnded(false);
        addItem(task);
        task = new Task(1, "tasktask3");
        task.setStartDate(time);
        task.setFinishDate(time);
        task.setDurationInMinutes(90);
        task.setPriority(TaskPriority.NotImportant);
        task.setComment("dfgdfgdfgdf");
        task.setEnded(false);
        addItem(task);
    }

    private static class DbOpenHelper extends SQLiteOpenHelper {

        public DbOpenHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String CREATE_DB = "CREATE TABLE " + TABLE_NAME + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL, "
                    + KEY_START + " TEXT, "
                    + KEY_FINISH + " TEXT, "
                    + KEY_DURATION + " INTEGER, "
                    + KEY_PRIORITY + " INTEGER NOT NULL, "
                    + KEY_COMMENT + " TEXT,"
                    + KEY_ENDED + " INTEGER NOT NULL);";
            db.execSQL(CREATE_DB);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
