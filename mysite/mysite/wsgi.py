"""
WSGI config for mysite project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/1.11/howto/deployment/wsgi/
"""

import os, sys

# add the mysite project path into the sys.path
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SITE_PKG_ROOT = os.path.join(BASE_DIR, '../mysite/lib/python2.7/site-packages')

#sys.path.append('<PATH_TO_MY_DJANGO_PROJECT>/mysite')

# add the virtualenv site-packages path to the sys.path
sys.path.append(SITE_PKG_ROOT)
sys.path.append(BASE_DIR)

from django.core.wsgi import get_wsgi_application

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "mysite.settings")

application = get_wsgi_application()
