/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.quduo.pixel.interfaces.android.common;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ActiveTask {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //实现一个implements Callable接口的匿名类
        //这个相当于ACE_Method_Object
        Callable<String> call = new Callable<String>() {
            public String call() throws Exception {
                //如果线程sleep了，请求使用isDone来查询任务是否被完成
                //结果居然会是ture，请求线程真正取结果的时候就会被阻塞了
                Thread.sleep(1000 * 6);

                //做乘法来延时
                //这样isDone就会返回false了
                /*for(int i = 0; i < 1000; ++i) {
                    int ret = i * i;
                }*/

                System.out.println("(" + Thread.currentThread().getName() + ") handle the tasks");

                return "ecy :-)";
            }
        };

        //创建线程池
        //这里创建的线程池的数量和使用activate的效果应该相似
        final ExecutorService exec = Executors.newFixedThreadPool(3);

        //这个相当于ACE_Future
        //ACE的主动模式中还需要使用ACE_Activation_Queue来保存ACE_Method_Object对象
        //程序员还得自己手动来操作这个队列，从中取出对象然后执行它
        //Java简化了这个操作，不过同时也就降低了灵活性，执行任务的顺序应该只能按照submit的
        //顺序来做。
        //可以多生成几个对象，然后将它们submit进去，这样就可以看到它们被不同的线程池中
        //不同的线程来处理
        Future<String> task = exec.submit(call);

        //这个相当于ACE_Future::ready
        if (task.isDone()) {
            System.out.println("not completed");
        } else {
            System.out.println("has completed");
        }

        System.out.println("(" + Thread.currentThread().getName() + ") wait for the task be compelete");

        //这个相当于ACE::get，如果线程没有完成，请求线程将被阻塞，可以加一个超时时间
        String str = task.get();
        System.out.println("now completed, the result is : " + str);
        exec.shutdown();
    }
}
