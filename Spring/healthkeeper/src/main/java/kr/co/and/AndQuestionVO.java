package kr.co.and;

import lombok.Data;

@Data
public class AndQuestionVO {

    private String QUE_ID, TITLE, CONTENT, TIME, MEMBER_ID, SECRET, CATEGORY_ID, READ_CNT, Name;
    private AndAnswerVO answer;
}
