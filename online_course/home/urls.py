from django.conf.urls import url

from home.views import index


urlpatterns = [
    url(r'^$', index, name='index')
]
