package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_finish_exercise.*
import java.text.SimpleDateFormat
import java.util.*

class FinishExercise : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_exercise)

        setSupportActionBar(toolbar_finish_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        btnFinish.setOnClickListener {
            finish()
        }
        addDateToDatabase()
    }

    private fun addDateToDatabase() {

        val c = Calendar.getInstance() // Calender Current Instance
        val dateTime = c.time // Current Date and Time of the system.
        Log.e("Date : ", "" + dateTime) // Printed in the log.
        val dbHandler =SqliteOpenHelper(applicationContext,null)


        /**
         * Here we have taken an instance of Date Formatter as it will format our
         * selected date in the format which we pass it as an parameter and Locale.
         * Here I have passed the format as dd MMM yyyy HH:mm:ss.
         *
         * The Locale : Gets the current value of the default locale for this instance
         * of the Java Virtual Machine.
         */
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()) // Date Formatter
        val date = sdf.format(dateTime) // dateTime is formatted in the given format.
        dbHandler.addDate(date)
        Log.e("Formatted Date : ", "" + date) // Formatted date is printed in the log.
          }
}