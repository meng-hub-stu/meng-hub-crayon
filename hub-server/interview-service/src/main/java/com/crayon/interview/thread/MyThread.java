package com.crayon.interview.thread;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/7/9 11:31
 **/
public class MyThread extends Thread{
    @Override
    public void run() {
        System.out.println("run");
    }
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
    }
}
