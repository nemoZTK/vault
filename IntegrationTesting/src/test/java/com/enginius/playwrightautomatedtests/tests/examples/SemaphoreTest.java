package com.enginius.playwrightautomatedtests.tests.examples;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) throws Exception {
		Semaphore s = new Semaphore(0);
		System.out.println("INIT");
		(new Thread() {
			@Override
			public void run() {
				System.out.println("WAIT");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				System.out.println("RELEASE");
				s.release();
			}
		}).start();

		System.out.println("ACQUIRE");
		s.acquire();
		System.out.println("DONE");
	}
}
