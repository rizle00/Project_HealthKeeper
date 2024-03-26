select * from category
		where category_id < 6
        
insert into alarm_type (title, content) values('공지사항','새로운 공지사항이 등록되었습니다');
insert into alarm_type (title, content) values('답변','질문에 대한 답변이 등록되었습니다');
commit;
select 
SELECT * FROM member WHERE member_id IN (
    SELECT member_id FROM question WHERE QUE_ID = 3
);

select alarm_id, time, state , title,content From AlarmLog 
left join ALARM_TYPE  using ( category_id)
where member_id = 2 and state ='n';

UPDATE Alarmlog SET state='n' WHERE MEMBER_ID=2 and Alarm_id=2
commit;
select * from member order by member_id desc;
select * from alarm_type;
insert into Alarmlog (category_id, member_id) values (7,86);
insert into Alarmlog (category_id, member_id) values (8,86);
insert into Alarmlog (category_id, member_id) values (9,86);
insert into Alarmlog (category_id, member_id) values (10,86);
insert into Alarmlog (category_id, member_id) values (11,86);
insert into Alarmlog (category_id, member_id) values (13,86);
insert into Alarmlog (category_id, member_id) values (14,86);
commit;
select * from alarmlog where member_id =86;

select alarm_id, time, state , title,content From AlarmLog
		left join ALARM_TYPE  using ( category_id)
	where member_id = 86 and state = 'n';
    
    SELECT a.content answer_content , q.*, a.*, m.name
		FROM(
		SELECT row_number() over(ORDER BY time DESC) no , q.*
		FROM question q
		) q
		LEFT outer JOIN ANSWER a ON q.QUE_ID = a.QUE_ID
		LEFT OUTER JOIN Member m on q.member_id = m.member_id
        order by no;
 select * from question order by 1;
 select * from answer order by 1;
 insert into answer (content, que_id, admin_id) values('테스트 답변',388,2);
 delete from answer where ans_id = 18
 ;
 select * from member order by member_id desc;
 commit;
 SELECT CONDITION_PULSE, condition_time, CONDITION_TEMPERATURE
		FROM CONDITIONS c inner join( select * from member where guardian_id = 94) m on c.member_id = m.member_id
		WHERE 
       condition_time = (
			SELECT MAX(condition_time)
			FROM CONDITIONS
			WHERE member_id = 86
		);
        select * from member where member_id =95;
        update member set token = 'frCWWgPuRwaohASrlB_kDB:APA91bF3lBDo_q_azw0yNtLnHGlTYA5lWXnIM5-zuVLuiWTbfI7-sdYJosOWYT1J8UEJqJeootvom6ICRqFpkTCpNU7bCR75bIC_OGCU1thAtzPsFTvcH8QlnEESQEWj9WkWjYp-TiAo'
        where member_id =95;
        commit;
    delete from member where member_id = 94;
    update member set phone='010-3206-7276' where member_id =95;
    
    commit;
    select * from member where member_id = 56;
    