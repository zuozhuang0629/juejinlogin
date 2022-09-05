package com.juejin.login

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.juejin.login.databinding.DialogLoginBinding
import com.luozm.captcha.Captcha

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
        initView()
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

    val timeDown = object : CountDownTimer(60 * 1000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            mBinding.tvCode.text = "${millisUntilFinished / 1000}s"
        }

        override fun onFinish() {
            //设置验证码可点击
            mBinding.tvCode.isEnabled = true
            //恢复text
            mBinding.tvCode.text = "获取验证码"
        }

    }

    private fun initView() {
        //设置焦点变化监听
        mBinding.editUser.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                //该控件获取了焦点
                if (hasFocus) {
                    //设置获取焦点后的UI
                    Glide.with(this).load(R.drawable.ic_login_2).into(mBinding.dialogTopImg)
                }

            }

        mBinding.editPwd.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                //该控件获取了焦点
                if (hasFocus) {
                    //设置获取焦点后的UI
                    Glide.with(this).load(R.drawable.ic_login_1).into(mBinding.dialogTopImg)
                }

            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //让输入框获取焦点
            mBinding.editUser.requestFocus()
        }

        mBinding.tvCode.setOnClickListener {
            mBinding.captCha.visibility = View.VISIBLE
        }

        mBinding.captCha.setCaptchaListener(object : Captcha.CaptchaListener {
            /**
             * 验证通过回调
             *
             * @param time
             * @return
             */
            override fun onAccess(time: Long): String {
                //设置验证码不可点击
                mBinding.tvCode.isEnabled = false
                //开始倒计时
                timeDown.start()
                mBinding.captCha.visibility = View.GONE
                return "验证通过,耗时" + time + "毫秒";
            }

            /**
             * 验证失败回调
             *
             * @param failCount
             * @return
             */
            override fun onFailed(failCount: Int): String {
                return "验证失败,已失败" + failCount + "次";
            }

            override fun onMaxFailed(): String {
                Toast.makeText(
                    this@LoginDialog.requireContext(),
                    "验证超过次数，你的帐号被封锁",
                    Toast.LENGTH_SHORT
                ).show();
                return "验证失败,帐号已封锁";
            }

        })

        mBinding.btnLogin.setOnClickListener {
            //登录按钮不可交互
            mBinding.btnLogin.isEnabled =false

            //修改UI
            mBinding.btnLogin.text = "登录中..."

            //开始验证
            //判断手机号格式是否正确，这里只做了长度的按断，其实可以用正则来判断，我这里知识简单判断
            if(mBinding.editUser.text.toString().length < 11){
                Toast.makeText(
                    this@LoginDialog.requireContext(),
                    "账号格式错误",
                    Toast.LENGTH_SHORT
                ).show();
                //登录按钮可交互
                mBinding.btnLogin.isEnabled =true
                //修改UI
                mBinding.btnLogin.text = "登录"
                return@setOnClickListener
            }
            if(mBinding.editPwd.text.toString().length < 4){
                Toast.makeText(
                    this@LoginDialog.requireContext(),
                    "验证码错误",
                    Toast.LENGTH_SHORT
                ).show();
                //登录按钮可交互
                mBinding.btnLogin.isEnabled =true
                //修改UI
                mBinding.btnLogin.text = "登录"
                return@setOnClickListener
            }

            Toast.makeText(
                this@LoginDialog.requireContext(),
                "登录成功",
                Toast.LENGTH_SHORT
            ).show();

            dismiss()

        }
    }

    /**
     * 设置dialog的大小
     *
     */
    private fun setDialogSize() {
        val window = dialog?.window
        window?.let {

            //获取屏幕信息
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as? WindowManager
            val display = wm?.defaultDisplay
            val point = Point();
            display?.getSize(point);


            val layoutParams = it.attributes;

            //设置宽度为屏幕的百分之90
            layoutParams.width = (point.x * 0.9).toInt()
            //设置高度为自适应
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
        }
    }
}