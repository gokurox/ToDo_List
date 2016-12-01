package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Gursimran Singh on 08-11-2016.
 */

public class ListManager {
    private static ListManager uniqueInstance = null;
    private ListTableSQLiteHelper mDBHelper;
    private ArrayList<ToDoTask> mToDoList;

    private ListManager(Context context) {
        mDBHelper = ListTableSQLiteHelper.getDatabaseHelperInstance(context);
        mToDoList = new ArrayList<>();

        ArrayList<ToDoTask> listFromDB = mDBHelper.getAllTodoTasks();
        mToDoList.addAll(listFromDB);
    }

    public static ListManager getListManagerInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new ListManager(context);
        }
        return uniqueInstance;
    }

    public void addTask(ToDoTask task) {
        mToDoList.add(task);
        mDBHelper.addTodoTask(task);
    }

    public void deleteTask(int position) {
        ToDoTask task = mToDoList.get(position);
        mDBHelper.deleteTodoTask(task);
        mToDoList.remove(position);
    }

    public void updateTask(ToDoTask task) {
        int i;
        for (i = 0; i < mToDoList.size(); i++) {
            if (mToDoList.get(i).getUuid() == task.getUuid()) {
                break;
            }
        }

        if (i == mToDoList.size())
            return;

        List<ToDoTask> prefixList = mToDoList.subList(0, i);
        List<ToDoTask> suffixList = mToDoList.subList(i + 1, mToDoList.size());

        mToDoList.clear();
        mToDoList.addAll(prefixList);
        mToDoList.add(task);
        mToDoList.addAll(suffixList);

        mDBHelper.updateTodoTask(task);
    }

    public void clearAllTasks() {
        mToDoList.clear();
        mDBHelper.deleteAllTasks();
    }

    public ToDoTask getTask(int position) {
        return mToDoList.get(position);
    }

    public ArrayList<ToDoTask> getAllTasks() {
        return mToDoList;
    }

    public int getSize() {
        return mToDoList.size();
    }
}

class ListTableSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoDatabase.db";
    private static final int VERSION = 1;
    private static ListTableSQLiteHelper uniqueInstance = null;
    private String TODO_TABLE_NAME = "todoTable";
    private String TODO_COL_UUID = "uuid";
    private String TODO_COL_TITLE = "title";
    private String TODO_COL_DESCRIPTION = "description";
    private String TODO_COL_CHRONOLOGY = "chronology";

    private ListTableSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static ListTableSQLiteHelper getDatabaseHelperInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new ListTableSQLiteHelper(context, DATABASE_NAME, null, VERSION);
        }
        return uniqueInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TODO_TABLE_NAME + " (" +
                        TODO_COL_UUID + " TEXT PRIMARY KEY, " +
                        TODO_COL_TITLE + " TEXT, " +
                        TODO_COL_DESCRIPTION + " TEXT, " +
                        TODO_COL_CHRONOLOGY + " TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE_NAME);
        onCreate(db);
    }

    public ToDoTask getTodoTask(String taskUuid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDoTask> toDoTasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TODO_TABLE_NAME + " WHERE " + TODO_COL_UUID + " = " + taskUuid.toString(), null);

        cursor.moveToFirst();
        String uuid = cursor.getString(cursor.getColumnIndex(TODO_COL_UUID));
        String title = cursor.getString(cursor.getColumnIndex(TODO_COL_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(TODO_COL_DESCRIPTION));
        String chronology = cursor.getString(cursor.getColumnIndex(TODO_COL_CHRONOLOGY));

        ToDoTask task = new ToDoTask(uuid, title, description, chronology);
        return task;
    }

    public ArrayList<ToDoTask> getAllTodoTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDoTask> toDoTasks = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TODO_TABLE_NAME, null);

        while (cursor.moveToNext()) {
            String uuid = cursor.getString(cursor.getColumnIndex(TODO_COL_UUID));
            String title = cursor.getString(cursor.getColumnIndex(TODO_COL_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(TODO_COL_DESCRIPTION));
            String chronology = cursor.getString(cursor.getColumnIndex(TODO_COL_CHRONOLOGY));

            ToDoTask task = new ToDoTask(uuid, title, description, chronology);
            toDoTasks.add(task);
        }

        return toDoTasks;
    }

    public void addTodoTask(ToDoTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TODO_COL_UUID, task.getUuid().toString());
        contentValues.put(TODO_COL_TITLE, task.getTitle());
        contentValues.put(TODO_COL_DESCRIPTION, task.getDescription());
        contentValues.put(TODO_COL_CHRONOLOGY, task.getChronology().toString());

        db.insert(TODO_TABLE_NAME, null, contentValues);
    }

    public void updateTodoTask(ToDoTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TODO_COL_TITLE, task.getTitle());
        contentValues.put(TODO_COL_DESCRIPTION, task.getDescription());
        contentValues.put(TODO_COL_CHRONOLOGY, task.getChronology().toString());

        db.update(TODO_TABLE_NAME, contentValues, TODO_COL_UUID + " = ?", new String[]{task.getUuid().toString()});
    }

    public void deleteTodoTask(ToDoTask task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE_NAME, TODO_COL_UUID + " = ? ", new String[]{task.getUuid().toString()});
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE_NAME, "1", null);
    }
}

class ToDoTask {
    private UUID uuid;
    private String title;
    private String description;
    private GregorianCalendar chronology;

    public ToDoTask(String uuid, String title, String description, String chronology) {
        this.uuid = UUID.fromString(uuid);
        this.title = title;
        this.description = description;
        this.chronology = TimestampToGregCal(chronology);
    }

    public ToDoTask(String title, String description, GregorianCalendar chronology) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.chronology = chronology;
    }

    public ToDoTask(String title, String description, String chronology) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.chronology = TimestampToGregCal(chronology);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChronology() {
        return GregCalToTimestamp(chronology);
    }

    public void setChronology(GregorianCalendar chronology) {
        this.chronology = chronology;
    }

    private String GregCalToTimestamp(GregorianCalendar gregCal) {
        int year = gregCal.get(Calendar.YEAR);
        int month = gregCal.get(Calendar.MONTH);
        int dayOfMonth = gregCal.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = gregCal.get(Calendar.HOUR_OF_DAY);
        int minute = gregCal.get(Calendar.MINUTE);
        int second = gregCal.get(Calendar.SECOND);

        return (new String(year + "/" + month + "/" + dayOfMonth + " " +
                hourOfDay + ":" + minute + ":" + second));
    }

    private GregorianCalendar TimestampToGregCal(String timestamp) {
        String[] dateTimeSplit, dateSplit, timeSplit;
        int year, month, dayOfMonth, hourOfDay, minute, second;

        try {
            dateTimeSplit = timestamp.split(" ");
            dateSplit = dateTimeSplit[0].split("/");
            timeSplit = dateTimeSplit[1].split(":");

            year = Integer.parseInt(dateSplit[0]);
            month = Integer.parseInt(dateSplit[1]);
            dayOfMonth = Integer.parseInt(dateSplit[2]);
            hourOfDay = Integer.parseInt(timeSplit[0]);
            minute = Integer.parseInt(timeSplit[1]);
            second = Integer.parseInt(timeSplit[2]);
        } catch (Exception e) {
            return (new GregorianCalendar());
        }
        return (new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second));
    }
}
