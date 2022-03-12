package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener{

    private var restTimer:CountDownTimer? =null
    private var exerciseTimer:CountDownTimer?=null
    private var restProgress=0
    private var exerciseProgress=0
    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExerciseNumber=-1
    private var tts:TextToSpeech?=null
    private var player:MediaPlayer?=null
    private var exerciseAdapter:ExerciseStatusAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        exerciseList = BankOfExercise.getDefaultExerciseList()

        setSupportActionBar(toolbar_exercise_activity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        tts = TextToSpeech(this, this)
        setupRestView()
        setUpExerciseStatusRecyclerView()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/

        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }

    private fun setupRestView() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        tvUpcomingExerciseName.text=exerciseList!![currentExerciseNumber+1].getName()
        setRestProgressBar()
    }


    private fun setRestProgressBar() {
        try {
//            val soundURI =
//                Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            player=MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping=false
            player!!.start()

        }catch (e:Exception){
            e.printStackTrace()
        }

        progressBarForRest.progress = restProgress
        restTimer= object :CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {
                restProgress++
                progressBarForRest.progress=10-restProgress
                tvTimerForRestView.text=(10-restProgress).toString()
            }
            override fun onFinish() {
                llRestView.visibility=View.GONE
                currentExerciseNumber++;
                exerciseList!![currentExerciseNumber].setIsSelected(true)
                exerciseList!![currentExerciseNumber].setIsCompleted(false)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }
        }.start()
    }

    private fun setupExerciseView() {
        llExerciseView.visibility= View.VISIBLE
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        ivImageOfExercise.setImageResource(exerciseList!![currentExerciseNumber].getImage())
        exerciseName.text=exerciseList!![currentExerciseNumber].getName()
        speakOut(exerciseList!![currentExerciseNumber].getName())
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {

        progressBarForExercise.progress=exerciseProgress
        exerciseTimer=object:CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                progressBarForExercise.progress=30-exerciseProgress
                tvTimerForExerciseView.text=(30-exerciseProgress).toString()
            }
            override fun onFinish() {
                if(currentExerciseNumber < exerciseList!!.size-1){
                    exerciseList!![currentExerciseNumber].setIsSelected(false)
                    exerciseList!![currentExerciseNumber].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                   startActivity(Intent(this@ExerciseActivity,FinishExercise::class.java))
                    finish()
                    }
                llExerciseView.visibility=View.GONE
                llRestView.visibility=View.VISIBLE
            }
        }.start()
    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result==TextToSpeech.LANG_NOT_SUPPORTED || result==TextToSpeech.LANG_MISSING_DATA)
                Log.e("TTS","Language not supported")
        }else{
            Log.e("TTS","Initialization failed")
        }
    }

    private fun speakOut(text :String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setUpExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter=exerciseAdapter
    }

    public override fun onDestroy() {
        if (restTimer != null ) {
            restTimer!!.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null ) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player!=null){
            player!!.stop()
        }
        super.onDestroy()
    }
}