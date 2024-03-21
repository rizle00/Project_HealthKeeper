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
