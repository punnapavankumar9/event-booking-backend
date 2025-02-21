AUTH_TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXZhbiIsImV4cCI6MTc0MDAwMTY3OCwiaWF0IjoxNzM5OTk4MDc4fQ.OHFPc1pT81AKVBrxJs6hP27_KweEufmqSYmyqNLunkk"

curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/thandel/thandel-et00384012-1705486794.avif"' \
--form 'posterImage=@"./movie-images/thandel/download.avif"' \
--form 'movie=@./movie-images/thandel/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/mufasa/mufasa-the-lion-king-et00396541-1734081980.avif"' \
--form 'posterImage=@"./movie-images/mufasa/download.avif"' \
--form 'movie=@./movie-images/mufasa/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/laila/laila-et00403539-1738664691.avif"' \
--form 'posterImage=@"./movie-images/laila/download.avif"' \
--form 'movie=@./movie-images/laila/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/captain-america/captain-america-brave-new-world-et00399765-1737957738.avif"' \
--form 'posterImage=@"./movie-images/captain-america/download.avif"' \
--form 'movie=@./movie-images/captain-america/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/Sankranthiki-Vasthunam/sankranthiki-vasthunam-et00418119-1731656543.avif"' \
--form 'posterImage=@"./movie-images/Sankranthiki-Vasthunam/download.avif"' \
--form 'movie=@./movie-images/Sankranthiki-Vasthunam/movie.json;type=application/json'



curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/Brahma-Anandam/brahma-anandam-et00430098-1739443663.avif"' \
--form 'posterImage=@"./movie-images/Brahma-Anandam/download.avif"' \
--form 'movie=@./movie-images/Brahma-Anandam/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/Kudumbasthan/kudumbasthan-et00429890-1737524358.avif"' \
--form 'posterImage=@"./movie-images/Kudumbasthan/download.avif"' \
--form 'movie=@./movie-images/Kudumbasthan/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/love-yapa/loveyapa-et00427632-1738580822.avif"' \
--form 'posterImage=@"./movie-images/love-yapa/download.avif"' \
--form 'movie=@./movie-images/love-yapa/movie.json;type=application/json'



curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/deva/deva-et00374104-1738145051.avif"' \
--form 'posterImage=@"./movie-images/deva/download.avif"' \
--form 'movie=@./movie-images/deva/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/sky-force/sky-force-et00371539-1737535327.avif"' \
--form 'posterImage=@"./movie-images/sky-force/download.avif"' \
--form 'movie=@./movie-images/sky-force/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/chhaava/chhaava-et00408691-1737623374.avif"' \
--form 'posterImage=@"./movie-images/chhaava/download.avif"' \
--form 'movie=@./movie-images/chhaava/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/sanam-teri-kasam/sanam-teri-kasam-et00036895-1730964847.avif"' \
--form 'posterImage=@"./movie-images/sanam-teri-kasam/download.avif"' \
--form 'movie=@./movie-images/sanam-teri-kasam/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/ddlj/dilwale-dulhania-le-jayenge-et00000652-1685508478.avif"' \
--form 'posterImage=@"./movie-images/ddlj/download.avif"' \
--form 'movie=@./movie-images/ddlj/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/sos/surya-s-o-krishnan-et00002203-1689661215.avif"' \
--form 'posterImage=@"./movie-images/sos/download.avif"' \
--form 'movie=@./movie-images/sos/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/orange/orange-telugu-et00005527-1738763120.avif"' \
--form 'posterImage=@"./movie-images/orange/download.avif"' \
--form 'movie=@./movie-images/orange/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/the-wild-robot/the-wild-robot-et00398665-1732271118.avif"' \
--form 'posterImage=@"./movie-images/the-wild-robot/download.avif"' \
--form 'movie=@./movie-images/the-wild-robot/movie.json;type=application/json'


curl --location 'http://localhost:8080/api/v1/movies' \
--header "Authorization: Bearer $AUTH_TOKEN" \
--form 'images=@"./movie-images/ramam-ragavam/ramam-raghavam-et00390589-1709978238.avif"' \
--form 'posterImage=@"./movie-images/ramam-ragavam/download.avif"' \
--form 'movie=@./movie-images/ramam-ragavam/movie.json;type=application/json'
