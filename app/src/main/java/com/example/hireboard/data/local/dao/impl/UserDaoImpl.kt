package com.example.hireboard.data.local.dao.impl

import android.content.ContentValues
import android.database.Cursor
import com.example.hireboard.data.local.dao.UserDao
import com.example.hireboard.data.local.db.AppDatabase
import com.example.hireboard.domain.model.User

class UserDaoImpl(private val dbHelper: AppDatabase) : UserDao {

    override fun insertUser(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_EMAIL, user.email)
            put(AppDatabase.COLUMN_PASSWORD_HASH, user.passwordHash)
            put(AppDatabase.COLUMN_NAME, user.name)
            put(AppDatabase.COLUMN_PHONE, user.phone)

            when (user) {
                is User.Employee -> {
                    put(AppDatabase.COLUMN_USER_TYPE, "employee")
                    put(AppDatabase.COLUMN_SKILLS, user.skills)
                    put(AppDatabase.COLUMN_EXPERIENCE, user.experience)
                    put(AppDatabase.COLUMN_EDUCATION, user.education)
                }
                is User.Employer -> {
                    put(AppDatabase.COLUMN_USER_TYPE, "employer")
                    put(AppDatabase.COLUMN_COMPANY_NAME, user.companyName)
                    put(AppDatabase.COLUMN_COMPANY_DESCRIPTION, user.companyDescription)
                }
            }
        }

        return db.insert(AppDatabase.TABLE_USERS, null, values)
    }

    override fun getUserById(id: Long): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_USERS,
            null,
            "${AppDatabase.COLUMN_USER_ID} = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            getUserFromCursor(cursor).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    override fun getUserByEmail(email: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            AppDatabase.TABLE_USERS,
            null,
            "${AppDatabase.COLUMN_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            getUserFromCursor(cursor).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    private fun getUserFromCursor(cursor: Cursor): User {
        val id = cursor.getLong(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_USER_ID))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EMAIL))
        val passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_PASSWORD_HASH))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_NAME))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_PHONE))
        val userType = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_USER_TYPE))

        return if (userType == "employee") {
            User.Employee(
                id = id,
                email = email,
                passwordHash = passwordHash,
                name = name,
                phone = phone,
                skills = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_SKILLS)),
                experience = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EXPERIENCE)),
                education = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_EDUCATION))
            )
        } else {
            User.Employer(
                id = id,
                email = email,
                passwordHash = passwordHash,
                name = name,
                phone = phone,
                companyName = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_COMPANY_NAME)),
                companyDescription = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabase.COLUMN_COMPANY_DESCRIPTION))
            )
        }
    }

    override fun updateUser(user: User): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AppDatabase.COLUMN_EMAIL, user.email)
            put(AppDatabase.COLUMN_NAME, user.name)
            put(AppDatabase.COLUMN_PHONE, user.phone)

            when (user) {
                is User.Employee -> {
                    put(AppDatabase.COLUMN_SKILLS, user.skills)
                    put(AppDatabase.COLUMN_EXPERIENCE, user.experience)
                    put(AppDatabase.COLUMN_EDUCATION, user.education)
                }
                is User.Employer -> {
                    put(AppDatabase.COLUMN_COMPANY_NAME, user.companyName)
                    put(AppDatabase.COLUMN_COMPANY_DESCRIPTION, user.companyDescription)
                }
            }
        }

        return db.update(
            AppDatabase.TABLE_USERS,
            values,
            "${AppDatabase.COLUMN_USER_ID} = ?",
            arrayOf(user.id.toString())
        )
    }

    override fun deleteUser(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            AppDatabase.TABLE_USERS,
            "${AppDatabase.COLUMN_USER_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
