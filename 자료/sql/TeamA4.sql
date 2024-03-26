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
    select * from member where member_id = 95;
    select * from question order by que_id desc;
    update QUESTION set  secret = 'n' where que_id = 393;
        commit;
    insert into ANSWER (que_id, content, admin_id) values (397,'요즘 날씨가 따뜻해져서 나들이를 다니는 것이 기분 좋은데, 특히 공원에서 산책하는 것을 즐깁니다. 나무 사이로 비치는 햇빛과 바람에 스며드는 시원함이 정말 상쾌하죠. ',2);
    update faq set content = ' 대통령은 국가의 안위에 관계되는 중대한 교전상태에 있어서 국가를 보위하기 위하여 긴급한 조치가 필요하고 국회의 집회가 불가능한 때에 한하여 법률의 효력을 가지는 명령을 발할 수 있다.' where faq_id = 26;
    select * from member order by member_id desc;
    ALTER table member
modify column role  SET DEFAULT 'patient';
delete from member where member_id in(96,97,98);
commit;
select * from QUESTION order by que_id desc;
update notice set content = '대통령은 국가의 안위에 관계되는 중대한 교전상태에 있어서 국가를 보위하기 위하여 긴급한 조치가 필요하고 국회의 집회가 불가능한 때에 한하여 법률의 효력을 가지는 명령을 발할 수 있다.' where notice_id = 68;
update faq set content = '요즘 날씨가 따뜻해져서 나들이를 다니는 것이 기분 좋은데, 특히 공원에서 산책하는 것을 즐깁니다. 나무 사이로 비치는 햇빛과 바람에 스며드는 시원함이 정말 상쾌하죠. 함께하는 가족과의 소중한 시간도 이렇게 만족스럽습니다.' where faq_id = 24;
commit;