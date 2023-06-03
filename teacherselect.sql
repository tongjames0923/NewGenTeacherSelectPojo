/*
Navicat MySQL Data Transfer

Source Server         : docker
Source Server Version : 80030
Source Host           : localhost:3306
Source Database       : teacherselect

Target Server Type    : MYSQL
Target Server Version : 80030
File Encoding         : 65001

Date: 2023-06-03 12:55:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `adminToken` varchar(50) NOT NULL COMMENT '管理员密钥',
  `phone` varchar(50) DEFAULT NULL COMMENT '用户手机号',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`adminToken`),
  UNIQUE KEY `adminToken` (`adminToken`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('15606810923', '15606810923', '2023-05-06 10:40:11');
INSERT INTO `admin` VALUES ('test123', '123456789!', '2023-05-05 19:48:56');

-- ----------------------------
-- Table structure for `basicuser`
-- ----------------------------
DROP TABLE IF EXISTS `basicuser`;
CREATE TABLE `basicuser` (
  `uid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '无名' COMMENT '姓名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号（唯一）',
  `role` int NOT NULL DEFAULT '0' COMMENT '用户角色（学生、老师、管理员）',
  `departmentId` int DEFAULT NULL COMMENT '部门',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `unique_phone` (`phone`) USING BTREE,
  KEY `login_index` (`phone`,`password`) USING BTREE,
  KEY `role` (`role`),
  KEY `departmentId` (`departmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of basicuser
-- ----------------------------
INSERT INTO `basicuser` VALUES ('0B58A70EDFA59571D3BBFC512529D24D4920958D19DB0122B6', '三面品器或', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '88949958822', '2', '3', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('12950E30C54E2A3AC25676A91E65678D8FFC815D00DF513ADC', '水心斯', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '15606810922', '2', '2', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('1D5030E88BC2FCEEAA939E8006237B814CA664ACB83DCAA2FC', '目候写统', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '15606840922', '2', '3', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('21F2022A08B64913BCF795BED888235135989A691AA200A4B8', '童泊森', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '15606810923', '1', '2', '2023-05-06 10:40:11');
INSERT INTO `basicuser` VALUES ('298743E219305809974E3B69C2A7BB87BA92A7BB038AADD123', '高拉毛力火委府', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '19988647765', '2', '2', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('2E3E440259102017DF477CE60ADD245C17BA0E2F288AAE6FDA', '法或意片些', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '13373585226', '2', '2', '2023-05-05 20:12:22');
INSERT INTO `basicuser` VALUES ('4260B561BF3ED3F933B9C56223DE58556DE658D0BE8641B0C2', '张三', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18892990717', '0', '2', '2023-05-06 10:22:52');
INSERT INTO `basicuser` VALUES ('53E4D346F2BF907C93DCF431CAD3B33D964A3FE6919F697248', '任状家空会么', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18118158844', '2', '2', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('890F37D5BC1231FEF8B61204D6D679ED131C154082FBC9AB99', '李四', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18114968421', '0', '1', '2023-05-06 10:22:52');
INSERT INTO `basicuser` VALUES ('8F31A32B5812E740892C00AE07A055BAC2386659BB9389779E', '期大己养', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18135165233', '2', '2', '2023-05-05 20:12:22');
INSERT INTO `basicuser` VALUES ('BEDF3F42FDD0B305253BA715B4062C732A028F825C75D60C3D', '管理员大人', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '123456789!', '1', '3', '2023-05-06 10:20:50');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL COMMENT '部门id',
  `parentId` int NOT NULL DEFAULT '0' COMMENT '父级部门（顶级部门 0）',
  `departname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `departname` (`departname`),
  KEY `parentId` (`parentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '0', '宁波财经学院', '2023-05-06 10:06:00');
INSERT INTO `department` VALUES ('2', '1', '数字技术与工程学院', '2023-05-06 10:06:00');
INSERT INTO `department` VALUES ('3', '1', '金融与管理学院', '2023-05-06 10:06:00');
INSERT INTO `department` VALUES ('10', '1096761985', '测试部门A', '2023-06-02 20:01:31');
INSERT INTO `department` VALUES ('1096761985', '0', '测试学院', '2023-05-06 11:25:27');

-- ----------------------------
-- Table structure for `masterrelation`
-- ----------------------------
DROP TABLE IF EXISTS `masterrelation`;
CREATE TABLE `masterrelation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `studentPhone` varchar(50) NOT NULL COMMENT '学生手机号',
  `masterPhone` varchar(50) DEFAULT NULL COMMENT '老师手机号',
  PRIMARY KEY (`id`),
  KEY `FIND_INDEX` (`id`,`masterPhone`,`studentPhone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of masterrelation
-- ----------------------------

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` int NOT NULL COMMENT '唯一主键',
  `rolename` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `roleid` (`roleid`,`rolename`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('0', '学生', '2023-05-06 10:06:29');
INSERT INTO `role` VALUES ('1', '管理员', '2023-05-06 10:06:29');
INSERT INTO `role` VALUES ('2', '老师', '2023-05-06 10:06:29');

-- ----------------------------
-- Table structure for `scoreconfigtemplate`
-- ----------------------------
DROP TABLE IF EXISTS `scoreconfigtemplate`;
CREATE TABLE `scoreconfigtemplate` (
  `id` varchar(50) NOT NULL,
  `templateName` varchar(255) NOT NULL COMMENT '模板名称',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `createUser` varchar(255) NOT NULL COMMENT '创建用户',
  `departmentId` int NOT NULL COMMENT '模板有效部门',
  PRIMARY KEY (`id`),
  KEY `FIND_INDEX` (`id`,`templateName`,`departmentId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of scoreconfigtemplate
-- ----------------------------
INSERT INTO `scoreconfigtemplate` VALUES ('DD11F9F1103DA8E7E82D56B31DBEA7E5C4C900A408EC187F9C', '数工学院基础模板', '2023-06-03 12:26:37', '15606810923', '2');
INSERT INTO `scoreconfigtemplate` VALUES ('ECB3860155CA0DA94BF1B764A0A89D69AFEFD8C3EABAA153EB', '数工学院无敌模板', '2023-06-03 12:29:57', '15606810923', '2');

-- ----------------------------
-- Table structure for `scoreconfigtemplateitem`
-- ----------------------------
DROP TABLE IF EXISTS `scoreconfigtemplateitem`;
CREATE TABLE `scoreconfigtemplateitem` (
  `id` int NOT NULL AUTO_INCREMENT,
  `configName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '閰嶇疆鍗曢」鍚嶇О',
  `sortCode` int NOT NULL COMMENT '鎺掑簭鐮侊紝褰卞搷鐧惧垎姣?',
  `percent` int NOT NULL COMMENT '闁谎勫劤閸ㄥ骸袙?',
  `templateId` varchar(50) NOT NULL COMMENT '濡剝婢樻稉濠氭暛',
  PRIMARY KEY (`id`),
  KEY `FIND_INDEX` (`templateId`,`sortCode`) USING BTREE,
  KEY `ByTEMPLATE` (`templateId`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of scoreconfigtemplateitem
-- ----------------------------
INSERT INTO `scoreconfigtemplateitem` VALUES ('4', '优秀生', '0', '5', 'DD11F9F1103DA8E7E82D56B31DBEA7E5C4C900A408EC187F9C');
INSERT INTO `scoreconfigtemplateitem` VALUES ('5', '普通生', '1', '65', 'DD11F9F1103DA8E7E82D56B31DBEA7E5C4C900A408EC187F9C');
INSERT INTO `scoreconfigtemplateitem` VALUES ('6', '差生', '2', '30', 'DD11F9F1103DA8E7E82D56B31DBEA7E5C4C900A408EC187F9C');
INSERT INTO `scoreconfigtemplateitem` VALUES ('7', '优秀生', '0', '1', 'ECB3860155CA0DA94BF1B764A0A89D69AFEFD8C3EABAA153EB');
INSERT INTO `scoreconfigtemplateitem` VALUES ('8', '普通生', '1', '19', 'ECB3860155CA0DA94BF1B764A0A89D69AFEFD8C3EABAA153EB');
INSERT INTO `scoreconfigtemplateitem` VALUES ('9', '差生', '2', '80', 'ECB3860155CA0DA94BF1B764A0A89D69AFEFD8C3EABAA153EB');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `studentNo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `grade` int DEFAULT NULL COMMENT '年级',
  `cla` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '无班级' COMMENT '班级',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`studentNo`) USING BTREE,
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `StudentNo` (`studentNo`),
  KEY `FIND_INDEX` (`phone`,`studentNo`,`grade`,`cla`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生信息表';

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1101', '1', '三班', '18892990717', '2023-05-06 10:22:52');
INSERT INTO `student` VALUES ('1102', '1', '三班', '18114968421', '2023-05-06 10:22:52');

-- ----------------------------
-- Table structure for `teacher`
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `workNo` varchar(50) NOT NULL COMMENT '工号',
  `phone` varchar(50) NOT NULL COMMENT '手机号',
  `position` varchar(50) NOT NULL DEFAULT '讲师' COMMENT '职位',
  `pro_title` varchar(50) DEFAULT '无' COMMENT '职称',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`workNo`),
  UNIQUE KEY `workNo` (`workNo`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='教师信息表';

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('aliqua sit sed deserunt', '13373585226', 'Duis pariatur occaecat irure esse', '较管叫养', '2023-05-05 20:12:22');
INSERT INTO `teacher` VALUES ('dolor nisi anim minim qui', '15606840922', 'do fugiat officia', '人想理要类或', '2023-05-05 19:47:44');
INSERT INTO `teacher` VALUES ('labore officia quis qui', '88949958822', 'elit', '养电千', '2023-05-05 19:47:44');
INSERT INTO `teacher` VALUES ('minim', '18135165233', 'incididunt dolore', '问得者非压', '2023-05-05 20:12:22');
INSERT INTO `teacher` VALUES ('mollit cupidatat dolor', '19988647765', 'ipsum sit nisi dolor do', '系列儿同素数改', '2023-05-05 19:47:44');
INSERT INTO `teacher` VALUES ('non Ut', '18118158844', 'consectetur qui', '集问情包全', '2023-05-05 19:47:44');
INSERT INTO `teacher` VALUES ('pariatur cupidatat aute laborum', '15606810922', 'aliqua anim adipisicing minim', '也过术京', '2023-05-05 19:47:44');
