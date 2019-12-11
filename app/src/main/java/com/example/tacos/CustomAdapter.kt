package com.example.tacos

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*

import java.util.ArrayList

import com.bumptech.glide.Glide
import com.example.tacos.R
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class CustomAdapter(
    internal var context: Context,
    internal var itemModelList: ArrayList<ModelClass>
) : BaseAdapter() {
    override fun getCount(): Int {
        return itemModelList.size
    }

    override fun getItem(position: Int): Any {
        return itemModelList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = null
        if (convertView == null) {
            val mInflater = context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater!!.inflate(R.layout.list_item, null)
            val tvName = convertView!!.findViewById(R.id.tvName) as TextView
            val imgRemove = convertView!!.findViewById(R.id.imgRemove) as ImageView
            val imgEdit = convertView!!.findViewById(R.id.imgEdit) as ImageView
            val imgRemove2 = convertView!!.findViewById(R.id.imgRemove2) as ImageView
            val linearLayout = convertView!!.findViewById(R.id.llitem) as LinearLayout
            val linearLayout2 = convertView!!.findViewById(R.id.llitem2) as LinearLayout
            val iv_image = convertView!!.findViewById(R.id.iv_image) as ImageView
            val m = itemModelList[position]

            if (itemModelList.get(position).getName().equals("")) {

                linearLayout.visibility = GONE
                Glide.with(context).load(itemModelList.get(position).getivimage()).into(iv_image)
            } else {
                linearLayout2.visibility = GONE
                tvName.setText(itemModelList.get(position).getName().toString()).toString()
            }

            // for remove text

            imgRemove.setOnClickListener {
                itemModelList.removeAt(position)
                notifyDataSetChanged()
            }

            // for edit the text in dialogbox

            imgEdit.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {

                    val taskEditText = EditText(context)
                    val alertDialogBuilder =
                        AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog)
                    alertDialogBuilder.setView(taskEditText)
                    alertDialogBuilder.setPositiveButton(
                        "Done"
                    ) { dialog, which ->
                        if (TextUtils.isEmpty(taskEditText.text.toString())) {
                            Toast.makeText(context, "Fill Data", Toast.LENGTH_SHORT).show()
                        } else {
                            tvName.text = taskEditText.text.toString()
                        }
                    }

                    alertDialogBuilder.setNegativeButton(
                        "Cancel",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                dialog.dismiss()
                            }
                        })

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            })

            // long press click on text

            linearLayout.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(view: View): Boolean {

                    if (imgRemove.visibility == VISIBLE) {
                        imgEdit.visibility = View.INVISIBLE
                        imgRemove.visibility = View.INVISIBLE
                    } else {
                        imgEdit.visibility = VISIBLE
                        imgRemove.visibility = VISIBLE
                    }
                    return false
                }
            })

            // remove image

            imgRemove2.setOnClickListener {
                itemModelList.removeAt(position)
                notifyDataSetChanged()
            }

            // long press on image

            linearLayout2.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(view: View): Boolean {

                    imgRemove2.visibility = View.VISIBLE

                    return false
                }
            })


            imgRemove.visibility = View.INVISIBLE
        }
        return convertView
    }

}