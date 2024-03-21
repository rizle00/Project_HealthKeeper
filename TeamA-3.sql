SELECT q.*, a.*, m.name
        FROM(
        SELECT row_number() over(ORDER BY time DESC) no , q.*
        FROM question q
        ) q
        LEFT outer JOIN ANSWER a ON q.QUE_ID = a.QUE_ID
        LEFT OUTER JOIN Member m on q.member_id = m.member_id;
        
        select condition_pulse, condition_time from conditions where member_id = 1;
       SELECT CONDITION_PULSE, condition_time, CONDITION_TEMPERATURE
		FROM CONDITIONS
		WHERE member_id = 2
		  AND condition_time = (
			SELECT MAX(condition_time)
			FROM CONDITIONS
			WHERE member_id = 2
		);
insert into 
CONDITIONS
(CONDITION_PULSE, CONDITION_TEMPERATURE, CONDITION_ACCIDENT, MEMBER_ID) 
values (100, 36,'N', 2);
        
      commit;