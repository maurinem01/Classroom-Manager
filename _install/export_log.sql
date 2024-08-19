/*
This query merges two tables using the UNION operation:
	1. the original kumon_db.log table used by Classroom Manager v1.0
	2. the updated kumon_db.acuity_log used by Classroom Manager v1.1
	
To export to CSV using HeidiSQL:
	1. Click Tools > Export Grid Rows
	2. Under "Output target", choose "File"
	3. Click on the file folder icon and choose file destination
	4. Under "Row selection", choose "Complete"
	5. Click OK
*/

SELECT * FROM (
	SELECT s.student_id AS 'sid', 
		s.first_name AS 'fName', 
		s.last_name AS 'lName', 
		DATE(l.time_in) AS 'date', 
		TIME(l.time_in) AS 'in', 
		TIME(l.time_out) AS 'out'
		FROM student s 
	LEFT JOIN (
		SELECT student_id, 
			time_in, 
			time_out 
			FROM log
	) l ON s.student_id = l.student_id WHERE time_in IS NOT NULL
) t WHERE t.date >= CURDATE() AND t.date <= CURDATE()
UNION
SELECT * FROM (
	SELECT s.student_id AS 'sid', 
		s.first_name AS 'fName', 
		s.last_name AS 'lName', 
		DATE(l.time_in) AS 'date', 
		TIME(l.time_in) AS 'in', 
		TIME(l.time_out) AS 'out'
		FROM student s 
	LEFT JOIN (
		SELECT student_id, 
			time_in, 
			time_out 
			FROM acuity_log
	) l ON s.student_id = l.student_id WHERE time_in IS NOT NULL
) t WHERE t.date >= CURDATE() AND t.date <= CURDATE() 
ORDER BY date;



