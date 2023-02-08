package com.julyyu.asplugins.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import java.awt.*;

public class ProjectUtils {

    public static Project findProjectDir() {
        Project activeProject = null;
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects != null && projects.length > 0) {
            activeProject = projects[0];
            for (Project project : projects) {
                Window window = WindowManager.getInstance().suggestParentWindow(project);
                if (window != null && window.isActive()) {
                    activeProject = project;
                }
            }
        }
        return activeProject;
    }
}
