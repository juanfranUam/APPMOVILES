# Generated by Django 5.1.6 on 2025-02-07 12:53

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0007_challenge_detail_challenge_name_post_challenge_and_more'),
    ]

    operations = [
        migrations.AlterModelOptions(
            name='post',
            options={'ordering': ['-date']},
        ),
    ]
