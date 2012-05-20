package test.andro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.Calendar;

public class SqlTasksAdapter extends BaseAdapter {

    private static final String DB_NAME = "tasks_db.sqlite3";
    private static final String TABLE_NAME = "tasks";
    private static final int DB_VESION = 5;
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

    public SqlTasksAdapter(Context context) {
        super();
        this.context = context;
        init();
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
        switch (item.getPriority()) {
            case Important: {
                taskTextView.setTextColor(R.color.yellow);
            }
            break;
            case VeryImportant: {
                taskTextView.setTextColor(R.color.red);
            }
            break;
            case NotImportant: {
                taskTextView.setTextColor(R.color.blue);
            }
            break;
        }
        TextView startTextView = (TextView) view.findViewById(R.id.start_view);
        startTextView.setText(item.getStartDate().toGMTString());
        TextView finishTextView = (TextView) view.findViewById(R.id.finish_view);
        finishTextView.setText(item.getFinishDate().toGMTString());
        CheckBox endedBox = (CheckBox) view.findViewById(R.id.ended_chb);
        endedBox.setChecked(!item.isEnded());
        return view;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
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

    @Override
    public Task getItem(int position) {
        if (cursor.moveToPosition(position)) {
            long id = cursor.getLong(ID_COLUMN);
            String name = cursor.getString(NAME_COLUMN);
            int duration = cursor.getInt(DURATION_COLUMN);
            String finish = cursor.getString(FINISH_COLUMN);
            String start = cursor.getString(START_COLUMN);
            TaskPriority priority = TaskPriority.getByOrdinal(cursor.getInt(PRIORITY_COLUMN));
            String comment = cursor.getString(COMMENT_COLUMN);
            boolean ended = cursor.getInt(ENDED_COLUMN) > 0;
            Task taskOnPositon = new Task(id, name);
            taskOnPositon.setStartDate(start);
            taskOnPositon.setFinishDate(finish);
            taskOnPositon.setDurationInMinutes(duration);
            taskOnPositon.setPriority(priority);
            taskOnPositon.setComment(comment);
            taskOnPositon.setEnded(ended);
            return taskOnPositon;
        } else {
            throw new CursorIndexOutOfBoundsException(
                    "Cant move cursor on postion");
        }
    }

    public Cursor getAllEntries() {
        String[] columnsToTake = {KEY_ID, KEY_NAME, KEY_START, KEY_FINISH, KEY_DURATION, KEY_PRIORITY, KEY_COMMENT, KEY_ENDED};
        return database.query(TABLE_NAME, columnsToTake,
                null, null, null, null, KEY_ID);
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
        values.put(KEY_START, task.getStartDate().toGMTString());
        values.put(KEY_FINISH, task.getFinishDate().toGMTString());
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

    private void refresh() {
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
    }

    private void addInitValues() {
        Task task = new Task(1, "tasktask1");
        task.setStartDate(Calendar.getInstance().getTime());
        task.setFinishDate(Calendar.getInstance().getTime());
        task.setDurationInMinutes(60);
        task.setPriority(TaskPriority.Important);
        task.setComment("dfgdfgdfgdf");
        task.setEnded(false);
        addItem(task);
        task = new Task(1, "tasktask2");
        task.setStartDate(Calendar.getInstance().getTime());
        task.setFinishDate(Calendar.getInstance().getTime());
        task.setDurationInMinutes(30);
        task.setPriority(TaskPriority.VeryImportant);
        task.setComment("dfgdfgdfgdf");
        task.setEnded(false);
        addItem(task);
        task = new Task(1, "tasktask3");
        task.setStartDate(Calendar.getInstance().getTime());
        task.setFinishDate(Calendar.getInstance().getTime());
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
