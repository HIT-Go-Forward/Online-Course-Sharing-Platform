from django.conf.urls import url

from .views.register import register
from .views.login import login_authenticate


urlpatterns = [
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
]
