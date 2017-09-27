from django.conf.urls import include, url

urlpatterns = [
    url(r'^create-stream/', include('create-stream.urls')),
]
