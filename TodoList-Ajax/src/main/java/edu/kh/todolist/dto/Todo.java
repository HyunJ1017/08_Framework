package edu.kh.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo  {
						
	private int 		todoNo;
	private String 	todoTitle;
	private String 	todoDetail;
	private int 		todoComplete;
	private String 	regDate;
	
	
}