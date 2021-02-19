package space.lianxin.base.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.trello.rxlifecycle3.components.support.RxFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.KodeinTrigger
import org.kodein.di.generic.kcontext
import space.lianxin.base.app.BaseApplication
import space.lianxin.base.extention.screenHeight
import space.lianxin.base.extention.screenWidth

/**
 * ===========================================
 * 所有fragment的基类。
 *
 * @author: lianxin
 * @date: 2020/12/17 13:12
 * ===========================================
 */
abstract class BaseFragment<T : ViewBinding> : RxFragment(), KodeinAware {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    /** 屏幕宽度  */
    var mScreenWidth: Int = 0

    /** 屏幕高度(包含状态栏高度但不包含底部虚拟按键高度)  */
    var mScreenHeight: Int = 0

    /** 屏幕状态栏高度  */
    var mStatusBarHeight: Int = 0
    var isFragmentVisible = true

    /** 上下文  */
    protected lateinit var mContext: Context
    protected lateinit var mActivity: FragmentActivity
    private var isPrepared = false // 初始化完成
    private var isFirst = true // 第一次加载
    private var isInViewPager = false

    override val kodeinTrigger = KodeinTrigger()
    override val kodeinContext: KodeinContext<*> get() = kcontext(activity)
    override val kodein: Kodein = Kodein.lazy {
        extend(BaseApplication.INSTANCE.kodein)
        initKodein(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
        this.mActivity = requireActivity()
        mScreenWidth = mContext.screenWidth()
        mScreenHeight = mContext.screenHeight()
        mStatusBarHeight = BarUtils.getStatusBarHeight()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBeforeCreateView(savedInstanceState)
        _binding = inflateBinding()
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kodeinTrigger.trigger()
        initView(view)
        isPrepared = true
        lazyLoad()
    }

    /** 视图真正可见的时候才调用 */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFragmentVisible = isVisibleToUser
        isInViewPager = true
        lazyLoad()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    /** mActivity 是否初始化完成 */
    fun isMActivityInitialized(): Boolean {
        return this::mActivity.isInitialized
    }

    /** 懒加载 */
    private fun lazyLoad() {
        if (!isInViewPager) {
            isFirst = false
            initData()
            return
        }
        if (!isPrepared || !isFragmentVisible || !isFirst) {
            return
        }
        isFirst = false
        initData()
    }

    /** 需要在onCreateView中调用的方法 */
    protected open fun initBeforeCreateView(savedInstanceState: Bundle?) {}

    /** 初始化kodein */
    protected open fun initKodein(builder: Kodein.MainBuilder) {}

    /** 初始化ViewBinding */
    protected abstract fun inflateBinding(): T

    /** 初始化View */
    protected abstract fun initView(root: View?)

    /** 初始化数据 */
    protected abstract fun initData()

}