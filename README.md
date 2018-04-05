# 该library用于封装登录逻辑，实现0重复代码登录
 1。需要登录的功能分为2类，一：进入需要登录后的页面。二：需要登录后操作的功能如点赞，弹出漂亮对话框等。
 第一种在相应界面的activity上加CheckIfLoginAndLoginAndBackToContinue注解即可实现该界面任何跳转逻辑的拦截。（注：当其继承AppCompatActivity时需要在配置文件注册，其它可以省略注册）
 第二种在点击方法上加注解即可
 2.如果你使用组件化，请将该library依赖到basemodule，登录activity也放在basemodule
 
#使用
 在application里初始化
    ```
        LoginUtil.init(this,LoginActivity.class);
    ```
  第一种
  
  ```
    @CheckIfLoginAndLoginAndBackToContinue
    public class UserProfileActivity extends Activity
    
    
    public void userProfile(View view) {
            startActivity(new Intent(this,UserProfileActivity.class));
        }
  ```
  
  第二种
  
   ```
        @CheckIfLoginAndLoginAndBackToContinue
            public void zan(View view) {
                Toast.makeText(this,"点赞",Toast.LENGTH_SHORT).show();
            }
   ```
   当登录状态发生改变时调用LoginUtil.login(b);
   
   如此这般，在登录后会自动执行原有的逻辑代码
   
 混淆
 -keepattributes *Annotation*,Signature,Exceptions,InnerClasses,EnclosingMethod
 -keep om.lzy.login_library.annotation.** { *; }
 -dontwarn com.lzy.login_library.annotation.**
 -keepclasseswithmembernames class * { @com.lzy.login_library.annotation.* <methods>; }
   