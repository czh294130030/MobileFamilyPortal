package com.example.model;

public class KeyValue implements Comparable<KeyValue>{
	
	private Integer key;
	private String value;
	
	/*为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()*/
	@Override
	public String toString() {
	  return getValue();
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	/*根据Key排序（升序）*/
	@Override
    public int compareTo(KeyValue arg0) {
        return this.getKey().compareTo(arg0.getKey());
    }
}
