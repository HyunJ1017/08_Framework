package edu.kh.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Sub {
	
	private int subjectNo;
	private int listNo;
	private String subjectTitle;
	private String subjectDetail;
	private char complete;
/*
 * SUBJECT_NO
 * LIST_NO
 * SUBJECT_TITLE
 * SUBJECT_DETAIL
 * COMPLETE
 */

}
