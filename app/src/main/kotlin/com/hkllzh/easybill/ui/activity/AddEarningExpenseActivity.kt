package com.hkllzh.easybill.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseActivity
import com.hkllzh.easybill.bean.BillType
import kotlinx.android.synthetic.main.act_add_earning_expense.*

/**
 * 增加收入支出页面
 *
 * @author lizheng on 2017/12/8
 */
class AddEarningExpenseActivity : EBBaseActivity() {

    private lateinit var billType: BillType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_add_earning_expense)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        billType = BillType.valueOf(intent.getStringExtra(PARAM_BILL_TYPE))
        when (billType) {
            BillType.EARNING -> toolbar.title = "增加收入"
            BillType.EXPENSE -> toolbar.title = "增加支出"
        }

    }


    companion object {
        private val TAG = "AddEarningExpenseActivity"
        private val PARAM_BILL_TYPE = "billType"
        fun start(context: Context, bt: BillType) {
            val starter = Intent(context, AddEarningExpenseActivity::class.java)
            starter.putExtra(PARAM_BILL_TYPE, bt.name)
            context.startActivity(starter)
        }
    }

}