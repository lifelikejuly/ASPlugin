package com.julyyu.asplugins.tools.dartjsonformat;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.util.ThrowableRunnable;
import com.julyyu.asplugins.tools.dartjsonformat.model.MustacheEntity;
import com.julyyu.asplugins.utils.ClsUtils;
import com.julyyu.asplugins.utils.MustacheUtils;
import org.jetbrains.annotations.NotNull;


/**
 * @author shiki
 */
public class DartDataWriter {
    private Project project;
    private MustacheEntity mustacheEntity;
    private ClsUtils clsUtils;
    private MustacheUtils.GenDartFileListener genDartFileListener;
    private PsiFile file;

    private  WriteCommandAction.Builder builder;
    public DartDataWriter(Project project, ClsUtils clsUtils, MustacheEntity mustacheEntity, PsiFile file) {
        this.project = project;
        this.mustacheEntity = mustacheEntity;
        this.clsUtils = clsUtils;

        this.file = file;
        builder = WriteCommandAction.writeCommandAction(project,file);

    }

    public void start() {
        // 异步任务
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "DartJsonFormat") {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setIndeterminate(true);
                    try {
                        builder.run(new ThrowableRunnable<Throwable>() {
                            @Override
                            public void run() throws Throwable {
                                mustacheEntity.setDir(clsUtils.getClsDir(file));
                                MustacheUtils.genDartFile(mustacheEntity, genDartFileListener);
                                file.getParent().getVirtualFile().refresh(true, true);
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

//    @Override
//    protected void run() throws Throwable {
//        mustacheEntity.setDir(clsUtils.getClsDir(file));
//        MustacheUtils.genDartFile(mustacheEntity, genDartFileListener);
//        file.getParent().getVirtualFile().refresh(true, true);
//    }

    public MustacheUtils.GenDartFileListener getGenDartFileListener() {
        return genDartFileListener;
    }

    public void setGenDartFileListener(MustacheUtils.GenDartFileListener genDartFileListener) {
        this.genDartFileListener = genDartFileListener;
    }
}
