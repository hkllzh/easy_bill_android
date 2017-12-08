package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity

/**
 * 增加收入支出页面
 *
 * @author lizheng on 2017/12/8
 */
class AddEarningExpenseActivity : EBBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_add_earning_expense)
    }


    companion object {
        private val TAG = "AddEarningExpenseActivity"
        fun start(context: Context) {
            val starter = Intent(context, AddEarningExpenseActivity::class.java)
            context.startActivity(starter)
        }
    }

}