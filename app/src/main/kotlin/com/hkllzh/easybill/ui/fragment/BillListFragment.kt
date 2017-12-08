package com.hkllzh.easybill.ui.fragment

import android.os.Bundle
import android.widget.Button
import com.hkllzh.easybill.R
import com.hkllzh.easybill.base.EBBaseFragment
import com.hkllzh.easybill.bean.BillType
import com.hkllzh.easybill.ui.activity.AddEarningExpenseActivity
import com.jakewharton.rxbinding2.view.RxView

/**
 * 账单列表
 *
 * @author lizheng on 2017/12/8
 */
class BillListFragment : EBBaseFragment() {

    private val btnEarning by lazy {
        mRootView.findViewById(R.id.btnEarning) as Button
    }

    private val btnExpense by lazy {
        mRootView.findViewById(R.id.btnExpense) as Button
    }

    override fun layoutId() = R.layout.frg_bill_list

    override fun initView() {

        addDisposable(RxView.clicks(btnEarning).subscribe {
            AddEarningExpenseActivity.start(activity, BillType.EARNING)
        })

        addDisposable(RxView.clicks(btnExpense).subscribe {
            AddEarningExpenseActivity.start(activity, BillType.EXPENSE)
        })

    }

    companion object {
        private val TAG = "BillListFragment"
        fun newInstance(): BillListFragment {

            val args = Bundle()

            val fragment = BillListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}