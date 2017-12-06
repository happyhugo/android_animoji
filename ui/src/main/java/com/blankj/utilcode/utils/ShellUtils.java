//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ShellUtils {
    private ShellUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static ShellUtils.CommandResult execCmd(String command, boolean isRoot) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    public static ShellUtils.CommandResult execCmd(List<String> commands, boolean isRoot) {
        return execCmd(commands == null?null:(String[])commands.toArray(new String[0]), isRoot, true);
    }

    public static ShellUtils.CommandResult execCmd(String[] commands, boolean isRoot) {
        return execCmd(commands, isRoot, true);
    }

    public static ShellUtils.CommandResult execCmd(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
    }

    public static ShellUtils.CommandResult execCmd(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(commands == null?null:(String[])commands.toArray(new String[0]), isRoot, isNeedResultMsg);
    }

    public static ShellUtils.CommandResult execCmd(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if(commands != null && commands.length != 0) {
            Process process = null;
            BufferedReader successResult = null;
            BufferedReader errorResult = null;
            StringBuilder successMsg = null;
            StringBuilder errorMsg = null;
            DataOutputStream os = null;

            try {
                process = Runtime.getRuntime().exec(isRoot?"su":"sh");
                os = new DataOutputStream(process.getOutputStream());
                String[] e = commands;
                int var11 = commands.length;

                for(int var12 = 0; var12 < var11; ++var12) {
                    String command = e[var12];
                    if(command != null) {
                        os.write(command.getBytes());
                        os.writeBytes("\n");
                        os.flush();
                    }
                }

                os.writeBytes("exit\n");
                os.flush();
                result = process.waitFor();
                if(isNeedResultMsg) {
                    successMsg = new StringBuilder();
                    errorMsg = new StringBuilder();
                    successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                    errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

                    String var19;
                    while((var19 = successResult.readLine()) != null) {
                        successMsg.append(var19);
                    }

                    while((var19 = errorResult.readLine()) != null) {
                        errorMsg.append(var19);
                    }
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            } finally {
                CloseUtils.closeIO(new Closeable[]{os, successResult, errorResult});
                if(process != null) {
                    process.destroy();
                }

            }

            return new ShellUtils.CommandResult(result, successMsg == null?null:successMsg.toString(), errorMsg == null?null:errorMsg.toString());
        } else {
            return new ShellUtils.CommandResult(result, (String)null, (String)null);
        }
    }

    public static class CommandResult {
        public int result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
