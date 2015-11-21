SELECT Count(*)
FROM import.tmdbtotal 
WHERE (data->>'budget')::int = 0