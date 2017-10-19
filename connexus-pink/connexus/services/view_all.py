import jinja2
import json
import webapp2
from connexus.common import *
from connexus.ndb_model import *

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.PackageLoader('connexus', 'templates'),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)


# [START ViewAll]
class ViewAll(webapp2.RequestHandler):
    def get(self):
        streams = Stream.query().order(Stream.createDate).fetch()

        template_values = {
            'streams': streams,
            'page': 'View',
        }
        url, url_linktext, user = logout_func(self)
        template_values['url'] = url
        template_values['url_linktext'] = url_linktext
        template_values['user'] = user
        
        template = JINJA_ENVIRONMENT.get_template('viewall.html')
        self.response.write(template.render(template_values))
# [END ViewAll]

# [START ViewAllMobile]
class ViewAllMobile(webapp2.RequestHandler):
    def get(self):
        streams = Stream.query().order(Stream.createDate).fetch()

        stream = {}
        for s in streams:
            stream[s.name] = {
                'name': s.name,
                'id': s.key.id(),
                'coverImageURL': s.coverImageURL,
            }


        # build json for return
        data = {}
        data["page"] = 'View'
        data["streams"] = stream

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write(json.dumps(data))

# [END ViewAllMobile]