package space.lianxin.base.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MvRxViewModelStore
import com.airbnb.mvrx.MvRxViewModelStoreOwner
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import org.kodein.di.*
import org.kodein.di.android.retainedSubKodein
import org.kodein.di.generic.kcontext
import space.lianxin.base.extention.setStatusColor
import space.lianxin.base.extention.uiMode1Normal

/**
 * ===========================================
 * 所有activity的基类。
 *
 * @author: lianxin
 * @date: 2020/12/16 11:52
 * ===========================================
 */
abstract class BaseActivity<T : ViewBinding> : RxAppCompatActivity(),
    MvRxViewModelStoreOwner, KodeinAware {

    lateinit var binding: T

    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> by lazy { kcontext(this) }

    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    override val kodein: Kodein by retainedSubKodein(
        org.kodein.di.android.kodein(),
        copy = Copy.All
    ) {
        initKodein(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore()
        initStatus()
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        super.onCreate(savedInstanceState)
        binding = inflateBinding()
        setContentView(binding.root)
        kodeinTrigger.trigger()
        initView()
        initData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    /** 适配状态栏  */
    protected open fun initStatus() {
        // 默认状态栏无背景色
        setStatusColor(Color.TRANSPARENT)
        // 布局填充到状态栏
        uiMode1Normal()
    }

    /** 初始化kodein */
    protected open fun initKodein(builder: Kodein.MainBuilder) {}

    /** 这里可以做一些setContentView之前的操作,如全屏、常亮、设置Navigation颜色、状态栏颜色等  */
    protected open fun onCreateBefore() {}

    /** 初始化ViewBinding */
    protected abstract fun inflateBinding(): T

    /** 初始化View相关 */
    protected abstract fun initView()

    /** 初始化数据相关 */
    protected abstract fun initData()

}