package edu.kh.todolist.dto;

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
@Builder
@ToString
public class Todo {
	
	private int listNo;
	private String todoTitle;
	private String todoDetail;
	private char complete;
	private String regDate;
	private String color;

}
