SELECT "Н_ЛЮДИ"."ОТЧЕСТВО", "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД", "Н_УЧЕНИКИ"."ИД" FROM
    "Н_ЛЮДИ" RIGHT JOIN "Н_ОБУЧЕНИЯ" ON "Н_ЛЮДИ"."ИД" = "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД"
    RIGHT JOIN "Н_УЧЕНИКИ" USING("ЧЛВК_ИД")
    WHERE "Н_ЛЮДИ"."ОТЧЕСТВО" = 'Георгиевич'
    AND CAST("Н_ОБУЧЕНИЯ"."НЗК" AS INT) > 933232;