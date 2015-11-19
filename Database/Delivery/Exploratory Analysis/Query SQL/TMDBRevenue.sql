SELECT Count(*)
FROM import.tmdbtotal
WHERE (data->>'revenue')::text = '0'