# APITV
API middleware que expone servicios con los que se pueden consultar transmisiones o shows que se encuentran en otra plataforma, en otras palabras es un puente para no consultar directamente en la plataforma original

# Probar la consulta de shows
# A- Endpoint search

Petición GET a tu middleware local: http://localhost:8080/api/v1/shows?search_query=man

# Consultar show por id
#B- Endpoint show

Realiza una petición GET enviando el ID del show en la URL:http://localhost:8080/api/v1/shows/139 (ID correspondiente a la serie en TVMaze).


# B- Endpoint show
se actualizo para que consulte en mongo db si ya existe el show que se busca

# C- Endpoint comments:
Envia una petición POST mediante Postman o tu cliente HTTP de preferencia:URL: http://localhost:8080/api/v1/shows/analisis

Cuerpo de la peticion
{
  "show_id": 139,
  "comment": "Una excelente serie de comedia y drama de HBO.",
  "rating": 5
}