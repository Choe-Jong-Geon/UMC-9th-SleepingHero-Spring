-- 1. skins 테이블
INSERT IGNORE INTO skins (id, name) VALUES (1, '기본 용사 세트');
INSERT IGNORE INTO skins (id, name) VALUES (2, '전설의 황금 갑옷');
INSERT IGNORE INTO skins (id, name) VALUES (3, '전설의 황금 갑옷');


-- 2. 멤버 데이터 분리
-- [Member 1] 테스트용 본인 계정 (로그인 시 이 ID를 갖게 됨)
INSERT IGNORE INTO Members (id, email, nick_name, role, provider, provider_id, tutorial_clear, sleep_status)
VALUES (1, 'me@test.com', '내캐릭터', 'ROLE_USER', 'KAKAO', 'kakao_me', true, false);

-- [Member 2] 검색 대상 계정 (김민주)
INSERT IGNORE INTO Members (id, email, nick_name, role, provider, provider_id, tutorial_clear, sleep_status)
VALUES (2, 'minju@test.com', '민주친구', 'ROLE_USER', 'KAKAO', 'kakao_minju', true, false);

-- 3. 수면 데이터 (2번 멤버 '김민주'용)
-- 검색 결과에 나올 수치들을 2번 멤버에게 몰아줍니다.
INSERT IGNORE INTO sleep_goals (member_id, sleep_time, wake_time, current_streak, best_streak, non_sleep_streak)
VALUES (2, '23:00:00', '07:00:00', 16, 20, 0);

INSERT IGNORE INTO sleep_records (member_id, slept_time, woke_time, is_success)
VALUES (2, '2024-01-01 22:00:00', '2024-01-06 02:00:00', true); -- 100시간

INSERT IGNORE INTO sleep_records (member_id, slept_time, woke_time, is_success)
VALUES (2, '2024-01-10 20:00:00', '2024-01-16 21:00:00', true); -- 145시간

-- 4. 히어로 데이터 분리
-- 내 캐릭터 (1번 멤버)


SELECT * FROM Helps;