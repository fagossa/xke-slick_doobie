app="postgres-db"
if docker ps | awk -v app="app" 'NR>1{  ($(NF) == app )  }'; then
  docker stop "$app" && docker rm -f "$app"
fi