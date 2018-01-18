from django.conf.urls import url, include
from rest_framework.routers import DefaultRouter

from account.views.avatar import AvatarViewSet
from account.views.change_password import change_password
from account.views.login import login_authenticate
from account.views.logout import logout_view
from account.views.profile import profile_settings, profile_view
from account.views.register import register

router = DefaultRouter()
router.register(r'settings/profile/avatar', AvatarViewSet, 'avatar')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^register/$', register, name='register'),
    url(r'^login/$', login_authenticate, name='login'),
    url(r'^logout/$', logout_view, name='logout'),
    url(r'^settings/profile/$', profile_settings, name='profile_settings'),
    url(r'^settings/change_password/$', change_password, name='change_password'),
<<<<<<< HEAD
    url(r'^center/$', TemplateView.as_view(template_name='account/student/user.html'), name='center'),
=======
    url(r'^center/$', profile_view, name='center'),
>>>>>>> origin/master
]
