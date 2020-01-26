package com.example.tamagotchi

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.tutorialbox.*

class ktActivityCreate : AppCompatActivity(){
    private val TAG = "ActivityCreate"
    private var touchIndex = 0                      //오박사를 몇번 터치했는지 확인하기 위한 변수
    private var hatch = 0                           //알의 깨지는 효과를 위해 이미지를 변경한다. 이때 알을 몇 번 터치 했는지에 따라 이미지가 변경되는데 이를 위한 변수이다.
    private var isHatchStart = false

    private lateinit var adCreatePet : AlertDialog

    companion object{
        val sharedPref : SharedPref = SharedPref()
        lateinit var myPet : ktClassPet
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create)

        if(sharedPref.getString(
                        context = this,
                        prefsFileName = sharedPref.FILE_MY_PET_DATA,
                        key = sharedPref.KEY_NAME
                ).equals("EMPTY")){
            setLog("저장된 데이터 없음 : 최초 생성")

            printDoctor()


            //터치시 알이 흔들리는 애니메이션
            val shake : Animation = AnimationUtils.loadAnimation(this,R.anim.shake)

            ac_create_img_egg.setOnTouchListener { view, event ->
                if(isHatchStart){

                    setLog("터치한 횟수 : ${hatch}")
                    //진동 사용하기위한 객체

                    val vibrator : Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(200)

                    hatch ++

                    ac_create_img_wakeup.visibility = View.VISIBLE
                    ac_create_img_wakeup.x = event.x
                    ac_create_img_wakeup.y = event.y

                    //알 터치 횟수 마다 이미지 변경 됨.
                    when(hatch){
                        3->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg1"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                        6->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg2"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                        9->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg3"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                        12->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg4"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                        15->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg5"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                        18->{
                            ac_create_img_egg.setImageURI(Uri.parse("android.resource://com.example.tamagotchi/drawable/egg6"))
                            ac_create_img_egg.startAnimation(shake)
                        }
                    }
                    if(hatch>18){
                        ac_create_img_wakeup.visibility = View.GONE
                        setLog("알생성")
                    }
                }

                true
            }

        }
        else{
            setLog("저장된 데이터 있음 : 알 생성 생략")
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
        }





    }
    private fun printDoctor(){
        //포켓몬 박사 다이얼로그
        var doctorDialog = Dialog(this)
        doctorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        doctorDialog.setContentView(R.layout.tutorialbox)
        doctorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        doctorDialog.window?.setGravity(Gravity.BOTTOM)

        var doctorTalk : CustomTextView = doctorDialog.findViewById(R.id.dialog_tutorial_doctor_content)
        doctorTalk.setText("반갑네. 게임 진행을 원한다면 나를 터치해주게")

        doctorDialog.dialog_tutorial_layout.setOnClickListener {
            when(touchIndex){
                0 -> {
                    doctorTalk.setText("나의 이름은 오박사.")
                    touchIndex++
                }
                1 -> {
                    doctorTalk.setText("모두로부터 포켓몬 박사라고 존경받고 있지.")
                    touchIndex++
                }
                2 -> {
                    doctorTalk.setText("포켓몬 월드에 발을 처음 들인 자네에게 포켓몬 알을 선물할까 하네.")
                    touchIndex++
                }
                3 -> {
                    doctorTalk.setText("알을 받으면 알을 잘 어루만지듯 터치해보게. 아주 시비로운 일이 벌어질게야")
                    touchIndex++
                }
                4 -> {
                    doctorTalk.setText("자!! 여기 포켓몬 알!")
                    touchIndex++
                }
                5 -> {
                    isHatchStart = true
                    doctorDialog.dismiss()
                }
            }
        }
        doctorDialog.show()
    }
    //펫이 생성되고, 펫의 이름을 사용자가 입력한다.
    private fun createPet(){
        var builder = AlertDialog.Builder(this)

        //var adCreatePet = AlertDialog.Builder(this)
        builder.setTitle("파이리 이름 정하기")
        var etxName = EditText(this)
        builder.setView(etxName)

        var dialog_listener = object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->{
                        setLog("확인버튼")

                        myPet = ktClassPet(etxName.text.toString())

                        sharedPref.setString(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_NAME, myPet.name)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_HUNGER, myPet.hunger)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_STAMINA, myPet.stamina)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_HEALTH, myPet.health)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_EMOTION, myPet.emotion)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_GOLD, myPet.gold)
                        sharedPref.setInt(this@ktActivityCreate, sharedPref.FILE_MY_PET_DATA,sharedPref.KEY_GROWTH, myPet.growth)

                        adCreatePet.dismiss()
                        startActivity(Intent(this@ktActivityCreate,ActivityMain::class.java))
                        finish()

                    }
                    DialogInterface.BUTTON_NEGATIVE ->
                        setLog("취소버튼")
                }
            }
        }
        builder.setPositiveButton("확인",dialog_listener)
        builder.setNegativeButton("취소",dialog_listener)
        adCreatePet = builder.create()
        adCreatePet.show()
    }
    private fun setLog(content:String){
        Log.d(TAG,content)
    }
}