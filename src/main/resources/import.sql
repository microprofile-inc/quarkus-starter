-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database


INSERT INTO public.country_code (when_created, when_deleted, when_modified, country_code, currency_alphabetic_code, dial, id, languages, cldr_display_name, official_name_cn, official_name_en) VALUES ('2024-04-27 09:37:55.346630 +00:00', null, '2024-04-27 09:37:55.346672 +00:00', 'CN', 'CNY', '+86', '0466037a-b655-4629-a748-35ca3789d68f', 'zh-CN', 'China', '中华人民共和国', 'China');

INSERT INTO public."user" (when_created, when_deleted, when_modified, phone_number, country_code_id, id, email, username, avatar, password, social, website, role, sex) VALUES ('2026-01-11 07:54:15.073922 +00:00', null, '2026-01-11 07:54:15.073928 +00:00', '16631132230', '0466037a-b655-4629-a748-35ca3789d68f', '93750fb9-035c-47ae-82d8-48f2d3749468', '838394225@qq.com', 'bggdbbdccda', null, '$2a$10$TX9tTR9xg3qZa0wvbWEen.vb9qKXfF7HbE0U9rfK.ZVJ6w/vjSweO', null, null, 'USER', null);
INSERT INTO public.sms_code (code, effective, when_created, when_deleted, when_modified, phone_number, country_code_id, id) VALUES ('4689', false, '2026-01-11 07:53:35.818262 +00:00', null, '2026-01-11 07:54:14.916661 +00:00', '16631132230', '0466037a-b655-4629-a748-35ca3789d68f', 'd3a2abd5-e49e-4fb3-8bc0-73a19577419b');