from google.appengine.ext.webapp import util
from django.core.handlers import wsgi

# Create a Django application for WSGI.
app = wsgi.WSGIHandler()

def main():
  # Run the WSGI CGI handler with that application.
  util.run_wsgi_app(app)

if __name__ == '__main__':
  main()
