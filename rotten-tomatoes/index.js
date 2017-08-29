const express = require('express')  
const app = express()  
const port = 8080


app.param('movieId', function(request, response, next, movieId){
  
    if(movieId!="771373077") {
		response.end('Invalid movie id');
	}
});

app.get('/movie/:movieId', (request, response) => {
	
	var fs = require('fs');

	fs.readFile('take-home-movie.json', 'utf8', function (err, data) {
		if (err) throw err; // we'll not consider error handling for now
		var jsonData = JSON.parse(data);
				
	//	console.log("image url: " + jsonData.data.relationships.images.links.related);
		response.send("image url: " + jsonData.data.relationships.images.links.related)
	});

	
})

app.listen(port, (err) => {  
  if (err) {
    return console.log('something bad happened', err)
  }

  console.log(`server is listening on ${port}`)
})