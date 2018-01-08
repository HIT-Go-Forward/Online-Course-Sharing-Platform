from rest_framework.serializers import ModelSerializer
from account.models import UserProfile


class AvatarSerializer(ModelSerializer):
    class Meta:
        model = UserProfile
        fields = ('user', 'avatar',)
        read_only_fields = ('user',)
