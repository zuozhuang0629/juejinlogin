
# Android开发仿掘金Web端登录界面(Kotlin)

## 前言

各位大佬好，给大家分享一下用Android原生实现掘金Web端的登录界面效果，有哪些可以优化希望大佬们可以指正，那我们开始吧

## 最终效果图

![LPDS_GIF_20220905_182520.gif](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/89885d07d37a435a9ec64322e2344505~tplv-k3u1fbpfcp-watermark.image?)

## 前期准备

我们需要先把需要的资源给download下来，我用`Chrome`来进行这一步

- 开启Chrmoe的调试模式: 按F12开启或者在设置->更多工具->开发工具

![1662367049960.jpg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/b22fbf4b9dcd4f89917274543878bd9d~tplv-k3u1fbpfcp-watermark.image?)

- 开是网络抓包:网络->图片

![1662367135318.jpg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c59e01c9d0554d2d84e5fa5abb5ac73b~tplv-k3u1fbpfcp-watermark.image?)

这样我们就看到了所需要的图片资料了，我们另存一下放入我们的项目

## 代码

### 配置Gradle

- 我们来配置`ViewBinding`。在`build.gradle`中的`android`添加如下代码:

```
viewBinding {
    enabled = true
}
```

- 我们需要添加一些依赖

```
//glide库
implementation 'com.github.bumptech.glide:glide:4.13.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.13.0'
```

### LoginDialog
我们创建一个`LoginDialog.kt`文件，并且继承与`DialogFragment`用于展示登录的UI，具体操作如下

- dailog_login.xml

在`layout`目录下创建`dailog_login.xml`文件，用于显示登录的布局，具体代码如下:

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    //设置这个布局的高度是自适应的
    android:layout_height="wrap_content">

    //CardView来优化布局(可以快速设置圆角、阴影等操作)
    <androidx.cardview.widget.CardView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        //这里设置88dp是因为最上面的图片高度是96dp，我们这是88dp就可以实现完成重叠效果
        android:layout_marginTop="88dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@+id/dialog_top_img">

        //为了方便布局在CardView里面添加一个约束布局
        <androidx.constraintlayout.widget.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="16dp">

            <ImageView
                android:id="@+id/imageView3"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                app:layout_constraintBottom_toBottomOf="@+id/textView2"
                app:layout_constraintEnd_toEndOf="@+id/edit_user"
                app:layout_constraintTop_toTopOf="parent"
                app:srcCompat="@drawable/ic_baseline_close_24" />

            <TextView
                android:id="@+id/textView2"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="手机登录"
                android:textColor="@color/black"
                android:textSize="20sp"
                android:textStyle="bold"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

            <EditText
                android:id="@+id/edit_user"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_marginStart="16dp"
                android:layout_marginTop="16dp"
                android:layout_marginEnd="16dp"
                android:background="@drawable/bg_edit"
                android:ems="11"
                android:hint="请输入手机号码"
                android:inputType="number"
                android:maxLength="11"
                android:paddingStart="100dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/textView2" />

            <EditText
                android:id="@+id/edit_pwd"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_marginStart="16dp"
                android:layout_marginTop="16dp"
                android:layout_marginEnd="16dp"
                android:background="@drawable/bg_edit"
                android:ems="11"
                android:hint="请输入密码"
                android:inputType="number"
                android:maxLength="4"
                android:paddingStart="10dp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@+id/edit_user" />

            <TextView
                android:id="@+id/tv_code"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginEnd="16dp"
                android:text="获取验证码"
                android:textColor="#007fff"
                android:textSize="16sp"
                app:layout_constraintBottom_toBottomOf="@+id/edit_pwd"
                app:layout_constraintEnd_toEndOf="@+id/edit_pwd"
                app:layout_constraintTop_toTopOf="@+id/edit_pwd" />

            <LinearLayout
                android:id="@+id/linearLayout"
                android:layout_width="80dp"
                android:layout_height="0dp"
                android:orientation="horizontal"
                app:layout_constraintBottom_toBottomOf="@+id/edit_user"
                app:layout_constraintStart_toStartOf="@+id/edit_user"
                app:layout_constraintTop_toTopOf="@+id/edit_user">

                <TextView
                    android:id="@+id/textView5"
                    android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:gravity="center"
                    android:text="+86"
                    android:textColor="#000000" />

                <ImageView
                    android:id="@+id/imageView2"
                    android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:scaleType="center"
                    app:srcCompat="@drawable/ic_down" />
            </LinearLayout>

            <TextView
                android:id="@+id/btn_login"
                android:layout_width="0dp"
                android:layout_height="50dp"
                android:layout_marginTop="16dp"
                android:background="@drawable/bg_btn"
                android:gravity="center"
                android:text="登录"
                android:textColor="@color/white"
                android:textSize="16sp"
                app:layout_constraintEnd_toEndOf="@+id/edit_pwd"
                app:layout_constraintStart_toStartOf="@+id/edit_pwd"
                app:layout_constraintTop_toBottomOf="@+id/edit_pwd" />

            <TextView
                android:id="@+id/textView6"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:text="其他登录方式"
                android:textColor="#007fff"
                android:textSize="16sp"
                app:layout_constraintStart_toStartOf="@+id/btn_login"
                app:layout_constraintTop_toBottomOf="@+id/btn_login" />

            <TextView
                android:id="@+id/textView7"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:text="登录即表示同意"
                android:textSize="16sp"
                app:layout_constraintStart_toStartOf="@+id/btn_login"
                app:layout_constraintTop_toBottomOf="@+id/textView6" />

            <TextView
                android:id="@+id/textView8"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="4dp"
                android:text="用户协议"
                android:textColor="#007fff"
                android:textSize="16sp"
                app:layout_constraintStart_toEndOf="@+id/textView7"
                app:layout_constraintTop_toTopOf="@+id/textView7" />

            <TextView
                android:id="@+id/textView10"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="2dp"
                android:text="、"
                android:textSize="16sp"
                app:layout_constraintStart_toEndOf="@+id/textView8"
                app:layout_constraintTop_toTopOf="@+id/textView8" />

            <TextView
                android:id="@+id/textView9"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="隐私政策"
                android:textColor="#007fff"
                android:textSize="16sp"
                app:layout_constraintStart_toEndOf="@+id/textView10"
                app:layout_constraintTop_toTopOf="@+id/textView7" />

        </androidx.constraintlayout.widget.ConstraintLayout>
    </androidx.cardview.widget.CardView>

    <ImageView
        android:id="@+id/dialog_top_img"
        android:layout_width="142dp"
        android:layout_height="96dp"
        android:elevation="2dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/ic_login_2" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### bg_edit.xml

在`drawable`目录下创建`bg_edit.xml`的资源文件，设置`EditText`的样式，代码如下：

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    //不对焦的样式
    <item android:state_window_focused="false"
        android:drawable="@drawable/bg_edit_nofocused"/>
     //对焦的样式
    <item android:state_focused="true"
        android:drawable="@drawable/bg_edit_focused" />
</selector>
```
- bg_edit_nofocused

```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="2px"/>
    <solid android:color="@color/white"/>
    <stroke android:color="#e4e6eb" android:width="1dp"/>
</shape>
```

- bg_edit_focused

```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/white"/>
    <stroke android:color="#007fff" android:width="1dp"/>
</shape>
```

- bg_btn.xml

在`drawable`目录下创建`bg_btn.xml`的资源文件，设置`TextView`的样式,不用`Button`是因为设置`background`较为麻烦，代码如下

```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">

    <solid android:color="#007fff"/>
    <corners android:radius="2px"/>
</shape>
```

-  LoginDialo

`LoginDialog.kt`中代码具体如下:

```
class LoginDialog : DialogFragment() {
    //使用viewBinding
    lateinit var mBinding: DialogLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //创建布局
        mBinding = DialogLoginBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //初始化Dialog的相关配置
        initDialog()
    }

    /**
     * 初始化dialog相关配置
     *
     */
    private fun initDialog() {
        //设置Dialog的显示大小
        setDialogSize()

        //设置window的背景为透明色
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
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
```


### MainActivity

1. 我们修改一下`MainActivity`，实现展示一个登录`Button`，点击后弹出登录界面,具体代码如下:

```
class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        
        mBinding.button.setOnClickListener { 
            LoginDialog().show(supportFragmentManager, "")
        }
    }
}
```

### 运行一下

这个时候我们的UI大致就完成了，我们运行看一下，是不是我们所有期望的那样


![Screenshot_20220905_171401_com.juejin.login.jpg](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/506468231a2c40258b2233be711892b9~tplv-k3u1fbpfcp-watermark.image?)

### 登录逻辑

我们完成了UI相关的功能，接下来我们需要开始写，登录相关的逻辑了

-  点击不同输入框显示不同UI

在web端中，当我们点击输入手机号和请输入密码时，最上面的UI是显示不同，我们先把这个一部分功能实现以下:

我们添加一个`initView()`方法，专门初始化`View`相关操作,具体代码如下：

```
private fun initView() {
    //设置焦点变化监听
    mBinding.editUser.onFocusChangeListener =
        View.OnFocusChangeListener { v, hasFocus ->
            //该控件获取了焦点
            if(hasFocus){
                //设置获取焦点后的UI
                Glide.with(this).load(R.drawable.ic_login_2).into(mBinding.dialogTopImg)
            }
        }

    mBinding.editPwd.onFocusChangeListener =
        View.OnFocusChangeListener { v, hasFocus ->
            //该控件获取了焦点
            if(hasFocus){
                //设置获取焦点后的UI
                Glide.with(this).load(R.drawable.ic_login_1).into(mBinding.dialogTopImg)
            }
        }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //让输入框获取焦点
        mBinding.editUser.requestFocus()
    }
}
```

### 获取验证码


我们知道点击获取验证码会出现一个验证是否为人为操作，当操作完成后发送验证码，并且会有一个60s间隔，并且需要显示出实际秒数，我们使用`Captcha`库来完成验证，使用`CountDownTimer`来实现倒计时的效果

#### 添加验证拼图
- 添加倒计时

```
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
```

- 添加依赖
```
implementation 'com.luozm.captcha:captcha:1.1.2'
```
- 添加布局

```
<com.luozm.captcha.Captcha
    android:id="@+id/capt_cha"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:elevation="2dp"
    android:visibility="gone"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="@+id/cardView"
    app:layout_constraintStart_toStartOf="@+id/cardView"
    app:layout_constraintTop_toTopOf="@+id/cardView"
    //随便找一个图片就行了
    app:src="@drawable/ic_captcha" />
```

- 添加事件监听

```
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
        //关闭图片验证
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
```

- 点击发送验证码
```
mBinding.tvCode.setOnClickListener {
    //显示图片验证
    mBinding.captCha.visibility = View.VISIBLE
}
```

### 登录

- 添加登录判断逻辑

我们还是在`initView`方法中添加代码:

```
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
```

## 总结

到这里我们模仿掘金Web端登录就成功了，如果想看源码在这里[传送门](https://github.com/zuozhuang0629/juejinlogin.git)





