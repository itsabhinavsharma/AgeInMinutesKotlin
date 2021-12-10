package com.example.ageinminutes

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MainActivity : AppCompatActivity() {

    var selectedTimeTV: TextView? = null
    var selectedDateTV: TextView? = null
    var resultTV: TextView? = null
    var addSelectedTime:Long = 0
    var selectedDate:String = ""
    var currentDateStr:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datePickerButton : Button = findViewById(R.id.btnDatePicker)
        val timePickerButton : Button = findViewById(R.id.btnTimePicker)
        val calculateButton  : Button = findViewById(R.id.btnCalculateButton)
        val clearButton : Button = findViewById(R.id.btnClearButton)

        selectedTimeTV = findViewById(R.id.tvSelectedTime)
        selectedDateTV = findViewById(R.id.tvSelectedDate)
        resultTV = findViewById(R.id.tvResult)

        datePickerButton.setOnClickListener {
            clickDatePicker()
        }

        timePickerButton.setOnClickListener{
            clickTimePicker()
        }

        calculateButton.setOnClickListener {
            var resultTime = addSelectedTime
            resultTV?.text = "$resultTime"
        }

        clearButton.setOnClickListener {
            addSelectedTime = 0
            resultTV?.text = ""
            selectedDateTV?.text = ""
            selectedTimeTV?.text = ""
        }
    }

    private fun clickDatePicker()
    {
        //Toast.makeText(this,"button Works",Toast.LENGTH_LONG).show()
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        //dpd is date picker dialog variable
        val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                selectedDateTV?.text = selectedDate

                var sdfDate = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
                var theDate = sdfDate.parse(selectedDate)
                theDate?.let{
                    var selectedDateInMinutes = theDate.time / 60000
                    currentDateStr = "$day/${month+1}/$year"
                    var currentDate = sdfDate.parse(sdfDate.format(System.currentTimeMillis()))
                    currentDate?.let{
                        var currentDateInMinutes = currentDate.time /60000
                        var difference = currentDateInMinutes - selectedDateInMinutes
                        addSelectedTime += difference
                    }
                }

                //Toast.makeText(this,"works",Toast.LENGTH_LONG).show()
            }
            ,year
            ,month
            ,day)

        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun clickTimePicker()
    {
        //Toast.makeText(this,"This Button also works",Toast.LENGTH_LONG).show()
        val myClock = Calendar.getInstance()
        val hour = myClock.get(Calendar.HOUR_OF_DAY)
        val minute = myClock.get(Calendar.MINUTE)
        val is24HourView = true;


        TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, selectedHourOfDay, selectedMinutes, ->

                var selectedTime = "$selectedHourOfDay:$selectedMinutes"
                selectedTimeTV?.text = selectedTime
                var selectedHourOfDayInMinutes = selectedHourOfDay * 60
                var currentHourInMinutes = hour * 60

                if(currentDateStr == selectedDate)
                {
                    var properTimeMinutes =  (currentHourInMinutes + minute) - (selectedHourOfDayInMinutes + selectedMinutes)
                    addSelectedTime += properTimeMinutes.toLong()
                }
                else
                {
                    var properTimeMinutes = selectedHourOfDayInMinutes + selectedMinutes + currentHourInMinutes + minute
                    addSelectedTime += properTimeMinutes.toLong()
                    //Toast.makeText(this,"works",Toast.LENGTH_LONG).show()
                }

            },hour,minute,is24HourView).show()

    }
}