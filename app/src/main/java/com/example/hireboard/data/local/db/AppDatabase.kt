package com.example.hireboard.data.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "hire_board.db"
        private const val DATABASE_VERSION = 1

        // User table
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD_HASH = "password_hash"
        const val COLUMN_NAME = "name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_USER_TYPE = "user_type" // 'employee' or 'employer'

        // Employee specific columns
        const val COLUMN_SKILLS = "skills"
        const val COLUMN_EXPERIENCE = "experience"
        const val COLUMN_EDUCATION = "education"

        // Employer specific columns
        const val COLUMN_COMPANY_NAME = "company_name"
        const val COLUMN_COMPANY_DESCRIPTION = "company_description"

        // Vacancy table
        const val TABLE_VACANCIES = "vacancies"
        const val COLUMN_VACANCY_ID = "vacancy_id"
        const val COLUMN_EMPLOYER_ID = "employer_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_SALARY = "salary"
        const val COLUMN_EXPERIENCE_REQUIRED = "experience_required"
        const val COLUMN_SKILLS_REQUIRED = "skills_required"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_POST_DATE = "post_date"
        const val COLUMN_IS_ACTIVE = "is_active"

        // Application table
        const val TABLE_APPLICATIONS = "applications"
        const val COLUMN_APPLICATION_ID = "application_id"
        const val COLUMN_COVER_LETTER = "cover_letter"
        const val COLUMN_APPLICATION_DATE = "application_date"
        const val COLUMN_STATUS = "status"
        const val COLUMN_EMPLOYEE_ID = "employee_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create users table
        val createUserTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL,
                $COLUMN_USER_TYPE TEXT NOT NULL,
                $COLUMN_SKILLS TEXT,
                $COLUMN_EXPERIENCE TEXT,
                $COLUMN_EDUCATION TEXT,
                $COLUMN_COMPANY_NAME TEXT,
                $COLUMN_COMPANY_DESCRIPTION TEXT
            )
        """.trimIndent()

        // Create vacancies table
        val createVacancyTable = """
            CREATE TABLE $TABLE_VACANCIES (
                $COLUMN_VACANCY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMPLOYER_ID INTEGER NOT NULL,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_SALARY TEXT NOT NULL,
                $COLUMN_EXPERIENCE_REQUIRED TEXT NOT NULL,
                $COLUMN_SKILLS_REQUIRED TEXT NOT NULL,
                $COLUMN_LOCATION TEXT NOT NULL,
                $COLUMN_POST_DATE INTEGER NOT NULL,
                $COLUMN_IS_ACTIVE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_EMPLOYER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        // Create applications table
        val createApplicationTable = """
            CREATE TABLE $TABLE_APPLICATIONS (
                $COLUMN_APPLICATION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_VACANCY_ID INTEGER NOT NULL,
                $COLUMN_EMPLOYEE_ID INTEGER NOT NULL,
                $COLUMN_COVER_LETTER TEXT NOT NULL,
                $COLUMN_APPLICATION_DATE INTEGER NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                FOREIGN KEY ($COLUMN_VACANCY_ID) REFERENCES $TABLE_VACANCIES($COLUMN_VACANCY_ID),
                FOREIGN KEY ($COLUMN_EMPLOYEE_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID)
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createVacancyTable)
        db.execSQL(createApplicationTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VACANCIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}