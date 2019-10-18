package com.example.demoemoji

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.libemoji.bean.FaceBean
import com.cwb.libemoji.callback.OnFaceClickListener
import com.cwb.libemoji.ui.FaceFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


    }
}
