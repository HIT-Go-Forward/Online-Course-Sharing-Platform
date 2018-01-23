from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
from django.shortcuts import render
from django.urls import reverse_lazy

from account.models import TeacherApplication


@login_required(login_url=reverse_lazy('login'))
def teacher_apply(request):
    if request.method == 'POST':
        user = request.user
        application = TeacherApplication.objects.filter(user=user)
        if application.exists():
            return JsonResponse({'code': -100, 'message': '用户已提交过申请'})
        application = TeacherApplication(user=user)
        application.save()
        return JsonResponse({'code': 0})
    else:
        return render(request, 'account/student/teacher_apply.html')
