SELECT Count(*)
FROM import.tmdbtotal
WHERE (data->>'overview')::text IS NOT NULL