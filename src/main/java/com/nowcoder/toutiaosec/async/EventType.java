package com.nowcoder.toutiaosec.async;

public enum EventType {
	 LIKE(0),
	 COMMENT(1),
	 LOGIN(2),
	 MAIL(3),
	 SCORE(4);
	private int value;
	
	EventType(int value){
		this.value=value;
	}
}
