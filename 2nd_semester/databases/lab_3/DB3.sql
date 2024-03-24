CREATE TABLE IF NOT EXISTS "space_objects" (
  "id" serial PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "type" varchar(255) NOT NULL,
  "radius" integer CHECK ( "radius" > 0),
  "mass" integer CHECK ( "mass" > 0 ),
  "composition" varchar(255),
  "description" varchar(255)
);

CREATE TABLE IF NOT EXISTS "creatures" (
  "id" serial PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "description" varchar(255)
);

CREATE TABLE IF NOT EXISTS "SO_connections" (
  "SO_1st_id" integer NOT NULL REFERENCES "space_objects" ON DELETE CASCADE,
  "SO_2nd_id" integer NOT NULL REFERENCES "space_objects" ON DELETE CASCADE,
    PRIMARY KEY ("SO_1st_id", "SO_2nd_id"),
  "description" varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS "life_suitabilities" (
  "SO_id" integer REFERENCES "space_objects" ON DELETE CASCADE,
  "is_life_suitable" bool NOT NULL,
  "inhabitant_id" integer
);

CREATE TABLE IF NOT EXISTS "climates" (
  "SO_id" integer NOT NULL REFERENCES "space_objects" ON DELETE CASCADE,
  "average_temperature" float,
  "atmosphere_composition" varchar
);

CREATE OR REPLACE FUNCTION add_SO_moon()
RETURNS trigger LANGUAGE plpgsql AS $$
    DECLARE moon_id integer;
    BEGIN
    IF (new.type = 'Planet') THEN
        INSERT INTO "space_objects"("name", "type") VALUES
        (new.name || ' Moon', 'Moon') RETURNING id INTO moon_id;
        INSERT INTO "SO_connections"("SO_1st_id", "SO_2nd_id", description) VALUES
        (new.id, moon_id, '2nd is Moon for 1st.');
    END IF;
    RETURN new;
    END;
$$;

CREATE TRIGGER add_moon AFTER INSERT ON "space_objects"
    FOR EACH ROW EXECUTE PROCEDURE add_SO_moon();