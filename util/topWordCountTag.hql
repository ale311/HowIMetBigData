CREATE TABLE input (line STRING);
LOAD DATA LOCAL INPATH './exercise/data/Tag.txt' INTO TABLE input;
CREATE TABLE word_counts AS 
SELECT w.word, count(1) AS count FROM (SELECT explode(split(line, '\s')) AS word FROM input) w
GROUP BY w.word
ORDER BY w.word;