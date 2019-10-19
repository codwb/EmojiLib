# EmojiLib
A library for showing emoji in Android.

![GIF](show.gif)


## How to use it 

It's very easy to use.

in xml layout

    <com.cwb.libemoji.ui.FaceLayout
          android:id="@+id/face_layout"
          android:layout_width="match_parent"
          android:layout_height="200dp" />

in class

    face_layout.setOnFaceClickListener(object : OnFaceClickListener {

        override fun onClick(bean: FaceBean) {
            val text = "... ${bean.content}"
            FaceCenter.handlerFaceText(tv_face, text, 30f)
        }

        override fun onDelete() {
            FaceCenter.deleteFace(tv_face, 30f)
        }

    })
  
## Add EmojiLib to your project.

[![](https://jitpack.io/v/codwb/EmojiLib.svg)](https://jitpack.io/#codwb/EmojiLib)

    allprojects {
      repositories {
        
        maven { url 'https://jitpack.io' }
      }
    }

	implementation 'com.github.codwb:EmojiLib:Tag'
