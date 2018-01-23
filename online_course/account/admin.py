from django.contrib.admin import site

from account.models import UserProfile, TeacherApplication

site.register(UserProfile)
site.register(TeacherApplication)
