import math

from django.contrib.auth.decorators import login_required, user_passes_test
from django.db import transaction
from django.http import HttpResponseBadRequest
from django.shortcuts import render, redirect, get_object_or_404
from django.urls import reverse_lazy, reverse
from django.views.decorators.http import require_safe, require_POST

from account.models import TeacherApplication
from online_course.settings import ITEMS_PER_PAGE


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
    applications = TeacherApplication.objects.order_by('timestamp')[(page - 1) * ITEMS_PER_PAGE: page * ITEMS_PER_PAGE]
    total_page = math.ceil(TeacherApplication.objects.count() / ITEMS_PER_PAGE)
    return render(request, 'account/manager/apply_manage.html', context={
        'teacher_applications': applications,
        'total_page': total_page,
        'page_num': page,
    })


@require_POST
@user_passes_test(manager_check, login_url=reverse_lazy('login'))
def approve_application(request, application_id):
    application_id = int(application_id)
    with transaction.atomic():
        application = get_object_or_404(TeacherApplication, id=application_id)
        profile = application.user.userprofile
        profile.account_type = 't'
        profile.save()
        application.delete()
    return redirect(request.META.get('HTTP_REFERER', reverse('apply_manage')))


@require_POST
@user_passes_test(manager_check, login_url=reverse_lazy('login'))
def deny_application(request, application_id):
    application_id = int(application_id)
    application = get_object_or_404(TeacherApplication, id=application_id)
    application.delete()
    return redirect(request.META.get('HTTP_REFERER', reverse('apply_manage')))
