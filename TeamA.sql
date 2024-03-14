drop table member;
drop table hospitals;
drop table DISEASEs;
drop table DOCTORs;
drop table GUARDIANS;
drop table HOSPITALS;
drop table NOTICE;
drop table PATIENT;
drop table QUESTION;
drop table ADMIN;

CREATE TABLE member (
    member_id NUMBER PRIMARY KEY,
    guardian_id NUMBER NULL ,
    pw VARCHAR2(50) NULL ,
    name VARCHAR2(50) NOT NULL,
    phone VARCHAR2(15) NOT NULL,
    birth DATE NULL ,
    gender VARCHAR2(10) NULL ,
    blood VARCHAR2(10) NULL ,
    social VARCHAR2(4) DEFAULT 'x' NOT NULL  ,
    email VARCHAR2(50) NOT NULL,
    address VARCHAR2(50) NULL,
    address_detail VARCHAR2(30) NULL,
    alram VARCHAR2(4) DEFAULT 'y' NOT NULL ,
    role VARCHAR2(4) DEFAULT 'm' NOT NULL 
);

ALTER TABLE member add (token varchar2(400));
ALTER TABLE member
ADD CONSTRAINT fk_member_guardian_id
FOREIGN KEY (guardian_id)
REFERENCES member(member_id); -- �����ʿ�

CREATE SEQUENCE member_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER member_id_trigger
BEFORE INSERT ON member
FOR EACH ROW
BEGIN
    SELECT member_id_seq.NEXTVAL
    INTO :new.member_id
    FROM dual;
END;
--  ���� ����������, 
CREATE TABLE hospitals (
    hospital_id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    location VARCHAR2(100) NOT NULL,
    contact VARCHAR2(20)
);

CREATE SEQUENCE hospital_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER hospital_id_trigger
BEFORE INSERT ON hospitals
FOR EACH ROW
BEGIN
    SELECT hospital_id_seq.NEXTVAL
    INTO :new.hospital_id
    FROM dual;
END;

-- ���� ���� �ǻ� ��Ͻ�, ��� ���� �ְ�
CREATE TABLE doctors (
    doctor_id NUMBER PRIMARY KEY,
    name VARCHAR2(20) NOT NULL,
    specialty VARCHAR2(100),
    contact VARCHAR2(15),
    hospital_id NUMBER,
    CONSTRAINT fk_doctors_hospital_id
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(hospital_id)
);

CREATE SEQUENCE doctor_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER doctor_id_trigger
BEFORE INSERT ON doctors
FOR EACH ROW
BEGIN
    SELECT doctor_id_seq.NEXTVAL
    INTO :new.doctor_id
    FROM dual;
END;
DROP TABLE doctors;
DROP SEQUENCE doctor_id_seq;
DROP TRIGGER doctor_id_trigger;
-- �������̺��� �ǻ� �̸��� ����ϴ°����� ����
-- ��� ���� ���� ���̺�

CREATE TABLE member_hospitals (
    member_id NUMBER,
    hospital_id NUMBER,
    CONSTRAINT fk_member_hospitals_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id),
    CONSTRAINT fk_member_hospitals_hospital_id
        FOREIGN KEY (hospital_id)
        REFERENCES hospitals(hospital_id)
);
ALTER TABLE hospitals
MODIFY location VARCHAR2(400);
truncate table hospitals;
select * from hospitals;
ALTER TABLE member_hospitals
ADD doctor_name VARCHAR2(50);
-- ���� ���̺�
CREATE TABLE member_diseases (
    member_id NUMBER,
    disease_name VARCHAR2(100),
    description VARCHAR2(255),
    CONSTRAINT fk_member_diseases_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id)
);


-- ����͸� ���̺�
CREATE TABLE conditions (
    condition_id NUMBER PRIMARY KEY,
    condition_pulse NUMBER NOT NULL,
    condition_temperature NUMBER NOT NULL,
    condition_time DATE DEFAULT SYSDATE NOT NULL,
    member_id NUMBER NOT NULL,
    condition_accident CHAR(1) DEFAULT 'N' CHECK (condition_accident IN ('Y', 'N')),
    CONSTRAINT fk_conditions_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id)
);

CREATE SEQUENCE condition_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER condition_id_trigger
BEFORE INSERT ON conditions
FOR EACH ROW
BEGIN
    SELECT condition_id_seq.NEXTVAL
    INTO :new.condition_id
    FROM dual;
END;

-- ���޻��� �α�
drop table emergency;
CREATE TABLE emergency (
    log_id NUMBER PRIMARY KEY,
    time DATE DEFAULT SYSDATE NOT NULL,
    member_id NUMBER NOT NULL,
    category_id NUMBER NOT NULL unique,
    CONSTRAINT fk_emergency_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id)
);

CREATE SEQUENCE emergency_log_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER emergency_log_id_trigger
BEFORE INSERT ON emergency
FOR EACH ROW
BEGIN
    SELECT emergency_log_id_seq.NEXTVAL
    INTO :new.log_id
    FROM dual;
END;

CREATE TABLE emergency_type (
    category_id NUMBER PRIMARY KEY,
    title VARCHAR2(20) NOT NULL,
    content VARCHAR2(100) NOT NULL,
    CONSTRAINT fk_emergency_type_category_id
        FOREIGN KEY (category_id)
        REFERENCES emergency(category_id)
);
--��������
CREATE TABLE notice (
    notice_id NUMBER PRIMARY KEY,
    read_cnt NUMBER DEFAULT 0 NOT NULL ,
    title VARCHAR2(200) NOT NULL,
    content VARCHAR2(2000) NOT NULL,
    time DATE DEFAULT SYSDATE NOT NULL,
    admin_id number NOT NULL,
    CONSTRAINT fk_notice_admin_id
        FOREIGN KEY (admin_id)
        REFERENCES member(member_id)
);
CREATE SEQUENCE notice_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER notice_insert_trigger
BEFORE INSERT ON notice
FOR EACH ROW
BEGIN
    SELECT notice_id_seq.NEXTVAL
    INTO :new.notice_id
    FROM dual;
END;
-- �����ϴ� ����
CREATE TABLE faq (
    faq_id NUMBER PRIMARY KEY,
    title VARCHAR2(200) NOT NULL,
    content VARCHAR2(2000) NOT NULL,
    time DATE NOT NULL,
    member_id NUMBER NOT NULL,
    read_cnt NUMBER DEFAULT 0 NOT NULL,
     CONSTRAINT fk_faq_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id)
);

ALTER TABLE faq DROP COLUMN time;
ALTER TABLE faq DROP COLUMN read_cnt;

CREATE SEQUENCE faq_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER faq_insert_trigger
BEFORE INSERT ON faq
FOR EACH ROW
BEGIN
    SELECT faq_id_seq.NEXTVAL
    INTO :new.faq_id
    FROM dual;
END;
-- ���� �Խ���
CREATE TABLE category (
    category_id NUMBER PRIMARY KEY,
    name VARCHAR2(40) NOT NULL
);

CREATE SEQUENCE category_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER category_insert_trigger
BEFORE INSERT ON category
FOR EACH ROW
BEGIN
    SELECT category_id_seq.NEXTVAL
    INTO :new.category_id
    FROM dual;
END;

CREATE TABLE question (
    que_id NUMBER Primary key,
    title VARCHAR2(200) NOT NULL,
    content VARCHAR2(1000) NOT NULL,
    time DATE DEFAULT SYSDATE NOT NULL,
    member_id Number NOT NULL,
    secret VARCHAR2(4) DEFAULT 'n' NOT NULL ,
    category_id NUMBER NOT NULL,
    read_cnt NUMBER DEFAULT 0 NOT NULL ,
    CONSTRAINT fk_question_category_id
        FOREIGN KEY (category_id)
        REFERENCES category(category_id), -- ����, ���� ī�װ� ���̺��� �����ϵ��� �����ؾ� �մϴ�
    CONSTRAINT fk_question_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id)
);

CREATE SEQUENCE question_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER question_insert_trigger
BEFORE INSERT ON question
FOR EACH ROW
BEGIN
    SELECT question_id_seq.NEXTVAL
    INTO :new.que_id
    FROM dual;
END;
--�˶� �α�

CREATE TABLE alarmLog (
    alarm_id NUMBER PRIMARY KEY,
    title VARCHAR2(100) NOT NULL,
    content VARCHAR2(1000) NOT NULL,
    time DATE DEFAULT SYSDATE NOT NULL,
    state VARCHAR2(4) DEFAULT 'n' NOT NULL ,
    member_id number NOT NULL,
    CONSTRAINT fk_alarmLog_member_id
        FOREIGN KEY (member_id)
        REFERENCES member(member_id) -- ����, ���� ��� ���̺��� �����ϵ��� �����ؾ� �մϴ�
);

CREATE SEQUENCE alarm_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER alarmLog_insert_trigger
BEFORE INSERT ON alarmLog
FOR EACH ROW
BEGIN
    SELECT alarm_id_seq.NEXTVAL
    INTO :new.alarm_id
    FROM dual;
END;

--��� 

CREATE TABLE answer (
    ans_id NUMBER PRIMARY KEY,
    content VARCHAR2(1000) NOT NULL,
    que_id NUMBER NOT NULL,
    admin_id number,
    time DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT fk_answer_que_id
        FOREIGN KEY (que_id)
        REFERENCES question(que_id), -- question ���̺��� �����ϵ��� �����ؾ� �մϴ�
    CONSTRAINT fk_answer_admin_id
        FOREIGN KEY (admin_id)
        REFERENCES member(member_id) -- admin ���̺��� �����ϵ��� �����ؾ� �մϴ�
);
CREATE SEQUENCE answer_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER answer_insert_trigger
BEFORE INSERT ON answer
FOR EACH ROW
BEGIN
    SELECT answer_id_seq.NEXTVAL
    INTO :new.ans_id
    FROM dual;
END;

--���� ����
CREATE TABLE attach_file (
    file_id number PRIMARY KEY,
    name VARCHAR2(200) NOT NULL,
    path VARCHAR2(400) NOT NULL,
    time DATE DEFAULT SYSDATE NOT NULL,
    question_id NUMBER,
    NOTICE_ID NUMBER,
    CONSTRAINT fk_file_question_id
        FOREIGN KEY (question_id)
        REFERENCES QUESTION(QUE_ID), -- ����, ���� ��� ���̺��� �����ϵ��� �����ؾ� �մϴ�
    CONSTRAINT fk_file_notice_id
        FOREIGN KEY (NOTICE_ID)
        REFERENCES NOTICE(NOTICE_ID) -- ����, ���� �������� ���̺��� �����ϵ��� �����ؾ� �մϴ�
);

CREATE SEQUENCE file_id_seq
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;

CREATE OR REPLACE TRIGGER file_insert_trigger
BEFORE INSERT ON attach_file
FOR EACH ROW
BEGIN
    SELECT file_id_seq.NEXTVAL
    INTO :new.file_id
    FROM dual;
END;

commit;