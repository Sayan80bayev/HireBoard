package com.example.hireboard.data.local.dao.impl

import android.content.ContentValues
import android.database.Cursor
import com.example.hireboard.data.local.dao.VacancyDao
import com.example.hireboard.data.local.db.AppDatabase
import com.example.hireboard.domain.model.Vacancy

class VacancyDaoImpl(private val dbHelper: AppDatabase) : VacancyDao {

    override fun insertVacancy(vacancy: Vacancy): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_EMPLOYER_ID, vacancy.employerId)
            put(AppDatabase.COLUMN_TITLE, vacancy.title)
            put(AppDatabase.COLUMN_DESCRIPTION, vacancy.description)
            put(AppDatabase.COLUMN_SALARY, vacancy.salary)
            put(AppDatabase.COLUMN_EXPERIENCE_REQUIRED, vacancy.experienceRequired)
            put(AppDatabase.COLUMN_SKILLS_REQUIRED, vacancy.skillsRequired)
            put(AppDatabase.COLUMN_LOCATION, vacancy.location)
            put(AppDatabase.COLUMN_POST_DATE, vacancy.postDate)
            put(AppDatabase.COLUMN_IS_ACTIVE, vacancy.isActive)
        }

        return db.insert(AppDatabase.TABLE_VACANCIES, null, values)
    }

    override fun getVacancyById(id: Long): Vacancy? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_VACANCIES,
            null,
            "${AppDatabase.COLUMN_VACANCY_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            getVacancyFromCursor(cursor).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    override fun getVacanciesByEmployer(employerId: Long): List<Vacancy> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_VACANCIES,
            null,
            "${AppDatabase.COLUMN_EMPLOYER_ID} = ?",
            arrayOf(employerId.toString()),
            null, null, "${AppDatabase.COLUMN_POST_DATE} DESC"
        )

        val vacancies = mutableListOf<Vacancy>()
        while (cursor.moveToNext()) {
            vacancies.add(getVacancyFromCursor(cursor))
        }
        cursor.close()
        return vacancies
    }

    override fun getAllActiveVacancies(): List<Vacancy> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_VACANCIES,
            null,
            "${AppDatabase.COLUMN_IS_ACTIVE} = 1",
            null,
            null, null, "${AppDatabase.COLUMN_POST_DATE} DESC"
        )

        val vacancies = mutableListOf<Vacancy>()
        while (cursor.moveToNext()) {
            vacancies.add(getVacancyFromCursor(cursor))
        }
        cursor.close()
        return vacancies
    }

    override fun searchVacancies(query: String): List<Vacancy> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_VACANCIES,
            null,
            "${AppDatabase.COLUMN_IS_ACTIVE} = 1 AND " +
                    "(${AppDatabase.COLUMN_TITLE} LIKE ? OR " +
                    "${AppDatabase.COLUMN_DESCRIPTION} LIKE ? OR " +
                    "${AppDatabase.COLUMN_SKILLS_REQUIRED} LIKE ?)",
            arrayOf("%$query%", "%$query%", "%$query%"),
            null, null, "${AppDatabase.COLUMN_POST_DATE} DESC"
        )

        val vacancies = mutableListOf<Vacancy>()
        while (cursor.moveToNext()) {
            vacancies.add(getVacancyFromCursor(cursor))
        }
        cursor.close()
        return vacancies
    }

    private fun getVacancyFromCursor(cursor: Cursor): Vacancy {
        return Vacancy(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_VACANCY_ID)),
            employerId = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EMPLOYER_ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_TITLE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_DESCRIPTION)),
            salary = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_SALARY)),
            experienceRequired = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EXPERIENCE_REQUIRED)),
            skillsRequired = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_SKILLS_REQUIRED)),
            location = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_LOCATION)),
            postDate = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_POST_DATE)),
            isActive = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_IS_ACTIVE)) == 1
        )
    }

    override fun updateVacancy(vacancy: Vacancy): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_TITLE, vacancy.title)
            put(AppDatabase.COLUMN_DESCRIPTION, vacancy.description)
            put(AppDatabase.COLUMN_SALARY, vacancy.salary)
            put(AppDatabase.COLUMN_EXPERIENCE_REQUIRED, vacancy.experienceRequired)
            put(AppDatabase.COLUMN_SKILLS_REQUIRED, vacancy.skillsRequired)
            put(AppDatabase.COLUMN_LOCATION, vacancy.location)
            put(AppDatabase.COLUMN_IS_ACTIVE, vacancy.isActive)
        }

        return db.update(
            AppDatabase.TABLE_VACANCIES,
            values,
            "${AppDatabase.COLUMN_VACANCY_ID} = ?",
            arrayOf(vacancy.id.toString())
        )
    }

    override fun deleteVacancy(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AppDatabase.TABLE_VACANCIES,
            "${AppDatabase.COLUMN_VACANCY_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
