version: "3.1"
services:
  proxy:
    image: nginx:1.15-alpine
    ports:
      - "8080:80"
    volumes:
      - ./proxy/conf/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./proxy/conf/domain.conf:/etc/nginx/domain.conf:ro
    depends_on:
      - dienst
      - datenbank

  dienst:
    build: .
    ports:
      - "8081:8081"
    environment:
      - DB_HOST=db
      - DB_PORT=1521
      - DB_USER=perfectlybalanced
      - DB_PASSWORD=whatdiditcost
    links:
      - datenbank

  datenbank:
    container_name: datenbank
    image: oscarfonts/h2
    volumes:
      - ./datenbank:/opt/h2-data
    ports:
      - "1521:1521"
    environment:
      - H2_DATABASE=datenbank
      - H2_USER=perfectlybalanced
      - H2_PASSWORD=whatdiditcost
      - H2_ROOT_PASSWORD=everything