//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.blankj.utilcode.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    private FileUtils() {
        throw new UnsupportedOperationException("u can\'t instantiate me...");
    }

    public static File getFileByPath(String filePath) {
        return StringUtils.isSpace(filePath)?null:new File(filePath);
    }

    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(File file, String newName) {
        if(file == null) {
            return false;
        } else if(!file.exists()) {
            return false;
        } else if(StringUtils.isSpace(newName)) {
            return false;
        } else if(newName.equals(file.getName())) {
            return true;
        } else {
            File newFile = new File(file.getParent() + File.separator + newName);
            return !newFile.exists() && file.renameTo(newFile);
        }
    }

    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    public static boolean isDir(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }

    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(File file) {
        boolean var10000;
        label25: {
            if(file != null) {
                if(file.exists()) {
                    if(file.isDirectory()) {
                        break label25;
                    }
                } else if(file.mkdirs()) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    public static boolean createOrExistsFile(File file) {
        if(file == null) {
            return false;
        } else if(file.exists()) {
            return file.isFile();
        } else if(!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    public static boolean createFileByDeleteOldFile(File file) {
        if(file == null) {
            return false;
        } else if(file.exists() && file.isFile() && !file.delete()) {
            return false;
        } else if(!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    private static boolean copyOrMoveDir(String srcDirPath, String destDirPath, boolean isMove) {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
    }

    private static boolean copyOrMoveDir(File srcDir, File destDir, boolean isMove) {
        if(srcDir != null && destDir != null) {
            String srcPath = srcDir.getPath() + File.separator;
            String destPath = destDir.getPath() + File.separator;
            if(destPath.contains(srcPath)) {
                return false;
            } else if(srcDir.exists() && srcDir.isDirectory()) {
                if(!createOrExistsDir(destDir)) {
                    return false;
                } else {
                    File[] files = srcDir.listFiles();
                    File[] var6 = files;
                    int var7 = files.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        File file = var6[var8];
                        File oneDestFile = new File(destPath + file.getName());
                        if(file.isFile()) {
                            if(!copyOrMoveFile(file, oneDestFile, isMove)) {
                                return false;
                            }
                        } else if(file.isDirectory() && !copyOrMoveDir(file, oneDestFile, isMove)) {
                            return false;
                        }
                    }

                    return !isMove || deleteDir(srcDir);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean copyOrMoveFile(String srcFilePath, String destFilePath, boolean isMove) {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
    }

    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if(srcFile != null && destFile != null) {
            if(srcFile.exists() && srcFile.isFile()) {
                if(destFile.exists() && destFile.isFile()) {
                    return false;
                } else if(!createOrExistsDir(destFile.getParentFile())) {
                    return false;
                } else {
                    try {
                        return writeFileFromIS((File)destFile, new FileInputStream(srcFile), false) && (!isMove || deleteFile(srcFile));
                    } catch (FileNotFoundException var4) {
                        var4.printStackTrace();
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean copyDir(String srcDirPath, String destDirPath) {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    public static boolean copyDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    public static boolean copyFile(String srcFilePath, String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    public static boolean copyFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    public static boolean moveDir(String srcDirPath, String destDirPath) {
        return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    public static boolean moveDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    public static boolean moveFile(String srcFilePath, String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    public static boolean moveFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    public static boolean deleteDir(File dir) {
        if(dir == null) {
            return false;
        } else if(!dir.exists()) {
            return true;
        } else if(!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if(file.isFile()) {
                        if(!deleteFile(file)) {
                            return false;
                        }
                    } else if(file.isDirectory() && !deleteDir(file)) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }
    }

    public static boolean deleteFile(String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    public static boolean deleteFilesInDir(File dir) {
        if(dir == null) {
            return false;
        } else if(!dir.exists()) {
            return true;
        } else if(!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if(file.isFile()) {
                        if(!deleteFile(file)) {
                            return false;
                        }
                    } else if(file.isDirectory() && !deleteDir(file)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        if(!isDir(dir)) {
            return null;
        } else if(isRecursive) {
            return listFilesInDir(dir);
        } else {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                Collections.addAll(list, files);
            }

            return list;
        }
    }

    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    public static List<File> listFilesInDir(File dir) {
        if(!isDir(dir)) {
            return null;
        } else {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var3 = files;
                int var4 = files.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File file = var3[var5];
                    list.add(file);
                    if(file.isDirectory()) {
                        list.addAll(listFilesInDir(file));
                    }
                }
            }

            return list;
        }
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(File dir, String suffix, boolean isRecursive) {
        if(isRecursive) {
            return listFilesInDirWithFilter(dir, suffix);
        } else if(dir != null && isDir(dir)) {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var5 = files;
                int var6 = files.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    File file = var5[var7];
                    if(file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                        list.add(file);
                    }
                }
            }

            return list;
        } else {
            return null;
        }
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, String suffix) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix);
    }

    public static List<File> listFilesInDirWithFilter(File dir, String suffix) {
        if(dir != null && isDir(dir)) {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if(file.getName().toUpperCase().endsWith(suffix.toUpperCase())) {
                        list.add(file);
                    }

                    if(file.isDirectory()) {
                        list.addAll(listFilesInDirWithFilter(file, suffix));
                    }
                }
            }

            return list;
        } else {
            return null;
        }
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter, boolean isRecursive) {
        if(isRecursive) {
            return listFilesInDirWithFilter(dir, filter);
        } else if(dir != null && isDir(dir)) {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var5 = files;
                int var6 = files.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    File file = var5[var7];
                    if(filter.accept(file.getParentFile(), file.getName())) {
                        list.add(file);
                    }
                }
            }

            return list;
        } else {
            return null;
        }
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, FilenameFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    public static List<File> listFilesInDirWithFilter(File dir, FilenameFilter filter) {
        if(dir != null && isDir(dir)) {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if(filter.accept(file.getParentFile(), file.getName())) {
                        list.add(file);
                    }

                    if(file.isDirectory()) {
                        list.addAll(listFilesInDirWithFilter(file, filter));
                    }
                }
            }

            return list;
        } else {
            return null;
        }
    }

    public static List<File> searchFileInDir(String dirPath, String fileName) {
        return searchFileInDir(getFileByPath(dirPath), fileName);
    }

    public static List<File> searchFileInDir(File dir, String fileName) {
        if(dir != null && isDir(dir)) {
            ArrayList list = new ArrayList();
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if(file.getName().toUpperCase().equals(fileName.toUpperCase())) {
                        list.add(file);
                    }

                    if(file.isDirectory()) {
                        list.addAll(searchFileInDir(file, fileName));
                    }
                }
            }

            return list;
        } else {
            return null;
        }
    }

    public static boolean writeFileFromIS(String filePath, InputStream is, boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }

    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if(file != null && is != null) {
            if(!createOrExistsFile(file)) {
                return false;
            } else {
                BufferedOutputStream os = null;

                boolean len;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file, append));
                    byte[] e = new byte[1024];

                    int len1;
                    while((len1 = is.read(e, 0, 1024)) != -1) {
                        os.write(e, 0, len1);
                    }

                    boolean var6 = true;
                    return var6;
                } catch (IOException var10) {
                    var10.printStackTrace();
                    len = false;
                } finally {
                    CloseUtils.closeIO(new Closeable[]{is, os});
                }

                return len;
            }
        } else {
            return false;
        }
    }

    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }

    public static boolean writeFileFromString(File file, String content, boolean append) {
        if(file != null && content != null) {
            if(!createOrExistsFile(file)) {
                return false;
            } else {
                BufferedWriter bw = null;

                boolean var5;
                try {
                    bw = new BufferedWriter(new FileWriter(file, append));
                    bw.write(content);
                    boolean e = true;
                    return e;
                } catch (IOException var9) {
                    var9.printStackTrace();
                    var5 = false;
                } finally {
                    CloseUtils.closeIO(new Closeable[]{bw});
                }

                return var5;
            }
        } else {
            return false;
        }
    }

    public static List<String> readFile2List(String filePath, String charsetName) {
        return readFile2List(getFileByPath(filePath), charsetName);
    }

    public static List<String> readFile2List(File file, String charsetName) {
        return readFile2List((File)file, 0, 2147483647, charsetName);
    }

    public static List<String> readFile2List(String filePath, int st, int end, String charsetName) {
        return readFile2List(getFileByPath(filePath), st, end, charsetName);
    }

    public static List<String> readFile2List(File file, int st, int end, String charsetName) {
        if(file == null) {
            return null;
        } else if(st > end) {
            return null;
        } else {
            BufferedReader reader = null;

            Object curLine;
            try {
                int var14 = 1;
                ArrayList list = new ArrayList();
                if(StringUtils.isSpace(charsetName)) {
                    reader = new BufferedReader(new FileReader(file));
                } else {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
                }

                String e;
                for(; (e = reader.readLine()) != null && var14 <= end; ++var14) {
                    if(st <= var14 && var14 <= end) {
                        list.add(e);
                    }
                }

                ArrayList var8 = list;
                return var8;
            } catch (IOException var12) {
                var12.printStackTrace();
                curLine = null;
            } finally {
                CloseUtils.closeIO(new Closeable[]{reader});
            }

            return (List)curLine;
        }
    }

    public static String readFile2String(String filePath, String charsetName) {
        return readFile2String(getFileByPath(filePath), charsetName);
    }

    public static String readFile2String(File file, String charsetName) {
        if(file == null) {
            return null;
        } else {
            BufferedReader reader = null;

            String line;
            try {
                StringBuilder e = new StringBuilder();
                if(StringUtils.isSpace(charsetName)) {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                } else {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
                }

                while((line = reader.readLine()) != null) {
                    e.append(line).append("\r\n");
                }

                String var5 = e.delete(e.length() - 2, e.length()).toString();
                return var5;
            } catch (IOException var9) {
                var9.printStackTrace();
                line = null;
            } finally {
                CloseUtils.closeIO(new Closeable[]{reader});
            }

            return line;
        }
    }

    public static byte[] readFile2Bytes(String filePath) {
        return readFile2Bytes(getFileByPath(filePath));
    }

    public static byte[] readFile2Bytes(File file) {
        if(file == null) {
            return null;
        } else {
            try {
                return ConvertUtils.inputStream2Bytes(new FileInputStream(file));
            } catch (FileNotFoundException var2) {
                var2.printStackTrace();
                return null;
            }
        }
    }

    public static long getFileLastModified(String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    public static long getFileLastModified(File file) {
        return file == null?-1L:file.lastModified();
    }

    public static String getFileCharsetSimple(String filePath) {
        return getFileCharsetSimple(getFileByPath(filePath));
    }

    public static String getFileCharsetSimple(File file) {
        int p = 0;
        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException var7) {
            var7.printStackTrace();
        } finally {
            CloseUtils.closeIO(new Closeable[]{is});
        }

        switch(p) {
            case 61371:
                return "UTF-8";
            case 65279:
                return "UTF-16BE";
            case 65534:
                return "Unicode";
            default:
                return "GBK";
        }
    }

    public static int getFileLines(String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    public static int getFileLines(File file) {
        int count = 1;
        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] e = new byte[1024];

            int readChars;
            while((readChars = is.read(e, 0, 1024)) != -1) {
                for(int i = 0; i < readChars; ++i) {
                    if(e[i] == 10) {
                        ++count;
                    }
                }
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        } finally {
            CloseUtils.closeIO(new Closeable[]{is});
        }

        return count;
    }

    public static String getDirSize(String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    public static String getDirSize(File dir) {
        long len = getDirLength(dir);
        return len == -1L?"":ConvertUtils.byte2FitMemorySize(len);
    }

    public static String getFileSize(String filePath) {
        return getFileSize(getFileByPath(filePath));
    }

    public static String getFileSize(File file) {
        long len = getFileLength(file);
        return len == -1L?"":ConvertUtils.byte2FitMemorySize(len);
    }

    public static long getDirLength(String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    public static long getDirLength(File dir) {
        if(!isDir(dir)) {
            return -1L;
        } else {
            long len = 0L;
            File[] files = dir.listFiles();
            if(files != null && files.length != 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if(file.isDirectory()) {
                        len += getDirLength(file);
                    } else {
                        len += file.length();
                    }
                }
            }

            return len;
        }
    }

    public static long getFileLength(String filePath) {
        return getFileLength(getFileByPath(filePath));
    }

    public static long getFileLength(File file) {
        return !isFile(file)?-1L:file.length();
    }

    public static String getFileMD5ToString(String filePath) {
        File file = StringUtils.isSpace(filePath)?null:new File(filePath);
        return getFileMD5ToString(file);
    }

    public static byte[] getFileMD5(String filePath) {
        File file = StringUtils.isSpace(filePath)?null:new File(filePath);
        return getFileMD5(file);
    }

    public static String getFileMD5ToString(File file) {
        return ConvertUtils.bytes2HexString(getFileMD5(file));
    }

    public static byte[] getFileMD5(File file) {
        if(file == null) {
            return null;
        } else {
            DigestInputStream dis = null;

            try {
                FileInputStream e = new FileInputStream(file);
                MessageDigest md = MessageDigest.getInstance("MD5");
                dis = new DigestInputStream(e, md);
                byte[] buffer = new byte[262144];

                while(dis.read(buffer) > 0) {
                    ;
                }

                md = dis.getMessageDigest();
                byte[] var5 = md.digest();
                return var5;
            } catch (IOException | NoSuchAlgorithmException var9) {
                var9.printStackTrace();
            } finally {
                CloseUtils.closeIO(new Closeable[]{dis});
            }

            return null;
        }
    }

    public static String getDirName(File file) {
        return file == null?null:getDirName(file.getPath());
    }

    public static String getDirName(String filePath) {
        if(StringUtils.isSpace(filePath)) {
            return filePath;
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1?"":filePath.substring(0, lastSep + 1);
        }
    }

    public static String getFileName(File file) {
        return file == null?null:getFileName(file.getPath());
    }

    public static String getFileName(String filePath) {
        if(StringUtils.isSpace(filePath)) {
            return filePath;
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1?filePath:filePath.substring(lastSep + 1);
        }
    }

    public static String getFileNameNoExtension(File file) {
        return file == null?null:getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(String filePath) {
        if(StringUtils.isSpace(filePath)) {
            return filePath;
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1?(lastPoi == -1?filePath:filePath.substring(0, lastPoi)):(lastPoi != -1 && lastSep <= lastPoi?filePath.substring(lastSep + 1, lastPoi):filePath.substring(lastSep + 1));
        }
    }

    public static String getFileExtension(File file) {
        return file == null?null:getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if(StringUtils.isSpace(filePath)) {
            return filePath;
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastPoi != -1 && lastSep < lastPoi?filePath.substring(lastPoi + 1):"";
        }
    }
}
