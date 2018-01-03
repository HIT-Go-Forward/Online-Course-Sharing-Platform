from django.contrib.admin import site

from account.models import UserProfile


site.register(UserProfile)
