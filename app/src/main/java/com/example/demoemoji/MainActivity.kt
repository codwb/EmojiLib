package com.example.demoemoji

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cwb.libemoji.FaceCenter
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.callback.OnFaceClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         //使用fragment
         val beginTransaction = supportFragmentManager.beginTransaction()
          beginTransaction.replace(
              R.id.frame,
              FaceFragment.getInstance(object : OnFaceClickListener {
                  override fun onClick(bean: FaceBean) {
                      Toast.makeText(baseContext, "content: ${bean.content}", Toast.LENGTH_SHORT)
                          .show()
                  }

                  override fun onDelete() {
                  }

              })
          )
          beginTransaction.commit()

         */

        face_layout.setOnFaceClickListener(object : OnFaceClickListener {

            @SuppressLint("SetTextI18n")
            override fun onClick(bean: FaceBean) {
                val text = "${tv_face.text} ${bean.content}"
                FaceCenter.showFace(tv_face, text, 30f)
            }

            override fun onDelete() {
                FaceCenter.deleteFace(tv_face, 30f)
            }

        })


    }
}
