package com.julyyu.asplugins.tools.samplenode;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThrowableRunnable;
import com.julyyu.asplugins.utils.MustacheUtils;
import com.julyyu.asplugins.tools.samplenode.model.NodeEntity;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 组件类生成器
 */
public class WidgetNodeWriter {
    private Project project;

    private NodeEntity nodeEntity;

    private PsiFile psiFile;
    private MustacheUtils.GenDartFileListener genDartFileListener;
    private  WriteCommandAction.Builder builder;
    public WidgetNodeWriter(Project project, NodeEntity nodeEntity, PsiFile file) {

         builder = WriteCommandAction.writeCommandAction(project,file);
        this.project = project;
        this.nodeEntity = nodeEntity;
        this.psiFile = file;


    }


    public void start() {
        // 异步任务
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "WidgetNode") {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setIndeterminate(true);
                    try {
                        builder.run(new ThrowableRunnable<Throwable>() {
                            @Override
                            public void run() throws Throwable {
                                MustacheFactory mf = new DefaultMustacheFactory();
                                Mustache mustache = mf.compile("dart/widget_node.mustache");
                                String path = nodeEntity.getDir() + "/" + nodeEntity.getFileName();
                                File file = new File(path);
                                if (!file.getParentFile().exists()) {
                                    file.mkdirs();
                                }
                                try {
                                    file = new File(path);
                                    mustache.execute(new PrintWriter(file), nodeEntity).flush();
                                    if (genDartFileListener != null) {
                                        genDartFileListener.genDartFileResult(true);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    if (genDartFileListener != null) {
                                        genDartFileListener.genDartFileResult(false);
                                    }
                                }
                                psiFile.getParent().getVirtualFile().refresh(true, true);
                            }
                        });
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                    progressIndicator.setIndeterminate(false);
                    progressIndicator.setFraction(1.0);
                } catch (Exception e) {
                    progressIndicator.setIndeterminate(false);
                    progressIndicator.setFraction(1.0);
                }
            }
        });
    }

    public MustacheUtils.GenDartFileListener getGenDartFileListener() {
        return genDartFileListener;
    }

    public void setGenDartFileListener(MustacheUtils.GenDartFileListener genDartFileListener) {
        this.genDartFileListener = genDartFileListener;
    }

//    @Override
//    protected void run() throws Throwable {
//        MustacheFactory mf = new DefaultMustacheFactory();
//        Mustache mustache = mf.compile("dart/widget_node.mustache");
//        String path = nodeEntity.getDir() + "/" + nodeEntity.getFileName();
//        File file = new File(path);
//        if (!file.getParentFile().exists()) {
//            file.mkdirs();
//        }
//        try {
//            file = new File(path);
//            mustache.execute(new PrintWriter(file), nodeEntity).flush();
//            if (genDartFileListener != null) {
//                genDartFileListener.genDartFileResult(true);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            if (genDartFileListener != null) {
//                genDartFileListener.genDartFileResult(false);
//            }
//        }
//        psiFile.getParent().getVirtualFile().refresh(true, true);
//    }
}
