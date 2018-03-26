package com.company.funda.erp.util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class PrintUtil {

	
	/**
	 * 
	 * @param objects
	 * @return Person@182f0db[name=John Doe,age=33,smoker=false]
	 */
	public static String print(Object ... objects) {
		return getTogather(ToStringStyle.DEFAULT_STYLE, objects).toString();
	}
	
	/**
	 * 
	 * @param objects
	 * @return Person@182f0db[
			   name=John Doe
			   age=33
			   smoker=false
			 ]
	 */
	public static String printMultiLine(Object ... objects) {
		return getTogather(ToStringStyle.MULTI_LINE_STYLE, objects).toString();
	}
	
	/**
	 * 
	 * @param objects
	 * @return Person@182f0db[John Doe,33,false]
	 */
	public static String printNoFieldName(Object ... objects) {
		return getTogather(ToStringStyle.NO_FIELD_NAMES_STYLE, objects).toString();
	}
	
	/**
	 * 
	 * @param objects
	 * @return Person[name=John Doe,age=33,smoker=false]
	 */
	public static String printShort(Object ... objects) {
		return getTogather(ToStringStyle.SHORT_PREFIX_STYLE, objects).toString();
	}
	
	/**
	 * 
	 * @param objects
	 * @return John Doe,33,false
	 */
	public static String printSimple(Object ... objects) {
		return getTogather(ToStringStyle.SIMPLE_STYLE, objects).toString();
	}
	
	private static StringBuilder getTogather(ToStringStyle style,Object ... objects) {
		
		StringBuilder sb = new StringBuilder();
		if(null != objects) {
			for(Object obj:objects) {
				sb.append("\n ****** \n").append(ToStringBuilder.reflectionToString(obj, style)).append("\n ****** \n");
			}
		}
		return sb;
	}

	public static void main(String ...strings) {
		class Person{
			private String name;
			private int age;
			Person(String name,int age){
				this.name = name;
				this.age = age;
			}
		}
		
		Person t1 = new Person("Howard",40);
		Person t2 = new Person("Jeremy",35);
		Person t3 = new Person("Lina",35);
		Person t4 = new Person("Jerry",30);
		
		System.out.println("printMultiLine --- \n"+printMultiLine(t1,t2,t3,t4));
		System.out.println("printNoFieldName --- \n"+printNoFieldName(t1,t2,t3,t4));
		System.out.println("printShort --- \n"+printShort(t1,t2,t3,t4));
		System.out.println("printSimple --- \n"+printSimple(t1,t2,t3,t4));
	}

}
