package com.myboot.restapi;

import java.util.List;
import java.util.function.Consumer;

import org.hibernate.internal.build.AllowSysOut;
import org.junit.jupiter.api.Test;

public class LambdaTest { 
	@Test
	public void iterable() {
		List<String> myList = List.of("람다", "함수형", "스트림");
		
		//1 Annonymous Inner class로 만들
		myList.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				// TODO Auto-generated method stub
				System.out.print(t);
			}
		});

		System.out.println("22");
		myList.forEach(value -> {
			System.out.print(">");
			System.out.println(value);
		});
		System.out.println("33");
		myList.forEach(value -> 
			System.out.print(value)
		);
		
		System.out.println("44");
		myList.forEach(System.out::print);
	} 
}
