myinput = LOAD './exercise/data/Utente.txt' USING TextLoader() as (myword:chararray);
words = FOREACH myinput GENERATE FLATTEN(TOKENIZE(*));
grouped = GROUP words BY $0;
counts = FOREACH grouped GENERATE group as word, COUNT(words) as count;
sorted = ORDER counts BY count DESC;
store sorted into './exercise/pigoutput' using PigStorage(); 

