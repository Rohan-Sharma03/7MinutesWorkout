package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmicalculator.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMICalculator : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW ="METRIC_UNITE_VIEW"
        private const val US_UNITS_VIEW ="US_UNITE_VIEW"
    }
    private var currentVisibleView :String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmicalculator)

        setSupportActionBar(toolbar_bmi_activity)
        val actionbar = supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title="CALCULATE BMI"
        }

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        makeMetricViewVisible()

        rgUnits.setOnCheckedChangeListener { radioGroup, i ->
            if(i==R.id.rbMetricUnits){
                makeMetricViewVisible()
            }else{
                makeUSViewVisible()
            }

        }

        btnCalculateUnits.setOnClickListener {

            if(currentVisibleView== METRIC_UNITS_VIEW){
                if (validateMetricUnits()) {
                    // The height value is converted to a float value and divided by 100 to convert it to meter.
                    val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    // The weight value is converted to a float value
                    val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()
                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)

                } else {
                    Toast.makeText(this@BMICalculator, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{

                if(validateUSUnits()){
                    // The height value is converted to a float value and divided by 100 to convert it to meter.
                    val usUnitHeightValueFeet: String =
                        etUSUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        etUSUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue = usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                    val usUnitWeightValue: Float = etUSUnitWeight.text.toString().toFloat() // Weight value entered in EditText component.
                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)

                }else{
                    Toast.makeText(this@BMICalculator, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
        // END
    }

    private fun makeUSViewVisible() {

        currentVisibleView= US_UNITS_VIEW
        llMetricUnitsView.visibility=View.GONE
        llUSUnitsView.visibility=View.VISIBLE

        etUSUnitWeight.text!!.clear()
        etUSUnitHeightFeet.text!!.clear()
        etUSUnitHeightInch.text!!.clear()
        llDisplayBMIResult.visibility=View.GONE


    }

    private fun makeMetricViewVisible() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        llUSUnitsView.visibility = View.GONE // US UNITS VIEW is hidden

        etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.
        llDisplayBMIResult.visibility=View.GONE
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        tvYourBMI.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
        llDisplayBMIResult.visibility=View.VISIBLE
    }

    private fun validateMetricUnits(): Boolean {
        var isvaild = true
        if(etMetricUnitHeight.text.toString().isEmpty() || etMetricUnitWeight.text.toString().isEmpty()){
            isvaild = false
        }
        return isvaild

    }
    private fun validateUSUnits(): Boolean {
        var isvaild = true
        if(etUSUnitHeightFeet.text.toString().isEmpty() || etUSUnitHeightInch.text.toString().isEmpty() || etUSUnitWeight.text.toString().isEmpty()){
            isvaild = false
        }
        return isvaild

    }
}