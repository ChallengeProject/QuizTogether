-- DROP TABLE IF EXISTS broadcast;

-- Create syntax for TABLE 'broadcast'
CREATE TABLE `broadcast` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(50)  NOT NULL DEFAULT '',
  `description` varchar(300)  DEFAULT NULL,
  `broadcast_status` smallint(11) NOT NULL,
  `prize` bigint(20) NOT NULL,
  `gift_description` varchar(100)  DEFAULT NULL,
  `gift_type` int(11) NOT NULL,
  `winner_message` varchar(200)  DEFAULT NULL,
  `code` varchar(300)  DEFAULT '',
  `is_public` tinyint(4) DEFAULT NULL,
  `question_count` int(11) DEFAULT NULL,
  `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `updated_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_time` timestamp(3) NULL DEFAULT NULL,
  `scheduled_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
);
--
-- -- Create syntax for TABLE 'chat'
-- CREATE TABLE `chat` (
--   `id` bigint(20) NOT NULL AUTO_INCREMENT,
--   `broadcast_id` bigint(20) NOT NULL,
--   `chat_url` varchar(300)  NOT NULL DEFAULT '',
--   `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
--   PRIMARY KEY (`id`),
--   KEY `broadcast_id` (`broadcast_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- -- Create syntax for TABLE 'push'
-- CREATE TABLE `push` (
--   `seq` bigint(20) NOT NULL AUTO_INCREMENT,
--   `message` varchar(300)  NOT NULL DEFAULT '',
--   `broadcast_id` bigint(20) NOT NULL,
--   `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
--   `push_type` tinyint(4) NOT NULL,
--   PRIMARY KEY (`seq`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Create syntax for TABLE 'question'
CREATE TABLE `question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `broadcast_id` bigint(20) NOT NULL,
  `step` int(11) NOT NULL,
  `question_prop` varchar(1000)  NOT NULL DEFAULT '',
  `answer_no` int(11) NOT NULL,
  `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `updated_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `category` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `broadcast_id` (`broadcast_id`,`step`)
);
--
-- -- Create syntax for TABLE 'stream'
-- CREATE TABLE `stream` (
--   `id` bigint(20) NOT NULL AUTO_INCREMENT,
--   `user_id` bigint(20) NOT NULL,
--   `broadcast_id` bigint(20) NOT NULL,
--   `stream_status` tinyint(4) NOT NULL DEFAULT '0',
--   `streaming_url` varchar(300) NOT NULL DEFAULT '',
--   `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
--   `updated_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
--   `started_time` timestamp(3) NULL DEFAULT NULL,
--   `completed_time` timestamp(3) NULL DEFAULT NULL,
--   PRIMARY KEY (`id`),
--   KEY `broadcast_id` (`broadcast_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--

DROP TABLE IF EXISTS user;

-- Create syntax for TABLE 'user'
CREATE TABLE user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL DEFAULT '',
  user_status tinyint(4) NOT NULL DEFAULT '0',
  profile_path varchar(300) DEFAULT NULL,
  money bigint(20) NOT NULL DEFAULT '0',
  referral_user varchar(40) DEFAULT NULL,
  created_time timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  updated_time timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  deleted_time timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `name` (`name`)
);
--
-- -- Create syntax for TABLE 'user_answer_history'
-- CREATE TABLE `user_answer_history` (
--   `seq` bigint(11) NOT NULL AUTO_INCREMENT,
--   `user_id` bigint(11) NOT NULL,
--   `boradcast_id` bigint(11) NOT NULL,
--   `step` int(11) NOT NULL,
--   `answer_no` int(11) NOT NULL,
--   `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
--   PRIMARY KEY (`seq`),
--   KEY `user_id` (`user_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Create syntax for TABLE 'user_device'
CREATE TABLE `user_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `push_token` varchar(300)  NOT NULL DEFAULT '',
  `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `updated_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY uni_user_id (user_id)
);
--
-- Create syntax for TABLE 'user_follower'
CREATE TABLE `user_follower` (
  `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `follower` bigint(20) NOT NULL,
  `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`seq`),
  UNIQUE KEY `user_follower_user_id` (`user_id`,`follower`)
);
--
-- Create syntax for TABLE 'user_inventory'
CREATE TABLE `user_inventory` (
  `user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `heart_count` int(11) NOT NULL,
  `created_time` timestamp(3) NOT NULL,
  `updated_time` timestamp(3) NOT NULL,
  PRIMARY KEY (`user_id`)
);
--
-- -- Create syntax for TABLE 'user_inventory_history'
-- CREATE TABLE `user_inventory_history` (
--   `seq` int(11) unsigned NOT NULL AUTO_INCREMENT,
--   `user_id` int(11) DEFAULT NULL,
--   `created_time` int(11) DEFAULT NULL,
--   PRIMARY KEY (`seq`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- -- Create syntax for TABLE 'user_prize_history'
-- CREATE TABLE `user_prize_history` (
--   `seq` bigint(20) NOT NULL AUTO_INCREMENT,
--   `user_id` bigint(20) NOT NULL,
--   `amount` bigint(20) NOT NULL,
--   `broadcast_id` bigint(11) NOT NULL,
--   `created_time` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
--   PRIMARY KEY (`seq`),
--   KEY `user_id` (`user_id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Create syntax for TABLE 'user_referral'
CREATE TABLE `user_referral` (
  `seq` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `referral_user` bigint(20) NOT NULL,
  `created_time` timestamp(3) NOT NULL,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `user_id` (`user_id`)
);