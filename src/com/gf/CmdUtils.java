package com.gf;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
public class CmdUtils {

    /**
     *
     * @param cmd
     * @return
     * @throws IOException
     */
    private static InputStream getCmdInputStream(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        return inputStream;
    }

    /**
     *
     * @param cmd
     * @param charset
     * @throws Exception
     */
    private static void checkParam(String cmd, String charset) throws Exception {
        if (cmd == null) throw new Exception("指令不完整！");
        if (cmd.trim().length() == 0) throw new Exception("指令不完整！");
        try {
            "".getBytes(charset);
        } catch (UnsupportedEncodingException u) {
            throw new Exception("编码类型[" + charset + "]不支持！");
        }
    }

    /**
     * 运行CMD指令并接收响应数据
     *
     * @param cmd     要运行的CMD命令
     * @param charset 字符编码，例如：GBK、UTF-8等
     * @return
     * @throws IOException
     */
    public static String runCmdGetString(String cmd, String charset) throws Exception {
        checkParam(cmd, charset);

        InputStream inputStream = getCmdInputStream(cmd);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        String line = "";
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        return stringBuilder.toString();
    }

    public static List<String> runCmdGetList(String cmd, String charset) throws Exception {
        checkParam(cmd, charset);

        List<String> resultList = new ArrayList<>();
        InputStream inputStream = getCmdInputStream(cmd);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
        String line = "";
        while ((line = reader.readLine()) != null) {
            resultList.add(line);
        }

        return resultList;
    }
}
