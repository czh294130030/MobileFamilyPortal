package com.example.model;

public class KeyValue implements Comparable<KeyValue>{
	
	private Integer key;
	private String value;
	
	/*ΪʲôҪ��дtoString()�أ���Ϊ����������ʾ���ݵ�ʱ����������������Ķ������ַ���������£�ֱ�Ӿ�ʹ�ö���.toString()*/
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
	/*����Key��������*/
	@Override
    public int compareTo(KeyValue arg0) {
        return this.getKey().compareTo(arg0.getKey());
    }
}
