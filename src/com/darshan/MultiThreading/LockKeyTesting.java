package com.darshan.MultiThreading;

/*
Any thread T1 which executes a static synchronized method will acquire class object key on class object. 
So any other thread trying to execute the same method or any other static synchronized method in that class will wait for T1.

Any thread T1 which executes a NON static synchronized method will acquire instance key. 
So any other thread trying to execute the same method or any other non static method in the same instance will 
have to wait for T1. Threads trying to execute static sync methods wouldn’t wait for T1 as they will get the key 
from the class object. Also, threads on a different instance of the same class wouldn’t wait either as they will 
have that instance specific key.
*/
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class LockKeyTesting {

	public static void main(String[] args) throws InterruptedException {

		A object1 = new A();
		Runnable r1 = () -> object1.nonStaticM1("object1"); // This will use instance object key.
		Runnable r2 = () -> object1.nonStaticM2("object1"); // This will wait for r1 to complete.
		Runnable r3 = () -> object1.staticM("object1");  	// This won't wait for r1 to complete as it will use class object key. object1.staticM or A.staticM will both use class object key only.

		A object2 = new A();
		Runnable r4 = () -> object2.nonStaticM1("object2"); // This won't wait for r1 or r2.
		Runnable r5 = () -> object2.nonStaticM2("object2"); // This will wait for r4.
		Runnable r6 = () -> object2.staticM("object2");		// This will wait for r3 to complete.

		Thread t1 = new Thread(r1);
		t1.start();
		Thread.sleep(200);
		
		Thread t2 = new Thread(r2);
		t2.start();
		Thread.sleep(200);

		Thread t3 = new Thread(r3);
		t3.start();
		Thread.sleep(200);

		Thread t4 = new Thread(r4);
		t4.start();
		Thread.sleep(200);

		Thread t5 = new Thread(r5);
		t5.start();
		Thread.sleep(200);

		Thread t6 = new Thread(r6);
		t6.start();
		Thread.sleep(200);

		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		t6.join();
	}
}

class A {

	private Object key1 = new Object();
	private Object key2 = new Object();
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("sHH:mm:ss.SSS");
	

	public static synchronized void staticM(String objName) {
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] I am in static synchronized method "
				+ objName + ".staticM");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] End");
	}

	public synchronized void nonStaticM1(String objName) {
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] I am in non static synchronized method "
				+ objName + ".nonStaticM1");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] End");
	}

	public synchronized void nonStaticM2(String objName) {
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] I am in non static synchronized method "
				+ objName + ".nonStaticM2");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(dtf.format(LocalDateTime.now()) + " [" + Thread.currentThread().getName() + "] End");
	}
}
