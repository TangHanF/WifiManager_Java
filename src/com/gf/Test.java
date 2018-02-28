package com.gf;

import java.io.*;
import java.util.*;

/**
 * Title: <br>
 * Packet:com.gf<br>
 * Description: <br>
 * Author:GuoFu<br>
 * Create Date: 2017-12-27.<br>
 * Modify User: <br>
 * Modify Date: <br>
 * Modify Description: <br>
 */
public class Test {
    private static Map<String, String> queryWifi() throws Exception {
        String cmdQueryWifi = "cmd /c netsh wlan show profile";
        Map<String, String> wifiMap = new HashMap<>();
        List<String> resList = CmdUtils.runCmdGetList(cmdQueryWifi, "GBK");
        int index = 0;
        String tip = "", wifiName = "";
        System.out.println("您当前电脑已经连接过的WiFi热点有：");
        for (String str : resList) {
            if (str.contains("所有用户配置文件 :")) {
                index++;
                wifiName = str.substring(str.indexOf(":") + 1);
                tip = String.format("\t%3d、%s", index, wifiName);
                wifiMap.put(String.valueOf(index), wifiName);
                System.out.println(tip);
            }
        }
        return wifiMap;
    }

    public static void main(String[] args) throws Exception {
        boolean flag = true;
        Scanner scanner = new Scanner(System.in);
        final String FOLDER = "D:\\backup_wifi";
        String sf = "";
        int runIndex = 0;
        while (flag) {
            String cmdExportWifi = "cmd /c netsh wlan export profile folder=%s";
            String cmdExportWifi_byName = "cmd /c netsh wlan export profile name=\"%s\" folder=%s";
            String cmdImportWifi_byName = "netsh wlan add profile filename=\"%s\" user=all";

            if (runIndex > 0) sf = "\n\n";
            System.out.println(sf + ">>>>>>>>>>>>>>>>请选择您要进行的操作：\n\t【1】查询所有已连接过的WiFi\n\t【2】导出所有WiFi配置\n\t【3】导出某个WiFi配置\n\t【4】导入某个WiFi配置\n\t【5】导入所有WiFi配置\n\t终止请输入:E");
            System.out.println(">>>>输入功能号：");
            String opNum = scanner.next();
            switch (opNum) {
                // <editor-fold desc="查询所有已连接过的WiFi">
                case "1":
                    queryWifi();
                    break;
                // </editor-fold>

                // <editor-fold desc="导出所有WiFi配置">
                case "2":
                    System.out.println("请输入WiFi配置文件导入目录（使用默认路径请输入数字1）：");
                    String wifiCfgFilePath = scanner.next();
                    if ("1".equals(wifiCfgFilePath)) {
                        wifiCfgFilePath = FOLDER;
                    }
                    File file = new File(wifiCfgFilePath);
                    if (!file.exists())
                        file.mkdirs();
                    cmdExportWifi = String.format(cmdExportWifi, FOLDER);
                    CmdUtils.runCmdGetString(cmdExportWifi, "GBK");
                    System.out.println("========>>>>>>>>导出完毕！请打开：" + FOLDER + " 进行查看");
                    break;
                // </editor-fold>

                // <editor-fold desc="导出某个WiFi配置">
                case "3":
                    Map<String, String> wifiMap = queryWifi();
                    System.out.println("请输入WiFi配置文件导入目录（使用默认路径请输入数字1）：");
                    wifiCfgFilePath = scanner.next();
                    if ("1".equals(wifiCfgFilePath)) {
                        wifiCfgFilePath = FOLDER;
                    }

                    System.out.println("请输入要备份的WiFi序号:");
                    String wifiNum = scanner.next();
                    file = new File(wifiCfgFilePath);
                    if (!file.exists())
                        file.mkdirs();

                    String wifiName = wifiMap.get(wifiNum);
                    if (wifiName != null) {
                        cmdExportWifi_byName = String.format(cmdExportWifi_byName, wifiName.trim(), FOLDER);
                        String s = CmdUtils.runCmdGetString(cmdExportWifi_byName, "GBK");
                        if (s.contains("成功保存"))
                            System.out.println(wifiName.trim() + " 导出完毕！请打开：" + FOLDER + " 进行查看");
                        else
                            System.out.println(s);
                    } else {
                        System.out.println("导出失败！");
                    }
                    break;
                // </editor-fold>

                // <editor-fold desc="导入某个WiFi配置">
                case "4":
                    System.out.println("请输入WiFi的SSID名称：");
                    String ssid = scanner.next();
                    wifiCfgFilePath = FOLDER + File.separator + "无线网络连接-" + ssid + ".xml";
                    if (new File(wifiCfgFilePath).exists()) {
                        cmdImportWifi_byName = String.format(cmdImportWifi_byName, wifiCfgFilePath);
                        String s = CmdUtils.runCmdGetString(cmdImportWifi_byName, "GBK");
                        if (s.contains("添加到接口")) {
                            System.out.println("导入成功！\n");
                        } else {
                            System.out.println(s);
                        }
                    } else {
                        System.err.println("没有无线网[" + ssid + "]的相关配置信息，无法导入");
                    }
                    break;
                // </editor-fold>

                case "5":
                    // <editor-fold desc="导入所有WiFi配置">
                    System.out.println("请输入WiFi配置文件所在目录（使用默认路径请输入数字1）：");
                    wifiCfgFilePath = scanner.next();
                    if ("1".equals(wifiCfgFilePath)) {
                        wifiCfgFilePath = FOLDER;
                    }
                    file = new File(wifiCfgFilePath);
                    if (file.isDirectory()) {
                        file.listFiles();
                    } else {
                        System.out.println("WiFi配置文件目录不存在！");
                    }
                    List<String> fileList = getAllFiles(file);
                    for (String fileName : fileList) {
                        String tmpWifiCfgFilePath = wifiCfgFilePath + File.separator + fileName;
                        cmdImportWifi_byName = String.format(cmdImportWifi_byName, tmpWifiCfgFilePath);
                        String s = CmdUtils.runCmdGetString(cmdImportWifi_byName, "GBK");
                        if (s.contains("添加到接口")) {
                            System.out.println("\t" + tmpWifiCfgFilePath + " 导入成功... ...");
                        } else {
                            System.out.println(s);
                        }
                    }
                    System.out.println("所有WiFi全部导入完毕...");
                    System.out.println("");
                    break;
                // </editor-fold>
                case "E":
                    flag = false;
                    break;
                default:
                    System.err.println("无此操作....");
            }
            runIndex++;
        }

    }

    public static List<String> getAllFiles(File dir) throws Exception {
        List<String> fileList = new ArrayList<>();
        File[] fs = dir.listFiles();
        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                fileList.add(fs[i].getName());
                if (fs[i].isDirectory()) {
                    getAllFiles(fs[i]);
                }
            }
        }
        return fileList;
    }
}
