package com.example.tacos

import android.widget.ImageView
import java.io.File

class ModelClass(name: String, iv_image: String, nameVb: String, iv_imageVb: String) {

    var name: String
        internal set

    var iv_image: String
        internal set

    var nameVb: String
        internal set

    var iv_imageVb: String
        internal set

    init {
        this.name = name
    }

    fun getName(): Any {
        return name
    }

    init {
        this.iv_image = iv_image.toString()
    }

    fun getivimage(): Any {
        return iv_image
    }

    init {
        this.nameVb = nameVb
    }

    fun getnameVb(): Any {
        return nameVb
    }

    init {
        this.iv_imageVb = iv_imageVb.toString()
    }

    fun getiv_imageVb(): Any {
        return iv_imageVb
    }
}
