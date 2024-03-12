insert INTO conditions (CONDITION_PULSE, CONDITION_TEMPERATURE, CONDITION_ACCIDENT, MEMBER_ID)
		values (1,1,'N',2);
        
        select column_name, constraint_name from user_cons_columns
        where table_name = 'CONDITIONS';