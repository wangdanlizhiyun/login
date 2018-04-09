# 该library基于ams的hook和aop封装登录逻辑，实现0重复代码登录
 1。需要登录的功能分为2类.
   一：进入需要登录后的页面。二：需要登录后操作的功能如点赞，弹出漂亮对话框等。
 
   第一种在相应界面的activity上加CheckIfLoginAndLoginAndBackToContinue注解即可实现该界面任何跳转逻辑的拦截。
  （注：当其继承AppCompatActivity时需要在配置文件注册，其它可以省略注册）
 
   第二种在点击方法上加注解即可
 
 2.如果你使用组件化，请将该library依赖到basemodule，登录activity也放在basemodule
 
 3.登录拦截支持多线程，登录状态的改变也支持在子线程使用，登录后可自行执行原有的代码流程（对于任意线程都支持）
 
#使用
 在application里初始化
    ```
        LoginUtil.init(this,LoginActivity.class);
    ```
  第一种
  
  ```
    @CheckIfLoginAndLoginAndBackToContinue
    public class UserProfileActivity extends Activity
    
    
    //点击默认头像前往个人中心（自动拦截登录）
        public void userProfilePhoto(View view) {
            Intent intent = new Intent(this,UserProfileActivity.class);
            intent.putExtra("a","a");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        //点击默认昵称前往个人中心（自动拦截登录）
        public void userProfileName(View view) {
            Intent intent = new Intent(this,UserProfileActivity.class);
            intent.putExtra("b","b");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
  ```
  
  第二种
  
   ```
        public void zan(View view) {
                //模拟主线程的拦截
        //        doZan();
                //模拟子线程的拦截
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doZan();
        
                    }
                }).start();
        
            }
            //执行点赞的具体代码
            @CheckIfLoginAndLoginAndBackToContinue
            private void doZan() {
                Log.e("test","点赞 in "+Thread.currentThread().getName());
                ToastUtil.toast(MyApp.myApp,"点赞 in "+Thread.currentThread().getName());
            }
   ```
   当登录状态发生改变时调用
   ```//模拟主线程触发
      //                LoginUtil.login(true);
      
                      //模拟子线程触发
                      new Thread(new Runnable() {
                          @Override
                          public void run() {
                              LoginUtil.login(true);
                          }
                      }).start();
                      setResult(RESULT_OK);
                      finish();
   ```
   
 如此这般，在登录后会自动执行原有的逻辑代码
   
 混淆
 -keepattributes *Annotation*,Signature,Exceptions,InnerClasses,EnclosingMethod
 
 -keep om.lzy.login_library.annotation.** { *; }
 
 -dontwarn com.lzy.login_library.annotation.**
 
 -keepclasseswithmembernames class * { @com.lzy.login_library.annotation.* <methods>; }
 
   