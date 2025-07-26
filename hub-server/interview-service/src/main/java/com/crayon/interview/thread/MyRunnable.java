package com.crayon.interview.thread;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/7/9 11:32
 **/
public class MyRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("run");
    }
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
