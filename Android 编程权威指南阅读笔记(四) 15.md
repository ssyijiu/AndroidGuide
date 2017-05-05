# Android 编程权威指南阅读笔记

## 第15章：隐式 Intent 

- 使用隐式 intent，只需要告诉操作系统我们想要做什么，操作系统就会去启动能够胜任工作任务的activity。如果找到多个符合的activity，用提供一个可选的应用列供用户选择。

- 隐式 Intent 的组成：
  - action：要执行的动作。例如，要访问某个 URL，可以使用 `Intent.ACTION_ VIEW` ，要发行邮件，可以使用 `Intent.ACTION_SEND`。
  - data：携带的数据
    - mimeType 数据类型
    - scheme 数据协议 

- 隐式 Intent 也可以使用 putExtra 来传递数据

- ```java
  // 设置意图为 ACTION_SEND, 向其他 Activity 发送数据
  Intent intent = new Intent(Intent.ACTION_SEND);

  intent.setType("text/plain");  // 设置 mimeType 
  intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());  // 设置文本

  String subject = getString(R.string.crime_report_subject);
  intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 设置主题
  //  Intent.createChooser(intent, subject) 设置选择器标题
  startActivity(Intent.createChooser(intent, subject));  
  ```

  效果如图：

  ![](http://obe5pxv6t.bkt.clouddn.com/send_intent.jpg)






- 读取联系人

  ```java
  // 1. startActivityForResult
  final Intent pickContact = new Intent(Intent.ACTION_PICK,
              ContactsContract.Contacts.CONTENT_URI);
  startActivityForResult(pickContact, REQUEST_CONTACT);

  // 2. onActivityResult
  @Override public void onActivityResult(int requestCode, int resultCode, final Intent data) {

         if (requestCode == REQUEST_CONTACT && data != null) {
              Uri contactUri = data.getData();
              // Specify which fields you want your query to return
              // values for.
              String[] queryFields = new String[] {
                  ContactsContract.Contacts.DISPLAY_NAME
              };
              // Perform your query - the contactUri is like a "where"
              // clause here
              Cursor cursor = context.getContentResolver()
                  .query(contactUri, queryFields, null, null, null);

              try {
                  // Double-check that you actually got results
                  if (cursor == null || cursor.getCount() == 0) {
                      return;
                  }

                  // Pull out the first column of the first row of data -
                  // that is your suspect's name.
                  cursor.moveToFirst();
                  final String suspect = cursor.getString(0);

                  RealmUtil.transaction(new Realm.Transaction() {
                      @Override public void execute(Realm realm) {
                          crime.suspect = suspect;
                      }
                  });
                  btnChooseSuspect.setText(suspect);
              } finally {
                  IOUtil.close(cursor);
              }
          }
      }
  ```

  ​