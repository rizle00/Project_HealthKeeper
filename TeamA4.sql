select * from category
		where category_id < 6
        
insert into alarm_type (title, content) values('��������','���ο� ���������� ��ϵǾ����ϴ�');
insert into alarm_type (title, content) values('�亯','������ ���� �亯�� ��ϵǾ����ϴ�');
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
