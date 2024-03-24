CREATE TABLE temp (
    from_name varchar(255),
    description varchar(255),
    to_name varchar(255),
    relation_id int
);

INSERT INTO temp(relation_id, description, from_name, to_name)
    SELECT relation_id, description, ch1.name, ch2.name FROM relations
        INNER JOIN characters as ch1 ON relations.from_id = ch1.character_id
        INNER JOIN characters as ch2 ON relations.to_id = ch2.character_id;

SELECT * FROM temp;