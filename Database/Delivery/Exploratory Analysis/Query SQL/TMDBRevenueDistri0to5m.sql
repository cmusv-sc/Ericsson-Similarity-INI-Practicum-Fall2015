SELECT (data->>'production_countries')::text,
   SUM(CASE WHEN 0 <= (data->>'revenue')::bigint AND (data->>'revenue')::bigint <= 1000000 THEN 1 ELSE 0 END) as COUNT_0_TO_1_Million,
   SUM(CASE WHEN 1000000 < (data->>'revenue')::bigint AND (data->>'revenue')::bigint <= 5000000 THEN 1 ELSE 0  END) as COUNT_0_TO_5_Million,
   SUM(CASE WHEN 5000000 < (data->>'revenue')::bigint AND (data->>'revenue')::bigint <= 10000000 THEN 1 ELSE 0  END) as COUNT_0_TO_10_Million,
   SUM(CASE WHEN 10000000 < (data->>'revenue')::bigint THEN 1 ELSE 0  END) as COUNT_10_Million
FROM import.tmdbtotal
GROUP by (data->>'production_countries')::text
ORDER BY count_0_to_5_million DESC
Limit 10;