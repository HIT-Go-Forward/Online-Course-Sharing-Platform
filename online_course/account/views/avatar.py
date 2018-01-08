from rest_framework.permissions import IsAuthenticatedOrReadOnly, BasePermission, SAFE_METHODS
from rest_framework.viewsets import ModelViewSet

from account.models import UserProfile
from account.serializers import AvatarSerializer


class AvatarPermission(BasePermission):
    def has_object_permission(self, request, view, obj):
        if request.method in SAFE_METHODS:
            return True
        return obj.user == request.user


class AvatarViewSet(ModelViewSet):
    queryset = UserProfile.objects.all()
    serializer_class = AvatarSerializer
    permission_classes = (AvatarPermission,)
    http_method_names = ('get', 'post', 'put', 'head',)
    lookup_field = 'user'

    def perform_create(self, serializer):
        serializer.save(author=self.request.user)

    def perform_update(self, serializer):
        serializer.save(author=self.request.user)
