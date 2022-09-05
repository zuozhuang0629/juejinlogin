package com.juejin.login

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.juejin.login.databinding.DialogLoginBinding

class LoginDialog : DialogFragment() {

    lateinit var mBinding: DialogLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogLoginBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
    }

    /**
     * 初始化dialog相关配置
     *
     */
    private fun initDialog() {

        setDialogSize()

        //设置window的背景为透明色
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //设置点击空白和返回键不消失
        dialog?.setCanceledOnTouchOutside(false)
        //设置dialog的动画
        dialog?.window?.setWindowAnimations(R.style.dialog_base_anim)
    }

    /**
     * 设置dialog的大小
     *
     */
    private fun setDialogSize(){
        val window = dialog?.window
        window?.let {

            //获取屏幕信息
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val display = wm?.defaultDisplay
            val point = Point();
            display?.getSize(point);


            val layoutParams = it.attributes;

            //设置宽度为屏幕的百分之90
            layoutParams.width =  (point.x * 0.9).toInt()
            //设置高度为自适应
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }
}