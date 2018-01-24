from django.contrib.auth.decorators import login_required
from django.http import HttpResponseBadRequest
from django.shortcuts import render
from django.urls import reverse_lazy
from django.views.decorators.http import require_safe
from online_course.settings import ITEMS_PER_PAGE
from account.models import TeacherApplication
import math


@require_safe
@login_required(login_url=reverse_lazy('login'))
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
