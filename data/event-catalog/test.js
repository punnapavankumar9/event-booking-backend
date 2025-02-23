export function checkMovieStatus() {
  client.test("check movie creation status", function () {
    client.assert(response.status === 200, "status == 200");
  })
}
