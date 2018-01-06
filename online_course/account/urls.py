from django.conf.urls import url

from account.views.register import register
from account.views.login import login_authenticate
from account.views.logout import logout_view
from account.views.profile import profile_settings

from django.views.generic import TemplateView

urlpatterns = [
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
    url(r'^logout/$', logout_view, name='logout'),
    url(r'^settings/profile/$', profile_settings, name='profile_settings'),
    url(r'^center/$',TemplateView.as_view(template_name='account/user.html'),name='center')
]
