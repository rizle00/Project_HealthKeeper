delete from member where member_id = 92
;
select member_id, guardian_id, email, role from member order by member_id desc
;
commit;
select * from category;
INSERT INTO question ( TITLE, CONTENT, MEMBER_ID, SECRET, CATEGORY_ID)
      VALUES ( 'aaa', 'testasdcas', 86, 'y', 1);
      commit;

update member SET role = 'patient' where member_id = 86;
update member SET role = 'guardian' where member_id = 94;

UPDATE MEMBER SET token='dLFqJnOtSkiO8fx-bngRk9:APA91bG3-Xb-DF3BNIQtR65Uy4x2smS5FfZJ_TIrJ19svYLRVdhwv3m-mjtY652Sg9o0gpsmECflC_G13IkyJsLn7Wsn9o8xpzOstwmVUwI4lyIB5cHusTbQpMikTXZEbsTflysYfJ3w' WHERE MEMBER_ID=80;