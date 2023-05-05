/*
Navicat MySQL Data Transfer

Source Server         : docker
Source Server Version : 80030
Source Host           : localhost:3306
Source Database       : teacherselect

Target Server Type    : MYSQL
Target Server Version : 80030
File Encoding         : 65001

Date: 2023-05-05 20:30:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `adminToken` varchar(50) NOT NULL COMMENT '管理员密钥',
  `phone` varchar(50) DEFAULT NULL COMMENT '用户手机号',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`adminToken`),
  UNIQUE KEY `adminToken` (`adminToken`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------
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
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
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
INSERT INTO `basicuser` VALUES ('298743E219305809974E3B69C2A7BB87BA92A7BB038AADD123', '高拉毛力火委府', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '19988647765', '2', '2', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('2E3E440259102017DF477CE60ADD245C17BA0E2F288AAE6FDA', '法或意片些', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '13373585226', '2', '2', '2023-05-05 20:12:22');
INSERT INTO `basicuser` VALUES ('53E4D346F2BF907C93DCF431CAD3B33D964A3FE6919F697248', '任状家空会么', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18118158844', '2', '2', '2023-05-05 19:47:44');
INSERT INTO `basicuser` VALUES ('8F31A32B5812E740892C00AE07A055BAC2386659BB9389779E', '期大己养', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '18135165233', '2', '2', '2023-05-05 20:12:22');
INSERT INTO `basicuser` VALUES ('BEDF3F42FDD0B305253BA715B4062C732A028F825C75D60C3D', '管理员大人', '565A336C957A1589CD26A4F775F14BF1566EBC66581B88244A', '123456789!', '2', '3', '2023-05-05 19:47:44');

-- ----------------------------
-- Table structure for `department`
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parentId` int DEFAULT '0' COMMENT '父级部门（顶级部门 0）',
  `departname` varchar(50) DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `departname` (`departname`),
  KEY `parentId` (`parentId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', '0', '宁波财经学院');
INSERT INTO `department` VALUES ('2', '1', '数字技术与工程学院');
INSERT INTO `department` VALUES ('3', '1', '金融与管理学院');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `roleid` int NOT NULL COMMENT '唯一主键',
  `rolename` varchar(50) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `roleid` (`roleid`),
  KEY `rolename` (`roleid`,`rolename`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('0', '学生');
INSERT INTO `role` VALUES ('1', '管理员');
INSERT INTO `role` VALUES ('2', '老师');

-- ----------------------------
-- Table structure for `student`
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `studentNo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `grade` int DEFAULT NULL COMMENT '年级',
  `cla` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '无班级' COMMENT '班级',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `masterPhone` varchar(50) DEFAULT NULL COMMENT '导师手机号',
  `createDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`studentNo`) USING BTREE,
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `StudentNo` (`studentNo`),
  KEY `FIND_INDEX` (`phone`,`studentNo`,`grade`,`cla`) USING BTREE,
  KEY `MASTER_INDEX` (`masterPhone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='学生信息表';

-- ----------------------------
-- Records of student
-- ----------------------------

-- ----------------------------
-- Table structure for `studentlevel`
-- ----------------------------
DROP TABLE IF EXISTS `studentlevel`;
CREATE TABLE `studentlevel` (
  `id` int NOT NULL COMMENT '主键',
  `studentPhone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生手机号',
  `levelId` int NOT NULL COMMENT '层次项id',
  `position` int DEFAULT NULL COMMENT '排名位置',
  `comfirmed` int DEFAULT NULL COMMENT '确认状态 1确认，其他未确认。\n确认后不可修改',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of studentlevel
-- ----------------------------

-- ----------------------------
-- Table structure for `studentleveltemplate`
-- ----------------------------
DROP TABLE IF EXISTS `studentleveltemplate`;
CREATE TABLE `studentleveltemplate` (
  `templateId` int NOT NULL COMMENT '配置模板id',
  `templateName` varchar(50) NOT NULL COMMENT '配置模板名称',
  `departmentId` int NOT NULL COMMENT '模板适配部门',
  `enable` int NOT NULL COMMENT '是否启用',
  PRIMARY KEY (`templateId`),
  UNIQUE KEY `templateName` (`templateName`),
  KEY `FINDINDEX` (`templateId`,`templateName`,`departmentId`,`enable`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of studentleveltemplate
-- ----------------------------

-- ----------------------------
-- Table structure for `studentleveltemplateitem`
-- ----------------------------
DROP TABLE IF EXISTS `studentleveltemplateitem`;
CREATE TABLE `studentleveltemplateitem` (
  `id` int NOT NULL COMMENT '主键id',
  `templateId` int DEFAULT NULL COMMENT '模板id',
  `levelName` varchar(50) DEFAULT NULL COMMENT '层次名称',
  `percent` double DEFAULT NULL COMMENT '百分比',
  `indexCode` int DEFAULT NULL COMMENT '排序码',
  PRIMARY KEY (`id`),
  KEY `FINDINDEX` (`id`,`templateId`,`levelName`,`percent`,`indexCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of studentleveltemplateitem
-- ----------------------------

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
