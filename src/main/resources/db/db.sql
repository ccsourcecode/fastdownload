CREATE TABLE `t_person` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `age` int(11) default NULL,
  `address` varchar(50) default NULL,
  `mobile` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `company` varchar(50) default NULL,
  `title` varchar(50) default NULL,
  `create_time` datetime default NULL,
  PRIMARY KEY  (`id`)
);

create table t_download_file
(
    id          bigint auto_increment
        primary key,
    file_id     bigint     null,
    file_status varchar(2) null,
    start_time  timestamp  null,
    finish_time timestamp  null
);

