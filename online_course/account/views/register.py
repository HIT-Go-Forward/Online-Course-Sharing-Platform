from django.contrib.auth.models import User
from django.views.decorators.http import require_POST
from django.http import JsonResponse

from account.forms import RegisterForm


@require_POST()
def register(request):
    form = RegisterForm(request.POST)
    if form.is_valid():
        email = form.cleaned_data['email']
        user = User.objects.create_user(email, email, form.cleaned_data['password'])
        user.save()
        response = {'code': 0}
    else:
        response = {'code': -100, 'message': form.errors}
    return JsonResponse(response)
