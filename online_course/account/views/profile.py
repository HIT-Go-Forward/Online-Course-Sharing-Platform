from django.forms import ModelForm
from django.forms import CharField
from django.shortcuts import render
from django.contrib.auth.decorators import login_required
from django.urls import reverse
from django.urls import reverse_lazy
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponseRedirect

from account.models import UserProfile


@login_required(login_url=reverse_lazy('login'))
def profile_settings(request):
    try:
        profile = UserProfile.objects.get(user=request.user)
    except ObjectDoesNotExist:
        profile = UserProfile(user=request.user)
        profile.account_type = 's'
        profile.save()
    if request.method == 'POST':
        form = ProfileForm(request.POST, request.FILES, instance=profile)
        if form.is_valid():
            form.save()
            return HttpResponseRedirect(reverse('profile_settings'))
    else:
        form = ProfileForm(
            instance=profile,
            initial={'email': request.user.email, 'username': request.user.username},
        )
    return render(request, 'account/profile_settings.html', {'form': form})


class ProfileForm(ModelForm):
    username = CharField(disabled=True, label='用户名', required=False)
    email = CharField(disabled=True, label='邮箱', required=False)

    def __init__(self, *args, **kwargs):
        super(ProfileForm, self).__init__(*args, **kwargs)
        self.fields['account_type'].disabled = True

    class Meta:
        model = UserProfile
        fields = [
            'account_type',
            'birth_date',
            'gender',
            'company',
            'degree',
            'avatar',
            'description',
        ]
        labels = {
            'account_type': '身份',
            'birth_date': '出生日期',
            'gender': '性别',
            'company': '公司/学校',
            'degree': '学历',
            'avatar': '头像',
            'description': '个人信息',
        }