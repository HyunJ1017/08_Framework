package edu.kh.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder // 빌더 패턴을 위한 메서드 자동완성 어노테이션
			// 빌더 패턴 : 특정 값으로 초기화된 객체를 쉽게 생성하기 위한 메서드를 만드는 패턴
public class Student {
	
	private String studentNo;
	private String name;
	private int age;

	private List<String> hobbyList;
}
