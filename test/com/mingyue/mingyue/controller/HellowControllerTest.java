package com.mingyue.mingyue.controller;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class HellowControllerTest {

    @Test
    public void test() {
        final Integer[] i = {0};
        new Thread(() -> {
            synchronized (i) {
                while (i[0] < 10) {
                    i[0]++;
                    System.out.println(Thread.currentThread().getName() + "--" + i[0]);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (i) {
                while (i[0] < 10) {

                    i[0]++;
                    System.err.println(Thread.currentThread().getName() + "--" + i[0]);

                    try {
                        i.wait(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public synchronized void print(Integer i) {
        System.out.println(i);
    }

}