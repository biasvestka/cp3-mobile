import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "habits.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "habits"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_FREQUENCY = "frequency"
        const val COL_DESCRIPTION = "description"
        const val COL_REMINDER = "reminder"
        const val COL_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_FREQUENCY + " TEXT,"
                + COL_DESCRIPTION + " TEXT,"
                + COL_REMINDER + " TEXT,"
                + COL_STATUS + " TEXT CHECK( " + COL_STATUS + " IN ('ativo', 'inativo'))"
                + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertHabit(name: String, frequency: String, description: String, reminder: String, status: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_FREQUENCY, frequency)
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_REMINDER, reminder)
        contentValues.put(COL_STATUS, status)
        db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllHabits(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun updateHabit(id: Int, name: String, frequency: String, description: String, reminder: String, status: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_FREQUENCY, frequency)
        contentValues.put(COL_DESCRIPTION, description)
        contentValues.put(COL_REMINDER, reminder)
        contentValues.put(COL_STATUS, status)  // 'ativo' ou 'inativo'

        val result = db.update(TABLE_NAME, contentValues, "$COL_ID = ?", arrayOf(id.toString()))
        return result > 0
    }

    fun deleteHabit(id: Int): Int {
        val db = this.writableDatabase

        return db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
    }


}
