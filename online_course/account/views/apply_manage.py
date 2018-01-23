from django.contrib.auth.decorators import user_passes_test
from django.http import HttpResponseBadRequest
from django.shortcuts import render
from django.urls import reverse_lazy
from django.views.decorators.http import require_safe

from account.models import TeacherApplication


def manager_check(user):
    if not user:
        return False
    return user.groups.filter(name='Manager').exists()


@require_safe
@user_passes_test(manager_check, login_url=reverse_lazy('login'))
def apply_manage(request, page='1'):
    page = int(page)
    if page < 1 or 1000000000 < page:
        return HttpResponseBadRequest()
    applications = TeacherApplication.objects.order_by('timestamp')[(page - 1) * 20: page * 20]
    return render(request, 'account/manager/apply_manage.html', context={
        'teacher_applications': applications,
    })
