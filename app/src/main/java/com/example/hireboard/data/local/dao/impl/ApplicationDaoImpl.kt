package com.example.hireboard.data.local.dao.impl

import android.content.ContentValues
import android.database.Cursor
import com.example.hireboard.data.local.dao.ApplicationDao
import com.example.hireboard.data.local.db.AppDatabase
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.ApplicationStatus

class ApplicationDaoImpl(private val dbHelper: AppDatabase) : ApplicationDao {

    override fun insertApplication(application: Application): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_VACANCY_ID, application.vacancyId)
            put(AppDatabase.COLUMN_EMPLOYEE_ID, application.employeeId)
            put(AppDatabase.COLUMN_COVER_LETTER, application.coverLetter)
            put(AppDatabase.COLUMN_APPLICATION_DATE, application.applicationDate)
            put(AppDatabase.COLUMN_STATUS, application.status.name)
        }

        return db.insert(AppDatabase.TABLE_APPLICATIONS, null, values)
    }

    override fun getApplicationById(id: Long): Application? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_APPLICATIONS,
            null,
            "${AppDatabase.COLUMN_APPLICATION_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            getApplicationFromCursor(cursor).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    override fun getApplicationsByEmployee(employeeId: Long): List<Application> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_APPLICATIONS,
            null,
            "${AppDatabase.COLUMN_EMPLOYEE_ID} = ?",
            arrayOf(employeeId.toString()),
            null, null, "${AppDatabase.COLUMN_APPLICATION_DATE} DESC"
        )

        val applications = mutableListOf<Application>()
        while (cursor.moveToNext()) {
            applications.add(getApplicationFromCursor(cursor))
        }
        cursor.close()
        return applications
    }

    override fun getApplicationsForVacancy(vacancyId: Long): List<Application> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_APPLICATIONS,
            null,
            "${AppDatabase.COLUMN_VACANCY_ID} = ?",
            arrayOf(vacancyId.toString()),
            null, null, "${AppDatabase.COLUMN_APPLICATION_DATE} DESC"
        )

        val applications = mutableListOf<Application>()
        while (cursor.moveToNext()) {
            applications.add(getApplicationFromCursor(cursor))
        }
        cursor.close()
        return applications
    }

    private fun getApplicationFromCursor(cursor: Cursor): Application {
        return Application(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_APPLICATION_ID)),
            vacancyId = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_VACANCY_ID)),
            employeeId = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EMPLOYEE_ID)),
            coverLetter = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_COVER_LETTER)),
            applicationDate = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_APPLICATION_DATE)),
            status = ApplicationStatus.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_STATUS)))
        )
    }

    override fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_STATUS, status.name)
        }

        return db.update(
            AppDatabase.TABLE_APPLICATIONS,
            values,
            "${AppDatabase.COLUMN_APPLICATION_ID} = ?",
            arrayOf(applicationId.toString())
        )
    }

    override fun deleteApplication(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AppDatabase.TABLE_APPLICATIONS,
            "${AppDatabase.COLUMN_APPLICATION_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
