package kr.co.and.firebase;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RequestDTO {
    private String CATEGORY_ID;
    private String MEMBER_ID;
    private String name;
    private String GUARDIAN_ID;
}
