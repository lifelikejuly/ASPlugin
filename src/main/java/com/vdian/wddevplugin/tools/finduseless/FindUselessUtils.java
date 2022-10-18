package com.vdian.wddevplugin.tools.finduseless;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.vdian.wddevplugin.ui.checkboxtrees.CheckBoxTreeNode;
import com.vdian.wddevplugin.ui.error.ErrorDialog;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindUselessUtils {


    private static final String[] finalIgnoreDirs = new String[]{
            "/build", "/gradle",
    };

    public static void loopDirTrees(CheckBoxTreeNode node, Project project, List<String> ignoreDirs) {
        VirtualFile[] virtualFiles = ProjectRootManager.getInstance(project).getContentRoots();
        for (VirtualFile virtualFile : virtualFiles) {
            for (VirtualFile childFile : virtualFile.getChildren()) {
                if (childFile.getName().startsWith(".")) continue;
                loopDirTrees(ignoreDirs, node, childFile);
            }
        }
    }

    private static void loopDirTrees(List<String> ignoreDirs, CheckBoxTreeNode node, VirtualFile file) {
        if (file.isDirectory()) {
            if (!List.of(ignoreDirs).contains(file.getName()) &&
                    !file.getName().startsWith(".") &&
                    file.getChildren() != null) {
                CheckBoxTreeNode childNode = new CheckBoxTreeNode(file.getName(), file);
                node.add(childNode);
                for (VirtualFile childFile : file.getChildren()) {
                    loopDirTrees(ignoreDirs, childNode, childFile);
                }
            }
        }
    }

    public static List<ResFileInfo> findFileRes(File file, LoopFileCallback callback) {
        // 循环文件
        List<ResFileInfo> files = new ArrayList<>();
        if (file != null && file.isDirectory()) {
            files.addAll(loopDir(file.listFiles(), callback));
            return files;
        } else if (file != null &&
                !file.getName().startsWith(".") &&
                file.isFile()) {
            files.add(new ResFileInfo(file));
            try {
                String mimeType = Files.probeContentType(file.toPath());
                if (mimeType.startsWith("application") && callback != null) {
                    callback.callbackFindTextFile(file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                return files;
            }
        }
        return new ArrayList<>();
    }

    private static List<ResFileInfo> loopDir(File[] files, LoopFileCallback callback) {
        // 循环目录
        List<ResFileInfo> res = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (File file : files) {
                res.addAll(findFileRes(file, callback));
            }
        }
        return res;
    }


    public static void loopVirtualDir(List<String> ignoreDirs, VirtualFile file, List<ResFileInfo> findResFiles, LoopCallback loopCallback) {
        if (file == null) return;
        if (file.isDirectory()) {
            boolean isIgnore = false;
            for (String dir : finalIgnoreDirs) {
                if (file.getPath().endsWith(dir)) {
                    isIgnore = true;
                    break;
                }
            }
            if (!isIgnore &&
                    !ignoreDirs.contains(file.getPath()) &&
                    !file.getName().startsWith(".") &&
                    file.getChildren() != null)
                for (VirtualFile childFile : file.getChildren()) {
                    loopVirtualDir(ignoreDirs, childFile, findResFiles, loopCallback);
                }
        } else {
            if (file.getName().startsWith(".") ||
                    file.getName().endsWith(".apk")
//                    || file.getFileType().getName() != "PLAIN_TEXT"
            ) {
                return;
            }

            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    currentLine = currentLine.trim();
                    if (currentLine.startsWith("//")) continue; //
                    for (ResFileInfo f : findResFiles) {
                        if (containText(currentLine, f.getName())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("查询到[")
                                    .append(f.resFile.getPath())
                                    .append("]")
                                    .append("有在 >>>>>>> [")
                                    .append(file.getPath())
                                    .append("]")
                                    .append("文件中使用");
                            if (loopCallback != null)
                                loopCallback.callbackUsefulFile(f, new OutPutInfo(stringBuilder.toString()));
                        }
                    }
                }
            } catch (IOException e1) {
                ErrorDialog.showErrorDialog(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }


    public static void loopTextFiles(List<File> textFiles, List<ResFileInfo> findResFiles, LoopCallback loopCallback) {
        for (File textFile : textFiles) {
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(textFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String currentLine;
                while ((currentLine = br.readLine()) != null) {
                    currentLine = currentLine.trim();
                    if (currentLine.startsWith("//")) continue; // 忽略注释行
                    for (ResFileInfo f : findResFiles) {
                        if (containText(currentLine, f.getName())) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("查询到[")
                                    .append(f.resFile.getPath())
                                    .append("]")
                                    .append("有在 >>>>>>> [")
                                    .append(textFile.getPath())
                                    .append("]")
                                    .append("文件中使用");
                            if (loopCallback != null)
                                loopCallback.callbackUsefulFile(f, new OutPutInfo(stringBuilder.toString()));
                        }
                    }
                }
                fis.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static Pattern pattern = Pattern.compile("\\d+$");

    public static String returnNumEndName(String name) {
        String[] splits = name.split("\\.");
        if (splits.length == 2) {
            String headText = splits[0];
            Matcher matcher = pattern.matcher(headText);
            if (matcher.find()) {
                headText = headText.substring(0, headText.length() - matcher.group().length());
                return headText;
            }
            return name;

        }
        return name;

    }

    private static boolean containText(String textLine, String name) {
        return textLine.contains(name);
    }

    public interface LoopCallback {
        void callbackUsefulFile(ResFileInfo f, OutPutInfo outPutInfo);
    }

    public interface LoopFileCallback {
        void callbackFindTextFile(File f);
    }
}
