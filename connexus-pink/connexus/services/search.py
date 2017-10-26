import jinja2
import webapp2
import urllib2
import json
from connexus.common import *
from connexus.ndb_model import *
from google.appengine.api import search

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START Search]
class Search(webapp2.RequestHandler):
    def post(self):
        search_string = self.request.get('search_string')
        search_string = search_string.replace('#', '')
        self.redirect('/search?search=' + search_string)

    def get(self):
        original_search_string = self.request.get('search')
        search_string = self.request.get('search').lower()

        index = search.Index(name='connexus_search')
        num_results = 0
        found_streams = []
        if search_string:
            search_results = get_streams_from_search(search_string)
            num_results = search_results.number_found
            for index, doc in enumerate(search_results):
                doc_id = doc.doc_id
                stream = ndb.Key(Stream, int(doc_id)).get()
                found_streams.append(stream)
                # if there are more than 5 then do not display them as per spec
                if index == 4:
                    break
        template_values = {
            'page': 'Connex.us',
            'search_string': original_search_string,
            'num_results': num_results,
            'found_streams': found_streams,
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
       
        template = JINJA_ENVIRONMENT.get_template('search.html')
        self.response.write(template.render(template_values))
# [END Search]

# [START MobileSearch]
class MobileSearch(webapp2.RequestHandler):
    def post(self):
        search_string = self.request.get('search_string')
        search_string = search_string.replace('#', '')
        self.redirect('/search?' + search_string)

    def get(self):
        original_search_string = self.request.get('search')
        search_string = self.request.get('search').lower()
        search_page = int(self.request.get('p'))

        index = search.Index(name='connexus_search')
        num_results = 0
        found_streams = []
        if search_string:
            search_results = get_streams_from_search(search_string)
            num_results = search_results.number_found
            for index, doc in enumerate(search_results):
                doc_id = doc.doc_id
                stream = ndb.Key(Stream, int(doc_id)).get()
                found_streams.append(stream)

        MAX_SEARCH_RESULT_PER_PAGE = 8
        low_range = 0 + MAX_SEARCH_RESULT_PER_PAGE*(search_page-1)
        high_range = MAX_SEARCH_RESULT_PER_PAGE + MAX_SEARCH_RESULT_PER_PAGE*(search_page-1)
        
        next_page = 'false'
        if high_range < num_results:
            next_page = 'true'

        if search_page != 1:
            prev_page = 'true'
        else:
            prev_page = 'false'

        template_values = {
            'page': 'Connex.us',
            'search_string': original_search_string,
            'num_results': num_results,
            'search_page': search_page,
            'found_streams': found_streams[low_range : high_range],
            'next_page': next_page,
            'next_cursor': search_page + 1,
            'prev_page': prev_page,
            'prev_cursor': search_page - 1,
        }
        self.response.write(json.dumps([template_values], cls=MyJsonEncoder))
# [END MobileSearch]

class AutocompleteSearch(webapp2.RequestHandler):
    def get(self):
        search_string = urllib2.unquote(self.request.query_string).lower()

        found_matches = get_json_streams_from_search(search_string)
        print("JSON: {}".format(found_matches))
        
        self.response.write(found_matches)

# from google's snippets
def delete_all_in_index(index):
    # index.get_range by returns up to 100 documents at a time, so we must
    # loop until we've deleted all items.
    while True:
        # Use ids_only to get the list of document IDs in the index without
        # the overhead of getting the entire document.
        document_ids = [
            document.doc_id
            for document
            in index.get_range(ids_only=True)]

        # If no IDs were returned, we've deleted everything.
        if not document_ids:
            break

        # Delete the documents for the given IDs
        index.delete(document_ids)

def get_streams_from_search(search_string):
    try:
        index = search.Index('connexus_search')
        # removes all stale documents in index
        delete_all_in_index(index)
        streams = Stream.query()
        for s in streams:
            tags = ''.join(str(e).lower() for e in s.tags)
            doc = search.Document(doc_id=str(s.key.id()),
                                  fields=[search.TextField(name='name', value=s.name.lower()),
                                          search.TextField(name='tags', value=tags),
                                          ]
                                  )
            search.Index(name='connexus_search').put(doc)

        search_query = search.Query(query_string=search_string)
        search_results = index.search(search_query)
        return search_results

    except search.Error:
        # todo add error handler
        pass

def get_json_streams_from_search(search_string):
        try:
            index = search.Index('connexus_search')
            # removes all stale documents in index
            delete_all_in_index(index)
            streams = Stream.query()
            for s in streams:
                tags = ''.join(str(e).lower() for e in s.tags)
                doc = search.Document(doc_id=str(s.key.id()),
                                      fields=[search.TextField(name='name', value=s.name.lower()),
                                              search.TextField(name='tags', value=tags),
                                              ]
                                      )
                search.Index(name='connexus_search').put(doc)

            search_query = search.Query(query_string=search_string)
            search_results = index.search(search_query)

            search_results = Stream.query().fetch()
            found_matches = []
            for stream in search_results:
                print("Stream_n={0} string={1} tags={2}".format(stream.name, search_string, stream.tags))
                for tag in stream.tags:
                    if search_string.lower() in tag.lower().strip() and not tag.lower().strip() in found_matches:
                        found_matches.append(tag)

                if search_string.lower() in stream.name.lower():
                    if not stream.name.lower() in found_matches:
                        found_matches.append(stream.name)
            print("before sort={}".format(found_matches))

            found_matches.sort() # sort alphabetically as per spec
            print("after sort={}".format(found_matches))
            return json.dumps(found_matches[:20]) # only get first 20 matches as per spec

        except search.Error:
            # todo add error handler
            pass
