# Generated by Django 5.1.7 on 2025-05-07 14:19

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0004_rename_points_post_likes'),
    ]

    operations = [
        migrations.RenameField(
            model_name='post',
            old_name='likes',
            new_name='points',
        ),
    ]
