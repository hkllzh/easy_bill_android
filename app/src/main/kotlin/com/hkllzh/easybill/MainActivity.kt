package com.hkllzh.easybill

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNoActionVersion.setOnClickListener {
            toast("进入无 Action Bar 的版本")
        }

        btnHaveActionVersion.setOnClickListener {
            //            alert {
//                title = "title"
//                message = "msg"
////                yesButton {
////                    title = "yes"
////                }
////                noButton {
////                    title = "d"
////                }
//                onCancelled {
//                    toast("onCancelled")
//                }
//                yesButton {
//
//                }
//            }.show()


//            alert("Order", "Do you want to order this item?") {
//                positiveButton("Yes") { toast("yes") }
//                negativeButton("No") { toast("no") }
//            }.show()

            alert {
                title = "title"
                message = "msg"
                positiveButton("positiveButton") { toast("positiveButton") }
                negativeButton("negativeButton") { toast("negativeButton") }
//                neutralPressed("negativeButton") { toast("negativeButton") }
            }.show()

        }
    }
}
