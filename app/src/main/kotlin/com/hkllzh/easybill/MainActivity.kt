package com.hkllzh.easybill

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hkllzh.easybill.ui.activity.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast

/**
 * 主页
 *
 * @author lizheng on 2017/11/29
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNoActionVersion.setOnClickListener {
            toast("进入无 Action Bar 的版本")
            LoginActivity.start(this)
        }

        btnHaveActionVersion.setOnClickListener {
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
