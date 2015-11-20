SELECT Count(*)
FROM import.tmdbtotal 
WHERE (data->>'poster_path')::text is not NULL