-- 1. 레벨 데이터 (1~3레벨)

select * from levels;
INSERT IGNORE INTO levels (id, need_exp) VALUES (1, 100);
INSERT IGNORE INTO levels (id, need_exp) VALUES (2, 250);
INSERT IGNORE INTO levels (id, need_exp) VALUES (3, 500);

-- skins 테이블 가데이터
INSERT  IGNORE INTO skins (id, name) VALUES (1, '기본 용사 세트');
INSERT IGNORE INTO skins (id, name) VALUES (2, '전설의 황금 갑옷');

-- skin_member (보유 현황)
INSERT IGNORE INTO skin_member (member_id, skin_id) VALUES (1, 1);
INSERT  IGNORE INTO skin_member (member_id, skin_id) VALUES (1, 2);