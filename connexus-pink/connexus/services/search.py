import os

import jinja2
import webapp2
from google.appengine.api import search

import urllib2

from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

# [START search_page]
class Search(webapp2.RequestHandler):

    def post(self):
        search_string = self.request.get('search_string')


        self.redirect('/search?' + search_string)


    def get(self):
        search_string = urllib2.unquote(self.request.query_string)

        index = search.Index(name='connexus_search')
        num_results = 0
        found_streams = []
        if search_string:
            try:
                index = search.Index('connexus_search')
                streams = Stream.query()
                for s in streams:
                    key = str(s.key)
                    print("s={0} key={1}".format(s, key))
                    doc = search.Document(doc_id=str(s.key.id()),
                                          fields=[search.TextField(name='name', value=s.name)]
                                         )
                    search.Index(name='connexus_search').put(doc)

                search_query = search.Query(query_string=search_string)
                search_results = index.search(search_query)
                print("Search Results={}".format(search_results))
                num_results = search_results.number_found
                for doc in search_results:
                    doc_id = doc.doc_id
                    found_streams.append(ndb.Key(Stream, int(doc_id)).get())
                    fields = doc.fields
                    print("doc_id={0} fields={1}".format(doc_id, fields)) 
            
            except search.Error:
                print("Caught a search Error")


        template_values = {
            'page': "Search",
            'search_string': search_string,
            'num_results': num_results,
            'found_streams': found_streams,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
       
        template = JINJA_ENVIRONMENT.get_template('search.html')
        self.response.write(template.render(template_values))

# [END search_page]
