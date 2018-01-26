from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
from django.shortcuts import render
from django.urls import reverse_lazy


@login_required(login_url=reverse_lazy('login'))
def my_course(request):
    user = request.user
    learning_courses = user.courses_learning.all()

    return render(request, 'course/my_course.html', context={
        'learning_courses': learning_courses,
    })
