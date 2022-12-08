/**
 *
 * init sql
 *
 */

CREATE TABLE `to_do_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `slug` VARCHAR(64) CHARSET ascii NOT NULL,
  `color` TINYINT UNSIGNED NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO to_do_category(name,slug,color) values('フロントエンド','front',1);
INSERT INTO to_do_category(name,slug,color) values('バックエンド','back',2);
INSERT INTO to_do_category(name,slug,color) values('インフラ','infra',3);


CREATE TABLE `to_do` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) unsigned NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `body` TEXT,
  `state` TINYINT UNSIGNED NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `to_do`(category_id,title,body,state) values(1, 'デザインをいい感じにする','ヘッダーのデザインをもっといい感じに',0);
INSERT INTO `to_do`(category_id,title,body,state) values(2, 'Controllerの修正','Controller名をもっといい感じに',1);
INSERT INTO `to_do`(category_id,title,body,state) values(3, '新しいDB環境の作成','タイトル通り',2);


