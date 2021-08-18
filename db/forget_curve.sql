/*
 Navicat Premium Data Transfer

 Source Server         : yuan
 Source Server Type    : MySQL
 Source Server Version : 50540
 Source Host           : 120.78.67.40:3306
 Source Schema         : forget_curve

 Target Server Type    : MySQL
 Target Server Version : 50540
 File Encoding         : 65001

 Date: 18/08/2021 22:44:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名密码',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `state` int(1) NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for forget_curve
-- ----------------------------
DROP TABLE IF EXISTS `forget_curve`;
CREATE TABLE `forget_curve`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `target` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标',
  `description` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `state` int(1) NULL DEFAULT 0 COMMENT '状态',
  `order_num` int(5) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for forget_curve_item
-- ----------------------------
DROP TABLE IF EXISTS `forget_curve_item`;
CREATE TABLE `forget_curve_item`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `target_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标',
  `cycle` int(1) NOT NULL COMMENT '周期',
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for four_review
-- ----------------------------
DROP TABLE IF EXISTS `four_review`;
CREATE TABLE `four_review`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `target_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标',
  `cycle` int(1) NOT NULL COMMENT '周期',
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for learn_time
-- ----------------------------
DROP TABLE IF EXISTS `learn_time`;
CREATE TABLE `learn_time`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `learn_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学习名称',
  `learn_time` int(5) NULL DEFAULT 0 COMMENT '学习时长（分）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学习时长' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tiny_habit
-- ----------------------------
DROP TABLE IF EXISTS `tiny_habit`;
CREATE TABLE `tiny_habit`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `task_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '微习惯名称',
  `state` int(1) NULL DEFAULT 1 COMMENT '状态',
  `description` varchar(225) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '微习惯描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '微习惯' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tiny_habit_log
-- ----------------------------
DROP TABLE IF EXISTS `tiny_habit_log`;
CREATE TABLE `tiny_habit_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'ID',
  `tiny_habit_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '微习惯ID',
  `punch_card_state` int(1) NULL DEFAULT 0 COMMENT '状态',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `time_interval` int(5) NULL DEFAULT 0 COMMENT '执行时间',
  `unit_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '单位',
  `execute_count` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '执行次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '微习惯日志' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
