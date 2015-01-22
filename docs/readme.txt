CN=Java Duke, OU=Java Software Division, O=Sun Microsystems Inc, C=US
cn=Common Name, ou=Organizational Unit, o=Organization, c=Country

EditText光标在右边，输入内容从右往左：
android:gravity="center_vertical|right"
或者
editText.setGravity(Gravity.RIGHT);

android:numeric="integer"
设置只能输入整数，如果是小数则是：decimal


Graphics

IntentService
activity、 intent 、intent filter、service、Broadcase、BroadcaseReceiver

Handler, Looper, MessageQueue和Thread
Android消息处理机制(Handler、Looper、MessageQueue与Message)

1、删除所有的 .bak 后缀：
rename 's/\.bak$//' *.bak

2、把 .jpe 文件后缀修改为 .jpg：
rename 's/\.jpe$/\.jpg/' *.jpe

3、把所有文件的文件名改为小写：
rename 'y/A-Z/a-z/' *
4、将 abcd.jpg 重命名为 abcd_efg.jpg：
for var in *.jpg; do mv "$var" "${var%.jpg}_efg.jpg"; done
5、将 abcd_efg.jpg 重命名为 abcd_lmn.jpg：
for var in *.jpg; do mv "$var" "${var%_efg.jpg}_lmn.jpg"; done
6、把文件名中所有小写字母改为大写字母：
for var in `ls`; do mv -f "$var" `echo "$var" |tr a-z A-Z`; done
7、把格式 *_?.jpg 的文件改为 *_0?.jpg：
for var in `ls *_?.jpg`; do mv "$var" `echo "$var" |awk -F '_' '{print $1 "_0" $2}'`; done
8、把文件名的前三个字母变为 vzomik：
for var in `ls`; do mv -f "$var" `echo "$var" |sed 's/^.../vzomik/'`; done
9、把文件名的后四个字母变为 vzomik：
for var in `ls`; do mv -f "$var" `echo "$var" |sed 's/....$/vzomik/'`; done