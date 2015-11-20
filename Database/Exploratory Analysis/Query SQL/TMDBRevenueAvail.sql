SELECT Count(*)
FROM import.tmdbtotal
WHERE (data->>'revenue')::bigint <> 0