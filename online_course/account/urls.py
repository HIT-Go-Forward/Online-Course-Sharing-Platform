from django.conf.urls import url, include
from rest_framework.routers import DefaultRouter

from account.views.avatar import AvatarViewSet
from account.views.register import register
from account.views.login import login_authenticate
from account.views.logout import logout_view
from account.views.profile import profile_settings
from account.views.change_password import change_password

from django.views.generic import TemplateView


router = DefaultRouter()
router.register(r'settings/profile/avatar', AvatarViewSet, 'avatar')


urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
    url(r'^logout/$', logout_view, name='logout'),
    url(r'^settings/profile/$', profile_settings, name='profile_settings'),
    url(r'^settings/change_password/$', change_password, name='change_password'),
    url(r'^center/$',TemplateView.as_view(template_name='account/user.html'),name='center'),
]
