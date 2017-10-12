$(document).ready(function(){
  $( function() {
    var cache = {};
    $( "#search" ).autocomplete({
      autoFocus: true,
      minLength: 1,
      source: function( request, response ) {
        var term = request.term;
        console.log(term)
        if ( !cache ) {
          return;
        }
        if ( term in cache ) {
          response( cache[ term ] );
          return;
        }
 
        $.getJSON( "/autocomplete_search", request.term, function( data ) {
          console.log(data.toString())
          console.log(request.term)
          cache[ term ] = data;
          console.log(cache);
          console.log(cache);
          response( data );
        });
      }
    });
  } );
});