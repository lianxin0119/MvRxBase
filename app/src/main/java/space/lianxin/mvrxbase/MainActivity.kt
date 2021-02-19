package space.lianxin.mvrxbase

import space.lianxin.base.ui.activity.BaseActivity
import space.lianxin.mvrxbase.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun inflateBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initData() {
    }

}