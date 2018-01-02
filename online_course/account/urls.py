from django.conf.urls import url

from account.views.register import register
from account.views.login import login_authenticate
from account.views.profile import profile_settings


urlpatterns = [
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
    url(r'^settings/profile/$', profile_settings, name='profile_settings')
]
