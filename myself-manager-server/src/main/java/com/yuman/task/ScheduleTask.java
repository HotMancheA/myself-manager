package com.yuman.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ScheduleTask {


    @Value("${backupDB.database}")
    private String databaseName;

    @Value("${backupDB.path}")
    private String path;
    //${scheduleTask.cron}
    @Scheduled(cron = "${scheduleTask.cron}")
    public void execute() throws Exception {
        log.warn("数据备份中...");
        backupDB();
        log.warn("数据备份结束");
    }


    public void backupDB() throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process pro = rt.exec(getCommand());
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
        String errorLine = null;
        while ((errorLine = br.readLine()) != null) {
            System.out.println(errorLine);
        }
        br.close();
        int result = pro.waitFor();
        if (result != 0) {
            throw new Exception("数据库备份失败！ ");
        }
    }

    private String[] getCommand() {
        String[] cmd = new String[3];
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
        String os = System.getProperties().getProperty("os.name");
        if (os.startsWith("Win")) {
            cmd[0] = "cmd.exe";
            cmd[1] = "/c";
        } else {
            cmd[0] = "/bin/sh";
            cmd[1] = "-c";
        }
        StringBuilder arg = new StringBuilder();
        arg.append("mysqldump ");
        arg.append("-uroot ");
        arg.append("-proot ");
        arg.append("");//--default-character=gbk
        arg.append("--skip-opt ");
        arg.append("--add-drop-database ");
        arg.append("--routines ");
        arg.append("--triggers ");
        arg.append("--compress ");
        arg.append("-r");
        arg.append(path);
        arg.append(databaseName);
        arg.append(".sql ");
        arg.append("--databases "+databaseName);
        cmd[2] = arg.toString();
        return cmd;
    }
}
