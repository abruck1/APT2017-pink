# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.http import HttpResponse
from django.shortcuts import render

# Create your views here.
def index(request):
    #upload an image
    # key: streamID, value: img url?

    # img1 = '~/Pictures/utaustin_colored/Orange_Tower.jpg'
    #
    # import logging
    # import os
    # from google.cloud import storage
    # import webapp2
    #
    # from google.appengine.api import app_identity
    #
    # def get(self):
    #     bucket_name = os.environ.get('BUCKET_NAME',
    #                                  app_identity.get_default_gcs_bucket_name())
    #
    #     self.response.headers['Content-Type'] = 'text/plain'
    #     self.response.write('Demo GCS Application running from Version: '
    #                         + os.environ['CURRENT_VERSION_ID'] + '\n')
    #     self.response.write('Using bucket name: ' + bucket_name + '\n\n')

    return HttpResponse("This is a test.")